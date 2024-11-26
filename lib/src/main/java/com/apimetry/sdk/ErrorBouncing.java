package com.apimetry.sdk;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;

public class ErrorBouncing {
    private static final String ERROR_SDK_NOT_CONFIGURED = "err_sdk_not_configured";
    private static final String ERROR_REQUEST_MISSING_CUSTOMER = "err_request_missing_customer";
    private static final String ERROR_FAILED_TO_RECORD_REQUEST = "err_failed_to_record_request";

    private final ConcurrentHashMap<String, Instant> timestamps;

    public ErrorBouncing() {
        this.timestamps = new ConcurrentHashMap<>();
    }

    public synchronized boolean sdkNotConfigured() {
        return bounce(ERROR_SDK_NOT_CONFIGURED);
    }

    public synchronized boolean requestMissingCustomer() {
        return bounce(ERROR_REQUEST_MISSING_CUSTOMER);
    }

    public synchronized boolean failedToRecordRequest() {
        return bounce(ERROR_FAILED_TO_RECORD_REQUEST);
    }

    private boolean bounce(String error) {
        final Instant ts = Instant.now();
        final Instant last = this.timestamps.get(error);
        if (last == null) {
            this.timestamps.put(error, ts);
            return true;
        }
        if (Duration.between(ts, last).getSeconds() > 60) {
            this.timestamps.put(error, ts);
            return true;
        }
        return false;
    }

}
