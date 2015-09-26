package com.crowdshelf.app.io.network.responseHandlers;

import android.util.Log;

import com.crowdshelf.app.MainController;
import com.crowdshelf.app.io.DBEventType;
import com.crowdshelf.app.models.Book;
import com.crowdshelf.app.ui.activities.MainTabbedActivity;
import com.google.gson.JsonParser;

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
public class BookListHandler implements ResponseHandler {

    @Override
    public void handleJsonResponse(String jsonString, DBEventType dbEventType) {
        JsonParser jsonParser = new JsonParser();
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObject.getJSONArray("books");
            BookHandler bh = new BookHandler();
            for (int i = 0; i < jsonArray.length(); i++) {
                bh.handleJsonResponse(jsonArray.getString(i), dbEventType);
            }
            /*
            Just for verification:
             */

            /*Realm realm = Realm.getDefaultInstance();
            realm.beginTransaction();
            RealmResults<Book> results = realm.allObjects(Book.class);
            Log.i("BookListHandler", "added " + String.valueOf(results.size()) + " books to the database");
            realm.commitTransaction();
            realm.close();*/
        } catch (JSONException e){
            Log.w("BookListHandler", "something wrong with JSON data" + e.getMessage());
        }
    }
}
