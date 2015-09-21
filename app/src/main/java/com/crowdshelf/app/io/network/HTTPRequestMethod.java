package com.crowdshelf.app.io.network;

/**
 * Created by Torstein on 08.09.2015.
 */
public enum HTTPRequestMethod {
    POST("POST"),
    PUT("PUT"),
    GET("GET"),
    DELETE("DELETE");

    private final String requestMethod;

    HTTPRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    @Override
    public String toString() {
        return requestMethod;
    }
}
