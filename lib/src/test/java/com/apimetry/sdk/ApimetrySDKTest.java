package com.apimetry.sdk;

import org.junit.jupiter.api.Test;

class ApimetrySDKTest {

    @Test
    void test() throws Exception {
        ApimetryConfig config = new ApimetryConfig();
        config.setSatelliteURL("http://localhost:4318");
        config.setDisableBatching(true);

        ApimetrySDK apimetry = new ApimetrySDK(config);
        apimetry.init();

        Request request = new Request();
        request.setMethod(HTTPMethod.GET);
        request.setRoute("/users/{id}");
        request.setTarget("/users/123");
        request.setCustomer(new Customer("1", "Microsoft"));
        request.setStatusCode(200);

        apimetry.record(request);

        Thread.sleep(5000);
    }
}