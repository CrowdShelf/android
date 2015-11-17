package com.crowdshelf.app.io.network;

import android.util.Log;

import com.crowdshelf.app.io.DbEventType;
import com.crowdshelf.app.io.network.responseHandlers.BookHandler;
import com.crowdshelf.app.io.network.responseHandlers.BookListHandler;
import com.crowdshelf.app.io.network.responseHandlers.CrowdHandler;
import com.crowdshelf.app.io.network.responseHandlers.CrowdListHandler;
import com.crowdshelf.app.io.network.responseHandlers.UserHandler;
import com.crowdshelf.app.io.network.responseHandlers.UserListHandler;
import com.crowdshelf.app.io.network.serializers.BookSerializer;
import com.crowdshelf.app.io.network.serializers.CrowdSerializer;
import com.crowdshelf.app.io.network.serializers.UserSerializer;
import com.crowdshelf.app.models.Book;
import com.crowdshelf.app.models.Crowd;
import com.crowdshelf.app.models.User;
import com.crowdshelf.app.ui.activities.MainTabbedActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;

/**
 * Created by Torstein on 01.09.2015.
 */
public class NetworkController {
    private static BookListHandler bookListHandler = new BookListHandler();
    private static CrowdListHandler crowdListHandler = new CrowdListHandler();
    private static UserListHandler userListHandler = new UserListHandler();
    private static CrowdHandler crowdHandler = new CrowdHandler();
    private static BookHandler bookHandler = new BookHandler();
    private static UserHandler userHandler = new UserHandler();

    private static Type bookType = new TypeToken<Book>(){}.getType();

    private static Gson gson = new GsonBuilder()
            .registerTypeAdapter(Book.class, new BookSerializer())
            .registerTypeAdapter(Crowd.class, new CrowdSerializer())
            .registerTypeAdapter(User.class, new UserSerializer())
            .serializeNulls()
            .setPrettyPrinting()
            .create();

    private static String addTokenToUrl(String url) {
        String s = "";
        if (url.contains("?")) {
            s = url + "&token=" + MainTabbedActivity.getMainUserLoginToken();
        } else {
            s = url + "?token=" + MainTabbedActivity.getMainUserLoginToken();
        }
        return s;
    }

    private static String addTokenToJsonObject(String jsonData) {
        String jsonDataWithToken = "";
        try {
            JSONObject jsonObj = new JSONObject(jsonData);
            jsonObj.put("token", MainTabbedActivity.getMainUserLoginToken());
            jsonDataWithToken = jsonObj.toString();
        } catch (Exception e) {
            Log.w("Networkcontroller", e.toString());
        }
        return jsonDataWithToken;
    }

    /*
    Books
     */

    // Add book to database
    public static void createBook(Book book, DbEventType dbEventType) {
        String jsonData = gson.toJson(book, Book.class);
        NetworkHelper.sendRequest(
                HttpRequestMethod.POST, "/books",
                addTokenToJsonObject(jsonData), bookHandler,
                dbEventType);
    }

    // Remove book to database
    public static void removeBook(String bookId, DbEventType dbEventType) {
        NetworkHelper.sendRequest(
                HttpRequestMethod.DELETE, addTokenToUrl("/books/" + bookId),
                null, null,
                dbEventType);
    }

    public static void getBook(String id, DbEventType dbEventType) {
        NetworkHelper.sendRequest(
                HttpRequestMethod.GET, addTokenToUrl("/books/" + id),
                null, bookHandler,
                dbEventType);
    }

    public static void getBooks(DbEventType dbEventType) {
        NetworkHelper.sendRequest(
                HttpRequestMethod.GET, addTokenToUrl("/books"),
                null, bookListHandler,
                dbEventType);
    }

    public static void getBooksByIsbn(String isbn, DbEventType dbEventType) {
        NetworkHelper.sendRequest(
                HttpRequestMethod.GET, addTokenToUrl("/books?isbn=" + isbn),
                null, bookListHandler,
                dbEventType);
    }

    public static void getBooksByIsbnOwner(String isbn, String userId, DbEventType dbEventType) {
        NetworkHelper.sendRequest(
                HttpRequestMethod.GET, addTokenToUrl("/books?isbn=" + isbn + "&?owner=" + userId),
                null, bookListHandler,
                dbEventType);
    }

    public static void getBooksOwned(String userId, DbEventType dbEventType) {
        NetworkHelper.sendRequest(
                HttpRequestMethod.GET, addTokenToUrl("/books?owner=" + userId),
                null, bookListHandler,
                dbEventType);
    }

    public static void getBooksRented(String userId, DbEventType dbEventType) {
        NetworkHelper.sendRequest(
                HttpRequestMethod.GET, addTokenToUrl("/books?rentedTo=" + userId),
                null, bookListHandler,
                dbEventType);
    }

    public static void getBooksOwnedAndRented(String userId, DbEventType dbEventType) {
        NetworkHelper.sendRequest(
                HttpRequestMethod.GET, addTokenToUrl("/books?owner=" + userId + "&?rentedTo=" + userId),
                null, bookListHandler,
                dbEventType);
    }

