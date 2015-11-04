package com.crowdshelf.app.io.network.responseHandlers;

import android.util.Log;

import com.crowdshelf.app.io.DbEventOk;
import com.crowdshelf.app.io.DbEventType;
import com.crowdshelf.app.models.Crowd;
import com.crowdshelf.app.ui.activities.MainTabbedActivity;
import com.google.gson.JsonSyntaxException;

import io.realm.Realm;

/**
 * Created by Torstein on 07.09.2015.
 */
public class CrowdHandler implements ResponseHandler {
    private static final String TAG = "CrowdHandler";
    @Override
    public void handleJsonResponse(String jsonString, DbEventType dbEventType) {
        try {
            Crowd c = gson.fromJson(jsonString, Crowd.class);
            Log.i(TAG, "added _id " + c.getId() + " name " + c.getName() + " owner " + c.getOwner() + " members " + c.getMembers().toString());
            Realm realm = Realm.getDefaultInstance();
            realm.beginTransaction();
            realm.copyToRealmOrUpdate(c);
            realm.commitTransaction();
            realm.close();
            if (c.getId().equals("")) {
                Log.w(TAG, "Received crowd does not have an id!");
            }
            MainTabbedActivity.getBus().post(new DbEventOk(dbEventType, c.getId()));
        } catch (JsonSyntaxException e) {
            Log.w(TAG, "something wrong with JSON data" +  e.getMessage());
        } catch (RuntimeException e) {
            Log.w(TAG, e.getMessage());
        }
    }
}
