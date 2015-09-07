package com.crowdshelf.app.network.responseHandlers;

import com.crowdshelf.app.MainController;
import com.crowdshelf.app.models.User;

/**
 * Created by Torstein on 07.09.2015.
 */
public class UserHandler implements ResponseHandler {
    @Override
    public void handleJsonResponse(String jsonString) {
        User user = gson.fromJson(jsonString, User.class);
        MainController.receiveUser(user);
    }
}
