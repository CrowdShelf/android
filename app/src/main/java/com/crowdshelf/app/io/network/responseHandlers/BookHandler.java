package com.crowdshelf.app.io.network.responseHandlers;

import android.util.Log;

import com.crowdshelf.app.io.DBEvent;
import com.crowdshelf.app.io.DBEventType;
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
    public void handleJsonResponse(String jsonString, DBEventType dbEventType) {
        try {
            Log.i(TAG, "Json-string: " + jsonString);
            Book b = gson.fromJson(jsonString, Book.class);
            Log.i(TAG, "added _id " + b.getId() + " isbn " + b.getIsbn() + " owner " + b.getOwner() + " rentedTo " + b.getRentedTo() + " availableForRent " + String.valueOf(b.getAvailableForRent()));
            Realm realm = Realm.getDefaultInstance();
            realm.beginTransaction();
            realm.copyToRealmOrUpdate(b);
            realm.commitTransaction();
            realm.close();
            if (b.getId().equals("")) {
                Log.w(TAG, "Received book does not have an id");
            }
            MainTabbedActivity.getBus().post(new DBEvent(dbEventType, b.getId()));
        } catch (JsonSyntaxException e) {
            Log.w(TAG, "something wrong with JSON data" + e.getMessage());
        } catch (RuntimeException e) {
            Log.w(TAG, e.getMessage());
        }
    }
}
