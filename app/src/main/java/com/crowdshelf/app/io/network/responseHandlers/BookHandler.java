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
    @Override
    public void handleJsonResponse(String jsonString, DBEventType dbEventType) {
        try {
            Log.i("BookHandler", "Json-string: " + jsonString);
            Book b = gson.fromJson(jsonString, Book.class);
            Log.i("BookHandler", "added _id " + b.getId() + " isbn " + b.getIsbn() + " owner " + b.getOwner() + " rentedTo " + b.getRentedTo() + " availableForRent" + String.valueOf(b.getAvailableForRent()));
            Realm realm = Realm.getDefaultInstance();
            realm.beginTransaction();
            realm.copyToRealmOrUpdate(b);
            realm.commitTransaction();
            realm.close();
            if (b.getId().equals("")) {
                Log.i("BookHandler", "Received book does not have an id");
            }
            MainTabbedActivity.getBus().post(new DBEvent(dbEventType, b.getId()));
        } catch (JsonSyntaxException e) {
            Log.w("BookHandler", "something wrong with JSON data");
            Log.w("BookHandler", e.getMessage());
        } catch (RuntimeException e) {
            Log.w("BookHandler", e.getMessage());
        }
    }
}
