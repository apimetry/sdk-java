package com.apimetry.sdk;

import java.util.Objects;

public class Customer {
    private final String id;
    private final String name;

    public Customer(String id) {
        this(id, "");
    }

    public Customer(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getID() {
        return this.id;
    }

    public String getName() {
        return this.name;
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
