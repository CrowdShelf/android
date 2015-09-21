package com.crowdshelf.app.io.network.responseHandlers;

import android.util.Log;

import com.crowdshelf.app.MainController;
import com.crowdshelf.app.io.DBEvent;
import com.crowdshelf.app.io.DBEventType;
import com.crowdshelf.app.models.User;
import com.crowdshelf.app.ui.activities.MainTabbedActivity;
import com.google.gson.JsonSyntaxException;

import io.realm.Realm;

/**
 * Created by Torstein on 07.09.2015.
 */
public class UserHandler implements ResponseHandler {
    @Override
    public void handleJsonResponse(String jsonString) {
        try {
            User u = gson.fromJson(jsonString, User.class);
            Log.d("NETDBTEST", "User added _id " + u.getId() + " username" + u.getUsername() + " name " + u.getName() + " email" + u.getEmail());
            Realm realm = Realm.getDefaultInstance();
            realm.beginTransaction();
            realm.copyToRealmOrUpdate(u);
            realm.commitTransaction();
            realm.close();
            MainTabbedActivity.getBus().post(new DBEvent(DBEventType.USER_READY, u.getId()));
        } catch (JsonSyntaxException e) {
            Log.d("NETDBTEST", "CrowdHandler something wrong with JSON data");
            e.printStackTrace();
        }
    }
}
