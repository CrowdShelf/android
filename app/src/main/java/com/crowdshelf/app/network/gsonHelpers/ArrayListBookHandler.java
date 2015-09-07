package com.crowdshelf.app.network.gsonHelpers;

import com.crowdshelf.app.MainController;
import com.crowdshelf.app.models.Book;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Torstein on 07.09.2015.
 */
public class ArrayListBookHandler implements JsonHandler {
    @Override
    public void handleJsonResponse(String jsonString) {
        JsonParser jsonParser = new JsonParser();
        JsonElement jsonElement = jsonParser.parse(jsonString);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonArray ja = jsonObject.getAsJsonArray("books");
        BookHandler bookHandler = new BookHandler();
        for (int i = 0; i < ja.size(); i++) {
            JsonElement e = ja.get(i);
            bookHandler.handleJsonResponse(e.toString());
        }
    }
}
