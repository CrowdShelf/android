package com.crowdshelf.app.network.gsonHelpers;

import com.crowdshelf.app.MainController;
import com.crowdshelf.app.models.Crowd;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by Torstein on 07.09.2015.
 */
public class CrowdHandler implements JsonHandler {
    @Override
    public void handleJsonResponse(String jsonString) {
        // THIS WILL WORK WHEN API IS UPDATED TO INCLUDE USER OBJECTS IN CROWDS

        Crowd crowd = gson.fromJson(jsonString, Crowd.class);
        MainController.receiveCrowd(crowd);
    }
}
