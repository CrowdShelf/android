package com.crowdshelf.app.io.network.serializers;

import android.util.Log;

import com.crowdshelf.app.models.Crowd;
import com.crowdshelf.app.models.MemberId;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

/**
 * Created by Torstein on 23.09.2015.
 */
public class CrowdSerializer implements JsonSerializer<Crowd> {
    @Override
    public JsonElement serialize(Crowd crowd, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        object.addProperty("_id", crowd.getId());
        object.addProperty("isbn", crowd.getName());
        object.addProperty("owner", crowd.getOwner());
        JsonArray members = new JsonArray();
        for (MemberId memberId : crowd.getMembers()) {
            members.add(new JsonPrimitive(memberId.getId()));
        }
        object.add("members", members);
        Log.i("CrowdSerializer", "Serialized: " + object.getAsString());
        return object;
    }
}