    public static void addRenter(String bookId, String userId, DbEventType dbEventType) {
        NetworkHelper.sendRequest(
                HttpRequestMethod.PUT, addTokenToUrl("/books/" + bookId +  "/renter/" + userId),
                null, bookHandler,
                dbEventType);
    }

    public static void removeRenter(String bookId, String userId, DbEventType dbEventType) {
        NetworkHelper.sendRequest(
                HttpRequestMethod.DELETE, addTokenToUrl("/books/" + bookId + "/renter/" + userId),
                null, bookHandler,
                dbEventType);
    }

    /*
    Crowds
     */

    public static void createCrowd(Crowd crowd, DbEventType dbEventType) {
        NetworkHelper.sendRequest(
                HttpRequestMethod.POST, "/crowds",
                addTokenToJsonObject(gson.toJson(crowd, Crowd.class)), crowdHandler,
                dbEventType);
    }

    public static void deleteCrowd(String crowdId, DbEventType dbEventType) {
        NetworkHelper.sendRequest(
                HttpRequestMethod.DELETE, addTokenToUrl("/crowds/" + crowdId),
                addTokenToJsonObject(""), null,
                dbEventType);
    }

    public static void getCrowd(String crowdId, DbEventType dbEventType) {
        NetworkHelper.sendRequest(
                HttpRequestMethod.GET, addTokenToUrl("/crowds/" + crowdId),
                null, crowdHandler,
                dbEventType);
    }

    public static void getCrowdBooks(String crowdId, DbEventType dbEventType) {
        NetworkHelper.sendRequest(
                HttpRequestMethod.GET, addTokenToUrl("/crowds/" + crowdId),
                null, crowdHandler,
                dbEventType);
    }

    public static void getCrowds(DbEventType dbEventType) {
        NetworkHelper.sendRequest(
                HttpRequestMethod.GET, addTokenToUrl("/crowds"),
                null, crowdListHandler,
                dbEventType);
    }

    public static void getCrowdsByMember(String userId, DbEventType dbEventType) {
        NetworkHelper.sendRequest(
                HttpRequestMethod.GET, addTokenToUrl("/crowds?member=" + userId),
                null, crowdListHandler,
                dbEventType);
    }

    public static void addCrowdMember(String crowdId, String userId, DbEventType dbEventType) {
        NetworkHelper.sendRequest(
                HttpRequestMethod.PUT, "/crowds/" + crowdId + "/members/" + userId,
                addTokenToJsonObject(""), null,
                dbEventType);
    }

    public static void removeCrowdMember(String crowdId, String userId, DbEventType dbEventType) {
        NetworkHelper.sendRequest(
                HttpRequestMethod.DELETE, "/crowds/" + crowdId + "/members/" + userId,
                addTokenToJsonObject(""), null,
                dbEventType);
    }

    /*
    Users
     */

    public static void createUser(String username, String name, String email, String password, DbEventType dbEventType) {
        JsonObject object = new JsonObject();
        object.addProperty("username", username);
        object.addProperty("name", name);
        object.addProperty("email", email);
        object.addProperty("password", password);
        String jsonData = object.toString();

        NetworkHelper.sendRequest(
                HttpRequestMethod.POST, "/users",
                jsonData, userHandler,
                dbEventType);
    }

    public static void getUser(String userId, DbEventType dbEventType){
        NetworkHelper.sendRequest(
                HttpRequestMethod.GET, addTokenToUrl("/users/" + userId),
                null, userHandler,
                dbEventType);
    }

    public static void getUserByUsername(String username, DbEventType dbEventType){
        NetworkHelper.sendRequest(
                HttpRequestMethod.GET, addTokenToUrl("/users?username=" + username),
                null, userListHandler,
                dbEventType);
    }

    public static void login(String username, String password, DbEventType dbEventType) {
        JsonObject object = new JsonObject();
        object.addProperty("username", username);
        object.addProperty("password", password);
        String jsonData = object.toString();
        NetworkHelper.sendRequest(HttpRequestMethod.POST,
                "/login/", jsonData,
                userHandler, dbEventType);
    }

    public static void forgotPassword(String username, DbEventType dbEventType) {
        JsonObject object = new JsonObject();
        object.addProperty("username", username);
        String jsonData = object.toString();
        NetworkHelper.sendRequest(HttpRequestMethod.POST,
                "/users/forgotpassword", jsonData,
                null, dbEventType);
    }

    public static void resetPassword(String username, String password, Integer key, DbEventType dbEventType) {
        JsonObject object = new JsonObject();
        object.addProperty("key", key);
        object.addProperty("password", password);
        object.addProperty("username", username);
        String jsonData = object.toString();
        NetworkHelper.sendRequest(HttpRequestMethod.POST,
                "/users/resetpassword", jsonData,
                null, dbEventType);
    }
}
