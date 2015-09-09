package com.crowdshelf.app.network.responseHandlers;

import com.crowdshelf.app.MainController;
import com.crowdshelf.app.models.User;
import com.google.gson.JsonSyntaxException;

/**
 * Created by Torstein on 07.09.2015.
 */
public class UserHandler implements ResponseHandler {
    @Override
    public void handleJsonResponse(String jsonString) {
        try {
            User user = gson.fromJson(jsonString, User.class);
            MainController.receiveUser(user);
        } catch (JsonSyntaxException e) {
            System.out.print("Received user was not in expected format\n");
            e.printStackTrace();
        }
    }
}
