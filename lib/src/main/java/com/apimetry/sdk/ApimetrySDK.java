package com.apimetry.sdk;


import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.StatusCode;
import io.opentelemetry.exporter.otlp.http.trace.OtlpHttpSpanExporter;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.SpanProcessor;
import io.opentelemetry.sdk.trace.export.BatchSpanProcessor;
import io.opentelemetry.sdk.trace.export.SimpleSpanProcessor;


public class ApimetrySDK {

    private final ApimetryConfig config;
    private OpenTelemetrySdk otel;

    public ApimetrySDK(ApimetryConfig config) {
        this.config = config;
    }

    public ApimetrySDK() {
        this(ApimetryConfig.fromEnv());
    }

    public boolean init() {
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
            return;
        }
        if (request == null || request.getCustomer() == null || !request.getCustomer().isValid()) {
            return;
        }
        try {
            final Span span = this.otel.getTracerProvider()
                .get("apimetry")
                .spanBuilder("request")
                .startSpan();
             try {
                 span.setStatus(statusCodeFrom(request.getStatusCode()));
                 span.setAttribute("http.method", request.getMethod().name());
                 span.setAttribute("http.route", request.getRoute());
                 span.setAttribute("url.path", request.getPath());
                 span.setAttribute("http.status_code", request.getStatusCode());
                 span.setAttribute("http.body", request.getBody());
                 span.setAttribute("apimetry.customer.id", request.getCustomer().getID());
                 span.setAttribute("apimetry.customer.name", request.getCustomer().getName());
                 if (request.getWorkspaceCode() != null) {
                     span.setAttribute("apimetry.workspace.code", request.getWorkspaceCode());
                 } else if (this.config.getDefaultWorkspaceCode() != null) {
                     span.setAttribute("apimetry.workspace.code", this.config.getDefaultWorkspaceCode());
                 }
             } catch (Exception e) {
             }
            span.end();
        } catch (Exception e) {
        }
    }

    private static StatusCode statusCodeFrom(int status) {
        if (status >= 200 && status < 300) {
            return StatusCode.OK;
        }
        return StatusCode.ERROR;
    }
}
