package com.apimetry.sdk;


import io.opentelemetry.api.trace.Span;
import io.opentelemetry.exporter.otlp.http.trace.OtlpHttpSpanExporter;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.export.BatchSpanProcessor;

public class ApimetrySDK {

    private final ApimetryConfig config;
    private OpenTelemetrySdk otel;

    public ApimetrySDK(ApimetryConfig config) {
        this.config = config;
    }

    public ApimetrySDK() {
        this(ApimetryConfig.fromEnv());
    }

    public void init() {
        final OtlpHttpSpanExporter exporter = OtlpHttpSpanExporter.builder()
            .setEndpoint(this.config.getSatelliteURL())
            .setCompression("none")
            .build();

        final SdkTracerProvider tracer = SdkTracerProvider.builder()
            .addSpanProcessor(BatchSpanProcessor.builder(exporter).build())
            .build();

        this.otel = OpenTelemetrySdk.builder()
            .setTracerProvider(tracer)
            .build();
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
                 span.setAttribute("http.method", request.getMethod());
                 span.setAttribute("http.route", request.getRoute());
                 span.setAttribute("url.path", request.getPath());
                 span.setAttribute("http.status_code", request.getStatusCode());
                 span.setAttribute("apimetry.customer.id", request.getCustomer().getID());
                 span.setAttribute("apimetry.customer.name", request.getCustomer().getName());
             } catch (Exception e) {

             }
            span.end();
        } catch (Exception e) {

        }
    }
}
