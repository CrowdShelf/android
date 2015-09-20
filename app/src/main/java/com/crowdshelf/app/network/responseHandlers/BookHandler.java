package com.crowdshelf.app.network.responseHandlers;

import com.crowdshelf.app.MainController;
import com.crowdshelf.app.models.Book;
import com.google.gson.JsonSyntaxException;

import io.realm.Realm;

/**
 * Created by Torstein on 07.09.2015.
 */
public class BookHandler implements ResponseHandler {
    @Override
    public void handleJsonResponse(String jsonString) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.createOrUpdateObjectFromJson(Book.class, jsonString);
        realm.commitTransaction();
        realm.close();
        /*
        try {
            Book book = gson.fromJson(jsonString, Book.class);
            MainController.receiveBook(book);
        } catch (JsonSyntaxException e) {
            System.out.print("Received book was not in expected format\n");
            e.printStackTrace();
        }
        */
    }
}
