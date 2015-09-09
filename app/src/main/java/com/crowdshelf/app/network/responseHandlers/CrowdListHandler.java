package com.crowdshelf.app.network.responseHandlers;

import com.crowdshelf.app.MainController;
import com.crowdshelf.app.models.Crowd;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by Torstein on 07.09.2015.
 */
public class CrowdListHandler implements ResponseHandler {
    private static Type crowdListType = new TypeToken<List<Crowd>>(){}.getType();

    @Override
    public void handleJsonResponse(String jsonString) {

        JsonParser jsonParser = new JsonParser();
        JsonElement jsonElement = jsonParser.parse(jsonString);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonArray jsonArray = jsonObject.getAsJsonArray("crowds");

        /*
        // Method 1
        CrowdHandler crowdHandler = new CrowdHandler();
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonElement e = jsonArray.get(i);
            crowdHandler.handleJsonResponse(e.toString());
        }
        */

        // Method 2
        try {
            List<Crowd> crowds = gson.fromJson(jsonArray, crowdListType);
            MainController.receiveCrowds(crowds);
        } catch (JsonSyntaxException e) {
            System.out.print("Received crowds was not in expected format\n");
            e.printStackTrace();
        }
    }
}
