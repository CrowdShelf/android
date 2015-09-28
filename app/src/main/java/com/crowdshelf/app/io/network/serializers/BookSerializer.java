package com.crowdshelf.app.io.network.serializers;

import android.util.Log;

import com.crowdshelf.app.models.Book;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
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

        if (book.getId() == null || book.getId().isEmpty()) {
            object.addProperty("_id", "-1");
        } else {
            object.addProperty("_id", book.getId());
        }

        object.addProperty("isbn", book.getIsbn());
        object.addProperty("owner", book.getOwner());

        if (book.getRentedTo() == null || book.getRentedTo().isEmpty()) {
            object.add("rentedTo", JsonNull.INSTANCE);
        } else {
            object.addProperty("rentedTo", book.getRentedTo());
        }

        object.addProperty("availableForRent", book.getAvailableForRent());
        Log.i("BookSerializer", "Serialized: " + object.toString());
        return object;
    }
}