package com.crowdshelf.app.io.network;

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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

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


    /*
    Books
     */

    // Add book to database
    public static void createBook(Book book, DbEventType dbEventType) {
        String jsonData = gson.toJson(book, Book.class);
        NetworkHelper.sendRequest(
                HttpRequestMethod.POST, "/books",
                jsonData, bookHandler,
                dbEventType);
    }

    // Remove book to database
    public static void removeBook(String bookId, DbEventType dbEventType) {
        NetworkHelper.sendRequest(
                HttpRequestMethod.DELETE, "/books/" + bookId,
                null, null,
                dbEventType);
    }

    public static void getBook(String id, DbEventType dbEventType) {
        NetworkHelper.sendRequest(
                HttpRequestMethod.GET, "/books/" + id,
                null, bookHandler,
                dbEventType);
    }

    public static void getBooks(DbEventType dbEventType) {
        NetworkHelper.sendRequest(
                HttpRequestMethod.GET, "/books",
                null, bookListHandler,
                dbEventType);
    }

    public static void getBooksByIsbn(String isbn, DbEventType dbEventType) {
        NetworkHelper.sendRequest(
                HttpRequestMethod.GET, "/books?isbn=" + isbn,
                null, bookListHandler,
                dbEventType);
    }

    public static void getBooksByIsbnOwner(String isbn, String userId, DbEventType dbEventType) {
        NetworkHelper.sendRequest(
                HttpRequestMethod.GET, "/books?isbn=" + isbn + "&?owner=" + userId,
                null, bookListHandler,
                dbEventType);
    }

    public static void getBooksOwned(String userId, DbEventType dbEventType) {
        NetworkHelper.sendRequest(
                HttpRequestMethod.GET, "/books?owner=" + userId,
                null, bookListHandler,
                dbEventType);
    }

    public static void getBooksRented(String userId, DbEventType dbEventType) {
        NetworkHelper.sendRequest(
                HttpRequestMethod.GET, "/books?rentedTo=" + userId,
                null, bookListHandler,
                dbEventType);
    }

    public static void getBooksOwnedAndRented(String userId, DbEventType dbEventType) {
        NetworkHelper.sendRequest(
                HttpRequestMethod.GET, "/books?owner=" + userId + "&?rentedTo=" + userId,
                null, bookListHandler,
                dbEventType);
    }

    public static void addRenter(String bookId, String userId, DbEventType dbEventType) {
        NetworkHelper.sendRequest(
                HttpRequestMethod.PUT, "/books/" + bookId +  "/renter/" + userId,
                null, bookHandler,
                dbEventType);
    }

    public static void removeRenter(String bookId, String userId, DbEventType dbEventType) {
        NetworkHelper.sendRequest(
                HttpRequestMethod.DELETE, "/books/" + bookId + "/renter/" + userId,
                null, bookHandler,
                dbEventType);
    }

    /*
    Crowds
     */

    public static void createCrowd(Crowd crowd, DbEventType dbEventType) {
        NetworkHelper.sendRequest(
                HttpRequestMethod.POST, "/crowds",
                gson.toJson(crowd, Crowd.class), crowdHandler,
                dbEventType);
    }

    public static void deleteCrowd(String crowdId, DbEventType dbEventType) {
        NetworkHelper.sendRequest(
                HttpRequestMethod.DELETE, "/crowds/" + crowdId,
                null, null,
                dbEventType);
    }

    public static void getCrowd(String crowdId, DbEventType dbEventType) {
        NetworkHelper.sendRequest(
                HttpRequestMethod.GET, "/crowds/" + crowdId,
                null, crowdHandler,
                dbEventType);
    }

    public static void getCrowdBooks(String crowdId, DbEventType dbEventType) {
        NetworkHelper.sendRequest(
                HttpRequestMethod.GET, "/crowds/" + crowdId,
                null, crowdHandler,
                dbEventType);
    }

    public static void getCrowds(DbEventType dbEventType) {
        NetworkHelper.sendRequest(
                HttpRequestMethod.GET, "/crowds",
                null, crowdListHandler,
                dbEventType);
    }

    public static void getCrowdsByMember(String userId, DbEventType dbEventType) {
        NetworkHelper.sendRequest(
                HttpRequestMethod.GET, "/crowds?member=" + userId,
                null, crowdListHandler,
                dbEventType);
    }

    public static void addCrowdMember(String crowdId, String userId, DbEventType dbEventType) {
        NetworkHelper.sendRequest(
                HttpRequestMethod.PUT, "/crowds/" + crowdId + "/members/" + userId,
                null, null,
                dbEventType);
    }

    public static void removeCrowdMember(String crowdId, String userId, DbEventType dbEventType) {
        NetworkHelper.sendRequest(
                HttpRequestMethod.DELETE, "/crowds/" + crowdId + "/members/" + userId,
                null, null,
                dbEventType);
    }

    /*
    Users
     */

    public static void createUser(User user, DbEventType dbEventType) {
        NetworkHelper.sendRequest(
                HttpRequestMethod.POST, "/users",
                gson.toJson(user, User.class), userHandler,
                dbEventType);
    }

    public static void getUser(String userId, DbEventType dbEventType){
        NetworkHelper.sendRequest(
                HttpRequestMethod.GET, "/users/" + userId,
                null, userHandler,
                dbEventType);
    }

    public static void getUserByUsername(String username, DbEventType dbEventType){
        NetworkHelper.sendRequest(
                HttpRequestMethod.GET, "/users?username=" + username,
                null, userListHandler,
                dbEventType);
    }

    public static void login(String username, DbEventType dbEventType) {
        JsonObject object = new JsonObject();
        object.addProperty("username", username);
        String jsonData = object.toString();
        NetworkHelper.sendRequest(HttpRequestMethod.POST,
                "/login/", jsonData,
                userHandler, dbEventType);
    }
}
