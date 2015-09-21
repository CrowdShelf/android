package com.crowdshelf.app.network.responseHandlers;

import com.crowdshelf.app.models.MemberId;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Type;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by Torstein on 07.09.2015.
 */
public interface ResponseHandler {
    Type memberIdType = new TypeToken<RealmList<MemberId>>(){}.getType();
    Gson gson = new GsonBuilder()
            .setExclusionStrategies(new ExclusionStrategy() {
                @Override
                public boolean shouldSkipField(FieldAttributes f) {
                    return f.getDeclaringClass().equals(RealmObject.class);
                }

                @Override
                public boolean shouldSkipClass(Class<?> clazz) {
                    return false;
                }
            })
            .registerTypeAdapter(memberIdType, new TypeAdapter<RealmList<MemberId>>() {

                @Override
                public void write(JsonWriter out, RealmList<MemberId> value) throws IOException {
                    // Ignore
                }

                @Override
                public RealmList<MemberId> read(JsonReader in) throws IOException {
                    RealmList<MemberId> list = new RealmList<MemberId>();
                    in.beginArray();
                    while (in.hasNext()) {
                        MemberId memberId = new MemberId();
                        memberId.setId(in.nextString());
                        list.add(memberId);
                    }
                    in.endArray();
                    return list;
                }
            })
            .setPrettyPrinting()
            .create();

    void handleJsonResponse(String jsonString);
}
