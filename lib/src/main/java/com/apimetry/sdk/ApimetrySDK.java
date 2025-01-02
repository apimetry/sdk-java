package com.apimetry.sdk;


import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.StatusCode;
import io.opentelemetry.exporter.otlp.http.trace.OtlpHttpSpanExporter;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.SpanProcessor;
import io.opentelemetry.sdk.trace.export.BatchSpanProcessor;
import io.opentelemetry.sdk.trace.export.SimpleSpanProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ApimetrySDK {

    private static final Logger log = LoggerFactory.getLogger(ApimetrySDK.class);

    private final ApimetryConfig config;
    private final ErrorBouncing errors;
    private OpenTelemetrySdk otel;

    public ApimetrySDK(ApimetryConfig config) {
        this.config = config;
        this.errors = new ErrorBouncing();
    }

    public ApimetrySDK() {
        this(ApimetryConfig.fromEnv());
    }

    public boolean init() {
        if (this.config.getSatelliteURL() == null) {
            log.warn("satellite url not specified, sdk nullified!");
            return false;
        }
        final StringBuilder endpoint = new StringBuilder();
        endpoint.append(this.config.getSatelliteURL());
        if (!this.config.getSatelliteURL().endsWith("/")) {
            endpoint.append("/");
        }
        endpoint.append("v1/traces");
        final OtlpHttpSpanExporter exporter = OtlpHttpSpanExporter.builder()
            .setEndpoint(endpoint.toString())
            .setCompression("none")
            .build();


        final SpanProcessor processor = this.config.isDisableBatching()
            ? SimpleSpanProcessor.create(exporter)
            : BatchSpanProcessor.builder(exporter).build();

        final SdkTracerProvider tracer = SdkTracerProvider.builder()
            .addSpanProcessor(processor)
            .build();

        this.otel = OpenTelemetrySdk.builder()
            .setTracerProvider(tracer)
            .build();
        return true;
    }


    public void record(Request request) {
        if (this.otel == null) {
            if (this.errors.sdkNotConfigured()) {
                log.error("Apimetry SDK is not configured to record requests, call init()");
            }
            return;
        }
        if (request == null) {
            return;
        }
        if (request.getCustomer() == null || !request.getCustomer().isValid()) {
            if (this.errors.requestMissingCustomer()) {
                log.warn("request is missing customer, will not record, target='{}'", request.getTarget());
            }
            return;
        }
        try {
            final Span span = this.otel.getTracerProvider()
                .get("apimetry")
                .spanBuilder("request")
                .startSpan();
            span.setStatus(statusCodeFrom(request.getStatusCode()));
            span.setAttribute("http.method", request.getMethod().name());
            span.setAttribute("http.route", request.getRoute());
            span.setAttribute("http.target", request.getTarget());
            span.setAttribute("http.status_code", request.getStatusCode());
            span.setAttribute("http.body", request.getBody());
            span.setAttribute("apimetry.customer.id", request.getCustomer().getID());
            span.setAttribute("apimetry.customer.name", request.getCustomer().getName());
            span.end();
            if (request.getWorkspaceCode() != null) {
                span.setAttribute("apimetry.workspace.code", request.getWorkspaceCode());
            } else if (this.config.getDefaultWorkspaceCode() != null) {
                span.setAttribute("apimetry.workspace.code", this.config.getDefaultWorkspaceCode());
            }
            span.end();
        } catch (Exception e) {
            if (this.errors.failedToRecordRequest()) {
                log.error("failed to record request to Apimetry", e);
            }
        }
    }

    private static StatusCode statusCodeFrom(int status) {
        if (status >= 200 && status < 300) {
            return StatusCode.OK;
        }
        return StatusCode.ERROR;
    }
}
