package com.crowdshelf.app.gsonHelpers;

import com.crowdshelf.app.models.Book;
import com.crowdshelf.app.models.User;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import com.crowdshelf.app.MainController;


/**
 * Created by Torstein on 02.09.2015.
 */
public class JsonHelper {

    @Deprecated
    public static ArrayList<User> usernamesToUsers(JsonArray usernames) {
        ArrayList<User> users = new ArrayList<User>();
        for (int i = 0, size = usernames.size(); i < size; i++)
        {
            String username = usernames.get(i).getAsString();
            users.add(MainController.getUser(username));
        }
        return users;
    }

    @Deprecated
    public static void jsonEToCrowd(JsonElement json) {
        JsonObject jsonObject = json.getAsJsonObject();
        String _id = jsonObject.get("_id").getAsString();
        String name = jsonObject.get("name").getAsString();
        String username = jsonObject.get("creator").getAsString();
        User owner = MainController.getUser(username);
        ArrayList<User> members = JsonHelper.usernamesToUsers(jsonObject.get("members").getAsJsonArray());

        //return new Crowd(_id, name, owner, members);
    }

    public static ArrayList<String> jsonBookArrayToBookIdArrayList(JsonArray jArray) {
        ArrayList<String> bookIds = new ArrayList<String>();
        for (int i = 0; i < jArray.size(); i++) {
            Book book = new Gson().fromJson(jArray.get(i), Book.class);
            MainController.retrieveBook(book);
            bookIds.add(book.get_id());
        }
        return bookIds;
    }

    public static ArrayList<String> jsonArrayToStringArrayList(JsonArray jArray){
        ArrayList<String> returnList = new ArrayList<String>();
        for (int i = 0; i < jArray.size(); i++) {
            String val = jArray.get(i).getAsString();
            returnList.add(val);
        }
        return returnList;
    }
}
