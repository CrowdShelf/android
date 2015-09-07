package com.crowdshelf.app.network.gsonHelpers;

import com.crowdshelf.app.MainController;
import com.crowdshelf.app.models.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by Torstein on 07.09.2015.
 */
public class UserHandler implements JsonHandler {
    @Override
    public void handleJsonResponse(String jsonString) {
        User user = gson.fromJson(jsonString, User.class);
        MainController.receiveUser(user);
    }
}
