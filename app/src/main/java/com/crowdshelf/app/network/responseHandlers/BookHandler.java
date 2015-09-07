package com.crowdshelf.app.network.responseHandlers;

import com.crowdshelf.app.MainController;
import com.crowdshelf.app.models.Book;

/**
 * Created by Torstein on 07.09.2015.
 */
public class BookHandler implements ResponseHandler {
    @Override
    public void handleJsonResponse(String jsonString) {
        Book book = gson.fromJson(jsonString, Book.class);
        MainController.receiveBook(book);
    }
}
