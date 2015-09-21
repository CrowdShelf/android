package com.crowdshelf.app.io.network.responseHandlers;

import android.util.Log;

import com.crowdshelf.app.models.Crowd;
import com.google.gson.JsonSyntaxException;

import io.realm.Realm;

/**
 * Created by Torstein on 07.09.2015.
 */
public class CrowdHandler implements ResponseHandler {
    @Override
    public void handleJsonResponse(String jsonString) {
        try {
            Crowd c = gson.fromJson(jsonString, Crowd.class);
            Log.d("NETDBTEST", "Crowd added _id " + c.getId() + " name " + c.getName() + " owner " + c.getOwner() + " members " + c.getMembers().toString());
            Realm realm = Realm.getDefaultInstance();
            realm.beginTransaction();
            realm.copyToRealmOrUpdate(c);
            realm.commitTransaction();
            realm.close();
        } catch (JsonSyntaxException e) {
            Log.d("NETDBTEST", "CrowdHandler something wrong with JSON data");
            e.printStackTrace();
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
