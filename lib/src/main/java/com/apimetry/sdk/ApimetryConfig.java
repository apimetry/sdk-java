package com.apimetry.sdk;

public class ApimetryConfig {

    private String satelliteURL;
    private String defaultWorkspaceCode;

    public static ApimetryConfig baseConfig() {
        return new ApimetryConfig();
    }

    public static ApimetryConfig fromEnv() {
        final ApimetryConfig config = baseConfig();
        config.setSatelliteURL(System.getenv("APIMETRY_SATELLITE_URL"));
        config.setDefaultWorkspaceCode(System.getenv("APIMETRY_DEFAULT_WORKSPACE_CODE"));
        return config;
    }

    public String getSatelliteURL() {
        return this.satelliteURL;
    }

    public void setSatelliteURL(String url) {
        this.satelliteURL = url;
    }

    public String getDefaultWorkspaceCode() {
        return this.defaultWorkspaceCode;
    }

    public void setDefaultWorkspaceCode(String value) {
        this.defaultWorkspaceCode = value;
    }
}
