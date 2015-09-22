package com.crowdshelf.app.io.network.responseHandlers;

import android.util.Log;

import com.crowdshelf.app.models.Crowd;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Torstein on 07.09.2015.
 */
public class CrowdListHandler implements ResponseHandler {

    @Override
    public void handleJsonResponse(String jsonString) {
        JsonParser jsonParser = new JsonParser();
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObject.getJSONArray("crowds");
            CrowdHandler ch = new CrowdHandler();
            for (int i = 0; i < jsonArray.length(); i++) {
                ch.handleJsonResponse(jsonArray.getString(i));
            }
            /*
            Just for verification:
             */
            Realm realm = Realm.getDefaultInstance();
            realm.beginTransaction();
            RealmResults<Crowd> results = realm.allObjects(Crowd.class);
            Log.i("UserHandler", "added " + String.valueOf(results.size()) + " crowds to the database");
            realm.commitTransaction();
            realm.close();
        } catch (JSONException e){
            Log.w("UserHandler", "something wrong with JSON data");
            Log.w("UserHandler", e.getMessage());
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
}