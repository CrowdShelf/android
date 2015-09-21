package com.crowdshelf.app.io.network.responseHandlers;

import com.crowdshelf.app.models.Book;
import com.crowdshelf.app.models.Crowd;
import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;


/**
 * Created by Torstein on 02.09.2015.
 */

@Deprecated
public class CrowdDeserializer implements JsonDeserializer<Crowd> {
    @Override
    public Crowd deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException
    {
        /*
        {
        username: String,
        booksOwned: Array[Book],
        booksRented: Array[Book],
        crowds: Array[String] // crowd _id
        }
         */
        JsonObject jsonObject = json.getAsJsonObject();
        String _id = jsonObject.get("_id").getAsString();
        String name = jsonObject.get("name").getAsString();
        String owner = jsonObject.get("owner").getAsString();
        Type arrayListStringType = new TypeToken<ArrayList<String>>(){}.getType();
        Type arrayListBookType = new TypeToken<ArrayList<Book>>(){}.getType();

        /*
        User objects are retrieved containing full book objects, but are stored only containing a
        reference to the book. We therefore store the book _id's in the user object and the books
        themselves in the MainController.
        */
        ArrayList<String> crowds = new Gson().fromJson(jsonObject.getAsJsonArray("crowds"), arrayListStringType);
        ArrayList<Book> booksOwnedObj = new Gson().fromJson(jsonObject.getAsJsonArray("booksOwned"), arrayListBookType);
        ArrayList<Book> booksRentedObj = new Gson().fromJson(jsonObject.getAsJsonArray("booksRented"), arrayListBookType);
        ArrayList<String> booksOwned = new ArrayList<String>();
        ArrayList<String> booksRented = new ArrayList<String>();

        for (Book b : booksOwnedObj) {
            //MainController.retrieveBook(b);
            booksOwned.add(b.getId());
        }
        for (Book b : booksRentedObj) {
            //MainController.retrieveBook(b);
            booksRented.add(b.getId());
        }

        Crowd crowd = new Crowd();
        //user.setUsername(username);
        //user.setCrowds(crowds);
        //user.setBooksOwned(booksOwned);
        //user.setBooksRented(booksRented);

        //user.toString();

        return crowd;
    }
}