package ntnu.stud.markul.crowdshelf.gsonHelpers;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;

import ntnu.stud.markul.crowdshelf.MainController;
import ntnu.stud.markul.crowdshelf.models.Book;
import ntnu.stud.markul.crowdshelf.models.Crowd;
import ntnu.stud.markul.crowdshelf.models.User;

/**
 * Created by Torstein on 02.09.2015.
 */
public class CrowdDeserializer implements JsonDeserializer<Crowd> {
    @Override
    public Crowd deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException
    {
        /*

       _id: String,
        name: String,
        creator: String,
        members: Array[String]
         */
        return null
    }
}