package com.crowdshelf.app.network.gsonHelpers;

import com.crowdshelf.app.MainController;
import com.crowdshelf.app.models.Book;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by Torstein on 07.09.2015.
 */
public class BookHandler implements JsonHandler {
    @Override
    public void handleJsonResponse(String jsonString) {
        Book book = gson.fromJson(jsonString, Book.class);
        MainController.receiveBook(book);
    }
}
