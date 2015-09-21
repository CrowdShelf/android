package com.crowdshelf.app.network.responseHandlers;

import android.util.Log;

import com.crowdshelf.app.MainController;
import com.crowdshelf.app.bookInfo.BookInfoGetter;
import com.crowdshelf.app.models.Book;
import com.crowdshelf.app.models.Crowd;
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
            Log.d("NETDBTEST", "Book added _id " + b.getId() + " isbn " + b.getIsbn() + " owner " + b.getOwner() + " rentedDo " + b.getRentedTo());
            Realm realm = Realm.getDefaultInstance();
            realm.beginTransaction();
            realm.copyToRealmOrUpdate(b);
            realm.commitTransaction();
            realm.close();
        } catch (JsonSyntaxException e) {
            Log.d("NETDBTEST", "CrowdHandler something wrong with JSON data");
            e.printStackTrace();
        }
    }
}
