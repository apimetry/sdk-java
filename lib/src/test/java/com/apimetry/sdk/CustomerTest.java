package com.apimetry.sdk;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CustomerTest {

    @Test
    void test_valid_customer() {
        assertThat(new Customer(null, "").isValid()).isFalse();
        assertThat(new Customer("   ", "").isValid()).isFalse();
        assertThat(new Customer("28575", "Microsoft").isValid()).isTrue();
    }
}