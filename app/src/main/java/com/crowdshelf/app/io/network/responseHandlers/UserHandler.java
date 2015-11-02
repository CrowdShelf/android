package com.crowdshelf.app.io.network.responseHandlers;

import android.util.Log;

import com.crowdshelf.app.io.DbEvent;
import com.crowdshelf.app.io.DbEventType;
import com.crowdshelf.app.models.User;
import com.crowdshelf.app.ui.activities.MainTabbedActivity;
import com.google.gson.JsonSyntaxException;

import io.realm.Realm;

/**
 * Created by Torstein on 07.09.2015.
 */
public class UserHandler implements ResponseHandler {
    private static final String TAG = "UserHandler";
    @Override
    public void handleJsonResponse(String jsonString, DbEventType dbEventType) {
        try {
            /*
            @todo: get token
             */
            User u = gson.fromJson(jsonString, User.class);
            Log.i(TAG, "User added _id " + u.getId() + " username " + u.getUsername() + " name " + u.getName() + " email " + u.getEmail() + " token " + u.getToken());
            Realm realm = Realm.getDefaultInstance();
            realm.beginTransaction();
            realm.copyToRealmOrUpdate(u);
            realm.commitTransaction();
            realm.close();
            if (u.getId().equals("")) {
                Log.w(TAG, "Received user does not have an id!");
            }
            MainTabbedActivity.getBus().post(new DbEvent(dbEventType, u.getId()));
        } catch (JsonSyntaxException e) {
            Log.w(TAG, "something wrong with JSON data" + e.getMessage());;
        } catch (RuntimeException e) {
            Log.w(TAG, e.getMessage());
        }
    }
}
