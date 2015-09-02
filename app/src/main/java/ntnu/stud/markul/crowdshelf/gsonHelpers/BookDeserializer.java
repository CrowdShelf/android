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
import ntnu.stud.markul.crowdshelf.models.User;

/**
 * Created by Torstein on 02.09.2015.
 */
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

    }
}