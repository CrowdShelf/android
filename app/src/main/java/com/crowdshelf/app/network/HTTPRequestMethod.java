package com.crowdshelf.app.network;

/**
 * Created by Torstein on 08.09.2015.
 */
public enum HTTPRequestMethod {
    POST("POST"),
    PUT("PUT"),
    GET("GET");

    private final String requestMethod;

    private HTTPRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    @Override
    public String toString() {
        return requestMethod;
    }
}
