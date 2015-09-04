package com.crowdshelf.app.gsonHelpers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;

import com.crowdshelf.app.models.User;


/**
 * Created by Torstein on 02.09.2015.
 */
public class UserDeserializer implements JsonDeserializer<User> {
    @Override
    public User deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException
    {
        /*
        { username: String, booksOwned: Array[Book], booksRented: Array[Book], crowds: Array[String# _id]  }
         */
        JsonObject jsonObject = json.getAsJsonObject();
        String username = jsonObject.get("username").getAsString();
        ArrayList<String> crowds = JsonHelper.jsonArrayToStringArrayList(jsonObject.getAsJsonArray("crowds"));
        ArrayList<String> booksOwned = JsonHelper.jsonBookArrayToBookIdArrayList(jsonObject.getAsJsonArray("booksOwned"));
        ArrayList<String> booksRented = JsonHelper.jsonBookArrayToBookIdArrayList(jsonObject.getAsJsonArray("booksOwned"));

        User user = new User();
        user.setUsername(username);
        user.setCrowds(crowds);
        user.setBooksOwned(booksOwned);
        user.setBooksRented(booksRented);

        user.toString();

        return user;
    }
}