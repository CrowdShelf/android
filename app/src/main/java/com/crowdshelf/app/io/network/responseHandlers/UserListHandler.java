package com.crowdshelf.app.io.network.responseHandlers;

import android.util.Log;

import com.crowdshelf.app.models.Crowd;
import com.crowdshelf.app.models.User;
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
    public void handleJsonResponse(String jsonString) {
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObject.getJSONArray("users");
            UserHandler uh = new UserHandler();
            for (int i = 0; i < jsonArray.length(); i++) {
                uh.handleJsonResponse(jsonArray.getString(i));
            }
            /*
            Just for verification:
             */
            Realm realm = Realm.getDefaultInstance();
            realm.beginTransaction();
            RealmResults<User> results = realm.allObjects(User.class);
            Log.i("UserHandler", "UserListHandler added " + String.valueOf(results.size()) + " users to the database");
            realm.commitTransaction();
            realm.close();
        } catch (JSONException e){
            Log.w("UserHandler", "UserList something wrong with JSON data");
            Log.w("UserHandler", e.getMessage());
        }
    }
}
