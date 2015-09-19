package com.crowdshelf.app.network.responseHandlers;

import com.crowdshelf.app.MainController;
import com.crowdshelf.app.models.Crowd;
import com.google.gson.JsonSyntaxException;

import io.realm.Realm;

/**
 * Created by Torstein on 07.09.2015.
 */
public class CrowdHandler implements ResponseHandler {
    @Override
    public void handleJsonResponse(String jsonString) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.createOrUpdateObjectFromJson(Crowd.class, jsonString);
        realm.commitTransaction();
    }
}
