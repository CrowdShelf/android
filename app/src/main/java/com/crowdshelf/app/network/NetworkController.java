package com.crowdshelf.app.network;

import com.crowdshelf.app.models.User;
import com.crowdshelf.app.network.responseHandlers.BookListHandler;
import com.crowdshelf.app.network.responseHandlers.CrowdListHandler;
import com.crowdshelf.app.network.responseHandlers.BookHandler;
import com.crowdshelf.app.network.responseHandlers.CrowdHandler;
import com.crowdshelf.app.network.responseHandlers.UserHandler;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import com.crowdshelf.app.models.Book;
import com.crowdshelf.app.models.Crowd;

/**
 * Created by Torstein on 01.09.2015.
 */
public class NetworkController {
    private static BookListHandler bookListHandler = new BookListHandler();
    private static CrowdListHandler crowdListHandler = new CrowdListHandler();
    private static CrowdHandler crowdHandler = new CrowdHandler();
    private static BookHandler bookHandler = new BookHandler();
    private static UserHandler userHandler = new UserHandler();

    /*
    Books
     */

    // Add book to database or update existing one
    public static void createBook(Book book) {
        NetworkHelper.sendRequest(HTTPRequestMethod.PUT,
                "/books", new Gson().toJson(book, Book.class), bookHandler);
    }

    public static void getBooks() {
        NetworkHelper.sendRequest(HTTPRequestMethod.GET,
                "/books", null, bookListHandler);
    }

    public static void getBooksByIsbn(String isbn) {
        NetworkHelper.sendRequest(HTTPRequestMethod.GET,
                "/books?isbn=" + isbn, null, bookListHandler);
    }

    public static void getBooksByIsbnOwner(String isbn, String userId) {
        NetworkHelper.sendRequest(HTTPRequestMethod.GET,
                "/books?isbn=" + isbn + "&?owner=" + userId, null, bookListHandler);
    }

    public static void getBooksOwned(String userId) {
        NetworkHelper.sendRequest(HTTPRequestMethod.GET,
                "/books?owner=" + userId, null, bookListHandler);
    }

    public static void getBooksRented(String userId) {
        NetworkHelper.sendRequest(HTTPRequestMethod.GET,
                "/books?rentedTo=" + userId, null, bookListHandler);
    }

    public static void addRenter(String bookId, String userId) {
        NetworkHelper.sendRequest(HTTPRequestMethod.PUT,
                "/books/" + bookId +  "/renter/" + userId, null, null);
    }

    public static void removeRenter(String bookId, String userId) {
        NetworkHelper.sendRequest(HTTPRequestMethod.DELETE,
                "/books/" + bookId + "/renter/" + userId, null, null);
    }

    /*
    Crowds
     */

    public static void createCrowd(Crowd crowd) {
        NetworkHelper.sendRequest(HTTPRequestMethod.POST,
                "/crowds", new Gson().toJson(crowd, Crowd.class), crowdHandler);
    }

    public static void getCrowds() {
        NetworkHelper.sendRequest(HTTPRequestMethod.GET,
                "/crowds", null, crowdListHandler);
    }

    public static void getCrowd(String crowdId) {
        NetworkHelper.sendRequest(HTTPRequestMethod.GET,
                "/crowds/" + crowdId, null, crowdHandler);
    }

    public static void addCrowdMember(String crowdId, String userId) {
        NetworkHelper.sendRequest(HTTPRequestMethod.PUT,
                "/crowds/" + crowdId + "/members/" + userId, null, null);
    }

    public static void removeCrowdMember(String crowdId, String userId) {
        NetworkHelper.sendRequest(HTTPRequestMethod.DELETE,
                "/crowds/" + crowdId + "/members/" + userId, null, null);
    }

    /*
    Users
     */

    public static void createUser(User user) {
        NetworkHelper.sendRequest(HTTPRequestMethod.PUT,
                "/users", new Gson().toJson(user, User.class), userHandler);
    }

    public static void getUser(String userId){
        NetworkHelper.sendRequest(HTTPRequestMethod.GET,
                "/users/" + userId, null, userHandler);
    }
}
