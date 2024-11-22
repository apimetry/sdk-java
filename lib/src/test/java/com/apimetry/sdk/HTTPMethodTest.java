package com.apimetry.sdk;

import org.junit.jupiter.api.Test;

import static com.apimetry.sdk.HTTPMethod.*;
import static org.assertj.core.api.Assertions.assertThat;

class HTTPMethodTest {

    @Test
    void test_from_string() {
        assertThat(HTTPMethod.fromString("get")).isEqualTo(GET);
        assertThat(HTTPMethod.fromString("GET")).isEqualTo(GET);
        assertThat(HTTPMethod.fromString("  GET   ")).isEqualTo(GET);

        assertThat(HTTPMethod.fromString("POST")).isEqualTo(POST);
        assertThat(HTTPMethod.fromString("PUT")).isEqualTo(PUT);
        assertThat(HTTPMethod.fromString("DELETE")).isEqualTo(DELETE);
        assertThat(HTTPMethod.fromString("PATCH")).isEqualTo(PATCH);
    }
}