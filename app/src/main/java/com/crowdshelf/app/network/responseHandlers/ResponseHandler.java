package com.crowdshelf.app.network.responseHandlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by Torstein on 07.09.2015.
 */
public interface ResponseHandler {
    static Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    void handleJsonResponse(String jsonString);
}
