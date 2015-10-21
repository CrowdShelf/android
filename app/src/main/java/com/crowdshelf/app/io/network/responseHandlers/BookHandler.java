package com.crowdshelf.app.io.network.responseHandlers;

import android.util.Log;

import com.crowdshelf.app.MainController;
import com.crowdshelf.app.io.DbEvent;
import com.crowdshelf.app.io.DbEventType;
import com.crowdshelf.app.models.Book;
import com.crowdshelf.app.ui.activities.MainTabbedActivity;
import com.google.gson.JsonSyntaxException;

import io.realm.Realm;

/**
 * Created by Torstein on 07.09.2015.
 */
public class BookHandler implements ResponseHandler {
    private static final String TAG = "BookHandler";
    @Override
    public void handleJsonResponse(String jsonString, DbEventType dbEventType) {
        Realm realm = Realm.getDefaultInstance();
        try {
            /*
            TODO: Only send bus event if the object is new or updated??
             */
            //Log.i(TAG, "Json-string: " + jsonString);
            Book b = gson.fromJson(jsonString, Book.class);
            Log.i(TAG, "added _id " + b.getId() + " isbn " + b.getIsbn() + " owner " + b.getOwner() + " rentedTo " + b.getRentedTo() + " availableForRent " + String.valueOf(b.getAvailableForRent()));
            MainController.getBookInfo(b.getIsbn(), DbEventType.NONE);

            realm.beginTransaction();
            realm.copyToRealmOrUpdate(b);
            realm.commitTransaction();

            if (b.getId().equals("")) {
                Log.w(TAG, "Received book does not have an id");
            }
            MainTabbedActivity.getBus().post(new DbEvent(dbEventType, b.getId()));
        } catch (JsonSyntaxException e) {
            Log.w(TAG, "something wrong with JSON data" + e.getMessage());
        } catch (RuntimeException e) {
            Log.w(TAG, e.getMessage());
        } finally {
            realm.close();
        }
    }
}
