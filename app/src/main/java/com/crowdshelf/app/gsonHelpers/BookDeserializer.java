package com.crowdshelf.app.gsonHelpers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import com.crowdshelf.app.models.Book;

/**
 * Created by Torstein on 02.09.2015.
 */
@Deprecated
public class BookDeserializer implements JsonDeserializer<Book> {
    @Override
    public Book deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException
    {
        /*
        _id: String,
        isbn: String,
        owner: String,
        availableForRent: Integer,
        rentedTo: Array[string],
        numberOfCopies: Integer,
         */
        return null;
    }

}