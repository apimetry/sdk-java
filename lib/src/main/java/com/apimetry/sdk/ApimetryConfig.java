package com.apimetry.sdk;

public class ApimetryConfig {

    private String satelliteURL;

    public static ApimetryConfig baseConfig() {
        return new ApimetryConfig();
    }

    public static ApimetryConfig fromEnv() {
        final ApimetryConfig config = baseConfig();
        config.setSatelliteURL(System.getenv("APIMETRY_SATELLITE_URL"));
        return config;
    }

    public String getSatelliteURL() {
        return this.satelliteURL;
    }

    public void setSatelliteURL(String url) {
        this.satelliteURL = url;
    }

}
