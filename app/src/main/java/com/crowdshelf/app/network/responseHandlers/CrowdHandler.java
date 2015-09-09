package com.crowdshelf.app.network.responseHandlers;

import com.crowdshelf.app.MainController;
import com.crowdshelf.app.models.Crowd;
import com.google.gson.JsonSyntaxException;

/**
 * Created by Torstein on 07.09.2015.
 */
public class CrowdHandler implements ResponseHandler {
    @Override
    public void handleJsonResponse(String jsonString) {
        // THIS WILL WORK WHEN API IS UPDATED TO INCLUDE USER OBJECTS IN CROWDS
        try {
            Crowd crowd = gson.fromJson(jsonString, Crowd.class);
            MainController.receiveCrowd(crowd);
        } catch (JsonSyntaxException e) {
            System.out.print("Received crowd was not in expected format\n");
            e.printStackTrace();
        }
    }
}
