package com.crowdshelf.app.io.network.responseHandlers;

import android.util.Log;

import com.crowdshelf.app.io.DbEventOk;
import com.crowdshelf.app.io.DbEventType;
import com.crowdshelf.app.ui.activities.MainTabbedActivity;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Torstein on 07.09.2015.
 */
public class CrowdListHandler implements ResponseHandler {

    @Override
    public void handleJsonResponse(String jsonString, DbEventType dbEventType) {
        JsonParser jsonParser = new JsonParser();
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObject.getJSONArray("crowds");
            CrowdHandler ch = new CrowdHandler();
            for (int i = 0; i < jsonArray.length(); i++) {
                ch.handleJsonResponse(jsonArray.getString(i), DbEventType.NONE);
            }
            MainTabbedActivity.getBus().post(new DbEventOk(dbEventType, "all"));
        } catch (JSONException e){
            Log.w("CrowdListHandler", "something wrong with JSON data" + e.getMessage());
        }
    }
}
