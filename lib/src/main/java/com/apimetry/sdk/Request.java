package com.apimetry.sdk;

import java.util.Objects;

public class Request {

    private String method;
    private String route;
    private String path;
    private int statusCode;
    private Customer customer;

    public Request() {

    }

    public String getMethod() {
        return this.method;
    }

    public void setMethod(String value) {
        this.method = value;
    }

    public String getRoute() {
        return this.route;
    }

    public void setRoute(String value) {
        this.route = value;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String value) {
        this.path = value;
    }

    public int getStatusCode() {
        return this.statusCode;
    }

    public void setStatusCode(int value) {
        this.statusCode = value;
    }

    public Customer getCustomer() {
        return this.customer;
    }

    public void setCustomer(Customer value) {
        this.customer = value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        final Request that = (Request) obj;
        return this.statusCode == that.statusCode
            && Objects.equals(this.method, that.method)
            && Objects.equals(this.route, that.route)
            && Objects.equals(this.path, that.path)
            && Objects.equals(this.customer, that.customer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.method, this.route, this.path, this.statusCode, this.customer);
    }
}
