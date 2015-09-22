package com.crowdshelf.app.io.network.responseHandlers;

import android.util.Log;

import com.crowdshelf.app.MainController;
import com.crowdshelf.app.io.DBEvent;
import com.crowdshelf.app.io.DBEventType;
import com.crowdshelf.app.models.Crowd;
import com.crowdshelf.app.models.MemberId;
import com.crowdshelf.app.ui.activities.MainTabbedActivity;
import com.google.gson.JsonSyntaxException;

import java.lang.reflect.Type;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;

/**
 * Created by Torstein on 07.09.2015.
 */
public class CrowdHandler implements ResponseHandler {
    @Override
    public void handleJsonResponse(String jsonString, DBEventType dbEventType) {
        try {
            Crowd c = gson.fromJson(jsonString, Crowd.class);
            Log.i("CrowdHandler", "added _id " + c.getId() + " name " + c.getName() + " owner " + c.getOwner() + " members " + c.getMembers().toString());
            Realm realm = Realm.getDefaultInstance();
            realm.beginTransaction();
            realm.copyToRealmOrUpdate(c);
            realm.commitTransaction();
            realm.close();
            if (c.getId().equals("")) {
                Log.w("CrowdHandler", "Received crowd does not have an id!");
            }
            MainTabbedActivity.getBus().post(new DBEvent(dbEventType, c.getId()));
        } catch (JsonSyntaxException e) {
            Log.w("CrowdHandler", "something wrong with JSON data");
            Log.w("CrowdHandler", e.getMessage());
        } catch (RuntimeException e) {
            Log.w("CrowdHandler", e.getMessage());
        }
    }

    /*
        // Method 2
        try {
            List<Crowd> crowds = gson.fromJson(jsonArray, crowdListType);
            MainController.receiveCrowds(crowds);
        } catch (JsonSyntaxException e) {
            System.out.print("Received crowds was not in expected format\n");
            e.printStackTrace();
        }
        */

}
