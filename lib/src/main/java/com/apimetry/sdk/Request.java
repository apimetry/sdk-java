package com.apimetry.sdk;

import java.util.Objects;

public class Request {

    private HTTPMethod method;
    private String route;
    private String target;
    private int statusCode;
    private Customer customer;
    private String body;
    private String workspaceCode;

    public Request() {

    }

    public HTTPMethod getMethod() {
        return this.method;
    }

    public void setMethod(HTTPMethod value) {
        this.method = value;
    }

    public String getRoute() {
        return this.route;
    }

    public void setRoute(String value) {
        this.route = value;
    }

    public String getTarget() {
        return this.target;
    }

    public void setTarget(String value) {
        this.target = value;
    }

    public int getStatusCode() {
        return this.statusCode;
    }

    public void setStatusCode(int value) {
        this.statusCode = value;
    }

    public String getBody() {
        return this.body;
    }

    public void setBody(String value) {
        this.body = value;
    }

    public Customer getCustomer() {
        return this.customer;
    }

    public void setCustomer(Customer value) {
        this.customer = value;
    }

    public String getWorkspaceCode() {
        return this.workspaceCode;
    }

    public void setWorkspaceCode(String value) {
        this.workspaceCode = value;
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
            && Objects.equals(this.target, that.target)
            && Objects.equals(this.body, that.body)
            && Objects.equals(this.customer, that.customer)
            && Objects.equals(this.workspaceCode, that.workspaceCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            this.method,
            this.route,
            this.target,
            this.statusCode,
            this.body,
            this.workspaceCode,
            this.customer
        );
    }
}
