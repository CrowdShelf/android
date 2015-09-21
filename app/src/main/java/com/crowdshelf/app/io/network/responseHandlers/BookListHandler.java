package com.crowdshelf.app.io.network.responseHandlers;

import android.util.Log;

import com.crowdshelf.app.MainController;
import com.crowdshelf.app.models.Book;
import com.crowdshelf.app.ui.activities.MainTabbedActivity;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

import io.realm.Realm;

/**
 * Created by Torstein on 07.09.2015.
 */
public class BookListHandler implements ResponseHandler {
    //private static Type bookListType = new TypeToken<List<Book>>(){}.getType();

    @Override
    public void handleJsonResponse(String jsonString) {
        JsonParser jsonParser = new JsonParser();
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObject.getJSONArray("books");

            Realm realm = Realm.getDefaultInstance();
            realm.beginTransaction();
            realm.createAllFromJson(Book.class, jsonArray);
            realm.commitTransaction();
            realm.close();
            // MainTabbedActivity.getBus().post(new DBEvent(DBEventType.BOOK_READY, isbn));
        } catch (JSONException e){
            Log.d("NETDBTEST", "BookListHandler something wrong with JSON data");
            e.printStackTrace();
        }

        /*
        try {
            List<Book> books = gson.fromJson(jsonArray, bookListType);
            MainController.receiveBooks(books);
        } catch (JsonSyntaxException e) {
            System.out.print("Received books was not in expected format\n");
            e.printStackTrace();
        }
        */
    }
}
