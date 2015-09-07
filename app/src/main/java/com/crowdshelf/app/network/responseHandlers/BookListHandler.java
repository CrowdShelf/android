package com.crowdshelf.app.network.responseHandlers;

import com.crowdshelf.app.MainController;
import com.crowdshelf.app.models.Book;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by Torstein on 07.09.2015.
 */
public class BookListHandler implements ResponseHandler {
    private static Type bookListType = new TypeToken<List<Book>>(){}.getType();

    @Override
    public void handleJsonResponse(String jsonString) {
        JsonParser jsonParser = new JsonParser();
        JsonElement jsonElement = jsonParser.parse(jsonString);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonArray jsonArray = jsonObject.getAsJsonArray("books");

        // Method 1
        BookHandler bookHandler = new BookHandler();
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonElement e = jsonArray.get(i);
            bookHandler.handleJsonResponse(e.toString());
        }

        //Method 2
        List<Book> books = gson.fromJson(jsonArray, bookListType);
        MainController.receiveBooks(books);
    }
}
