package com.apimetry.sdk;

public enum HTTPMethod {
    GET,
    POST,
    PUT,
    DELETE,
    PATCH;

    public static HTTPMethod fromString(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        final String cleaned = value.trim().toUpperCase();
        if (cleaned.equals("GET")) {
            return GET;
        }
        if (cleaned.equals("POST")) {
            return POST;
        }
        if (cleaned.equals("PUT")) {
            return PUT;
        }
        if (cleaned.equals("DELETE")) {
            return DELETE;
        }
        if (cleaned.equals("PATCH")) {
            return PATCH;
        }
        return null;
    }
}
