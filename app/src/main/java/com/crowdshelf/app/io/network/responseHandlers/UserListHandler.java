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

/**
 * Created by Torstein on 07.09.2015.
 */
public class UserListHandler implements ResponseHandler {
    private static Type crowdListType = new TypeToken<List<Crowd>>(){}.getType();

    @Override
    public void handleJsonResponse(String jsonString) {
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObject.getJSONArray("users");

            Realm realm = Realm.getDefaultInstance();
            realm.beginTransaction();
            realm.createOrUpdateAllFromJson(User.class, jsonArray);
            realm.commitTransaction();
            realm.close();
        } catch (JSONException e){
            Log.d("NETDBTEST", "UserList something wrong with JSON data");
            e.printStackTrace();
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
