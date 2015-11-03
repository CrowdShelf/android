package com.crowdshelf.app.io.network;

/**
 * Created by Torstein on 03.11.2015.
 */
public class Response {
    private String jsonData;
    private int responseCode;
    private String responseMessage;

    public Response(String jsonData, int responseCode, String responseMessage) {
        this.jsonData = jsonData;
        this.responseCode = responseCode;
        this.responseMessage = responseMessage;
    }

    public String getJsonData() {
        return jsonData;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }
}
