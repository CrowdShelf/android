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
    public void handleJsonResponse(String jsonString) {
        try {
            Book b = gson.fromJson(jsonString, Book.class);
            Log.d("NETDBTEST", "Book added _id " + b.getId() + " isbn " + b.getIsbn() + " owner " + b.getOwner() + " rentedTo " + b.getRentedTo());
            Realm realm = Realm.getDefaultInstance();
            realm.beginTransaction();
            realm.copyToRealmOrUpdate(b);
            realm.commitTransaction();
            realm.close();
            MainTabbedActivity.getBus().post(new DBEvent(DBEventType.BOOK_READY, b.getId()));
        } catch (JsonSyntaxException e) {
            Log.d("NETDBTEST", "CrowdHandler something wrong with JSON data");
            e.printStackTrace();
        }
    }
}
