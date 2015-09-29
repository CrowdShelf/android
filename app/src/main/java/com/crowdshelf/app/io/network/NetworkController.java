package com.crowdshelf.app.io.network;

import com.crowdshelf.app.io.DBEventType;
import com.crowdshelf.app.io.network.responseHandlers.BookHandler;
import com.crowdshelf.app.io.network.responseHandlers.BookListHandler;
import com.crowdshelf.app.io.network.responseHandlers.CrowdHandler;
import com.crowdshelf.app.io.network.responseHandlers.CrowdListHandler;
import com.crowdshelf.app.io.network.responseHandlers.UserHandler;
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
    private static CrowdHandler crowdHandler = new CrowdHandler();
    private static BookHandler bookHandler = new BookHandler();
    private static UserHandler userHandler = new UserHandler();

    private static Type bookType = new TypeToken<Book>() {
    }.getType();

    private static Gson gson = new GsonBuilder()
            .registerTypeAdapter(Book.class, new BookSerializer())
            .registerTypeAdapter(Crowd.class, new CrowdSerializer())
            .registerTypeAdapter(User.class, new UserSerializer())
            .serializeNulls()
            .setPrettyPrinting()
            .serializeNulls()
            .create();


    /*
    Books
     */

    // Add book to database or update existing one
    public static void createBook(Book book, DBEventType dbEventType) {
        String jsonData = gson.toJson(book, Book.class);

        NetworkHelper.sendRequest(HTTPRequestMethod.POST,
                "/books", jsonData,
                bookHandler, dbEventType);
    }

    public static void getBook(String id, DBEventType dbEventType) {
        NetworkHelper.sendRequest(HTTPRequestMethod.GET,
                "/books/" + id, null,
                bookHandler, dbEventType);
    }

    public static void getBooks(DBEventType dbEventType) {
        NetworkHelper.sendRequest(HTTPRequestMethod.GET,
                "/books", null,
                bookListHandler, dbEventType);
    }

    public static void getBooksByIsbn(String isbn, DBEventType dbEventType) {
        NetworkHelper.sendRequest(HTTPRequestMethod.GET,
                "/books?isbn=" + isbn, null,
                bookListHandler, dbEventType);
    }

    public static void getBooksByIsbnOwner(String isbn, String userId, DBEventType dbEventType) {
        NetworkHelper.sendRequest(HTTPRequestMethod.GET,
                "/books?isbn=" + isbn + "&?owner=" + userId, null,
                bookListHandler, dbEventType);
    }

    public static void getBooksOwned(String userId, DBEventType dbEventType) {
        NetworkHelper.sendRequest(HTTPRequestMethod.GET,
                "/books?owner=" + userId, null,
                bookListHandler, dbEventType);
    }

    public static void getBooksRented(String userId, DBEventType dbEventType) {
        NetworkHelper.sendRequest(HTTPRequestMethod.GET,
                "/books?rentedTo=" + userId, null,
                bookListHandler, dbEventType);
    }

    public static void getBooksOwnedAndRented(String userId, DBEventType dbEventType) {
        NetworkHelper.sendRequest(HTTPRequestMethod.GET,
                "/books?owner=" + userId + "&?rentedTo=" + userId, null,
                bookListHandler, dbEventType);
    }

    public static void addRenter(String bookId, String userId, DBEventType dbEventType) {
        NetworkHelper.sendRequest(HTTPRequestMethod.PUT,
                "/books/" + bookId +  "/renter/" + userId, null,
                null, dbEventType);
    }

    public static void removeRenter(String bookId, String userId, DBEventType dbEventType) {
        NetworkHelper.sendRequest(HTTPRequestMethod.DELETE,
                "/books/" + bookId + "/renter/" + userId, null,
                null, dbEventType);
    }

    /*
    Crowds
     */

    public static void createCrowd(Crowd crowd, DBEventType dbEventType) {
        NetworkHelper.sendRequest(HTTPRequestMethod.POST,
                "/crowds", gson.toJson(crowd, Crowd.class),
                crowdHandler, dbEventType);
    }

    public static void getCrowd(String crowdId, DBEventType dbEventType) {
        NetworkHelper.sendRequest(HTTPRequestMethod.GET,
                "/crowds/" + crowdId, null,
                crowdHandler, dbEventType);
    }

    public static void getCrowds(DBEventType dbEventType) {
        NetworkHelper.sendRequest(HTTPRequestMethod.GET,
                "/crowds", null,
                crowdListHandler, dbEventType);
    }

    public static void addCrowdMember(String crowdId, String userId, DBEventType dbEventType) {
        NetworkHelper.sendRequest(HTTPRequestMethod.PUT,
                "/crowds/" + crowdId + "/members/" + userId, null,
                null, dbEventType);
    }

    public static void removeCrowdMember(String crowdId, String userId, DBEventType dbEventType) {
        NetworkHelper.sendRequest(HTTPRequestMethod.DELETE,
                "/crowds/" + crowdId + "/members/" + userId, null,
                null, dbEventType);
    }

    /*
    Users
     */

    public static void createUser(User user, DBEventType dbEventType) {
        NetworkHelper.sendRequest(HTTPRequestMethod.POST,
                "/users", gson.toJson(user, User.class),
                userHandler, dbEventType);
    }

    public static void getUser(String userId, DBEventType dbEventType){
        NetworkHelper.sendRequest(HTTPRequestMethod.GET,
                "/users/" + userId, null,
                userHandler, dbEventType);
    }

    public static void login(String username, DBEventType dbEventType) {
        JsonObject object = new JsonObject();
        object.addProperty("username", username);
        String jsonData = object.toString();
        NetworkHelper.sendRequest(HTTPRequestMethod.POST,
                "/login/", jsonData,
                userHandler, dbEventType);
    }
}
