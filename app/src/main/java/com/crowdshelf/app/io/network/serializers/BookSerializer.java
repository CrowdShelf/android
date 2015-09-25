package com.crowdshelf.app.io.network.serializers;

import com.crowdshelf.app.models.Book;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

/**
 * Created by Torstein on 23.09.2015.
 */
public class BookSerializer implements JsonSerializer<Book> {
    @Override
    public JsonElement serialize(Book book, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        object.addProperty("_id", book.getId());
        object.addProperty("isbn", book.getIsbn());
        object.addProperty("owner", book.getOwner());
        object.addProperty("rentedTo", book.getRentedTo());
//        object.addProperty("availableForRent", book.getAvailableForRent());
        return object;
    }
}