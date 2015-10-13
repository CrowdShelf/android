package com.crowdshelf.app.io.network.responseHandlers;

import android.util.Log;

import com.crowdshelf.app.io.DbEvent;
import com.crowdshelf.app.io.DbEventType;
import com.crowdshelf.app.ui.activities.MainTabbedActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Torstein on 07.09.2015.
 */
public class UserListHandler implements ResponseHandler {

    @Override
    public void handleJsonResponse(String jsonString, DbEventType dbEventType) {
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObject.getJSONArray("users");
            UserHandler uh = new UserHandler();
            for (int i = 0; i < jsonArray.length(); i++) {
                uh.handleJsonResponse(jsonArray.getString(i), DbEventType.NONE);
            }
            MainTabbedActivity.getBus().post(new DbEvent(dbEventType, "all"));
        } catch (JSONException e){
            Log.w("UserListHandler", "something wrong with JSON data" + e.getMessage());
        }
    }
}
