package com.crowdshelf.app.io.network.serializers;

import com.crowdshelf.app.models.User;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;


/**
 * Created by Torstein on 19.09.2015.
 */

public class UserSerializer implements JsonSerializer<User> {
    @Override
    public JsonElement serialize(User user, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        object.addProperty("_id", user.getId());
        object.addProperty("username", user.getUsername());
        object.addProperty("name", user.getName());
        object.addProperty("email", user.getEmail());
        return object;
    }
}