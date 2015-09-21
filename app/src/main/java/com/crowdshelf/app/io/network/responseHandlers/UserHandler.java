package com.crowdshelf.app.io.network.responseHandlers;

import com.crowdshelf.app.models.User;

import io.realm.Realm;

/**
 * Created by Torstein on 07.09.2015.
 */
public class UserHandler implements ResponseHandler {
    @Override
    public void handleJsonResponse(String jsonString) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.createOrUpdateObjectFromJson(User.class, jsonString);
        realm.commitTransaction();
        realm.close();
    }
}
