package com.crowdshelf.app.io.network.serializers;

import android.util.Log;

import com.crowdshelf.app.models.User;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
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
        if (user.getUsername().isEmpty()) {
            object.add("username", JsonNull.INSTANCE);
        } else {
            object.addProperty("username", user.getUsername());
        }

        if (user.getName().isEmpty()) {
            object.add("name", JsonNull.INSTANCE);
        } else {
            object.addProperty("name", user.getName());
        }

        if (user.getEmail().isEmpty()) {
            object.add("email", JsonNull.INSTANCE);
        } else {
            object.addProperty("email", user.getEmail());
        }

        Log.i("UserSerializer", "Serialized: " + object.getAsString());
        return object;
    }
}