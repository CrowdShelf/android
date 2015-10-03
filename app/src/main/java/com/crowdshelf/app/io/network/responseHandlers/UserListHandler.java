package com.crowdshelf.app.io.network.responseHandlers;

import android.util.Log;

import com.crowdshelf.app.io.DBEvent;
import com.crowdshelf.app.io.DBEventType;
import com.crowdshelf.app.models.Crowd;
import com.crowdshelf.app.models.User;
import com.crowdshelf.app.ui.activities.MainTabbedActivity;
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
public class UserListHandler implements ResponseHandler {

    @Override
    public void handleJsonResponse(String jsonString, DBEventType dbEventType) {
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObject.getJSONArray("users");
            UserHandler uh = new UserHandler();
            for (int i = 0; i < jsonArray.length(); i++) {
                uh.handleJsonResponse(jsonArray.getString(i), null);
            }
            MainTabbedActivity.getBus().post(new DBEvent(dbEventType, "all"));
        } catch (JSONException e){
            Log.w("UserListHandler", "something wrong with JSON data" + e.getMessage());
        }
    }
}
