package com.apimetry.sdk;

import java.util.Objects;
import java.util.Optional;

public class Customer {
    private final String id;
    private final String name;

    public Customer(String id) {
        this(id, "");
    }

    public Customer(String id, String name) {
        this.id = Optional.ofNullable(id).map(String::trim).orElse("");
        this.name = Optional.ofNullable(name).map(String::trim).orElse("");
    }

    public String getID() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public boolean isValid() {
        if (this.id == null) {
            return false;
        }
        return !this.id.trim().isEmpty();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        final Customer that = (Customer) obj;
        return Objects.equals(this.id, that.id)
            && Objects.equals(this.name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.name);
    }
}
