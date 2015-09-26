package com.crowdshelf.app.io.network.responseHandlers;

import android.util.Log;

import com.crowdshelf.app.io.DBEventType;
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
    public void handleJsonResponse(String jsonString, DBEventType dbEventType) {
        JsonParser jsonParser = new JsonParser();
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObject.getJSONArray("crowds");
            CrowdHandler ch = new CrowdHandler();
            for (int i = 0; i < jsonArray.length(); i++) {
                ch.handleJsonResponse(jsonArray.getString(i), dbEventType);
            }
            /*
            Just for verification:
             */
 /*           Realm realm = Realm.getDefaultInstance();
            realm.beginTransaction();
            RealmResults<Crowd> results = realm.allObjects(Crowd.class);
            Log.i("UserHandler", "added " + String.valueOf(results.size()) + " crowds to the database");
            realm.commitTransaction();
            realm.close();*/
        } catch (JSONException e){
            Log.w("UserHandler", "something wrong with JSON data" + e.getMessage());
        }
    }
}
