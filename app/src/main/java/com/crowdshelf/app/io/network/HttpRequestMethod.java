package com.crowdshelf.app.io.network;

/**
 * Created by Torstein on 08.09.2015.
 */
public enum HttpRequestMethod {
    POST("POST"),
    PUT("PUT"),
    GET("GET"),
    DELETE("DELETE");

    private final String requestMethod;

    HttpRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    @Override
    public String toString() {
        return requestMethod;
    }
}
