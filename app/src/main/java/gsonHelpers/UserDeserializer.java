package gsonHelpers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;

import MainController;
import models.Book;
import models.Crowd;
import models.User;

/**
 * Created by Torstein on 02.09.2015.
 */
public class UserDeserializer implements JsonDeserializer<User> {
    @Override
    public User deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException
    {
        /*
        { username: String, booksOwned: Array[Book], booksRented: Array[Book], crowds: Array{Crowd] }
         */
        JsonObject jsonObject = json.getAsJsonObject();
        String username = jsonObject.get("userName").getAsString();
        ArrayList<Book> booksOwned = new ArrayList<Book>();

        ArrayList<Book> booksRented = new ArrayList<Book>();

        ArrayList<Crowd> crowds = new ArrayList<Crowd>();
        //ArrayList<User> rentedTo = jsonHelper.usernamesToUsers(jsonObject.get("rentedTo").getAsJsonArray());

        //return new User(username, booksOwned, booksRented, crowds);
        return null;
    }
}