package com.crowdshelf.app.network;

import com.crowdshelf.app.network.gsonHelpers.ArrayListBookHandler;
import com.crowdshelf.app.network.gsonHelpers.ArrayListCrowdHandler;
import com.crowdshelf.app.network.gsonHelpers.BookHandler;
import com.crowdshelf.app.network.gsonHelpers.CrowdHandler;
import com.crowdshelf.app.network.gsonHelpers.UserHandler;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import com.crowdshelf.app.models.Book;
import com.crowdshelf.app.models.Crowd;

/**
 * Created by Torstein on 01.09.2015.
 */
public class NetworkController {
    private static ArrayListBookHandler arrayListBookHandler = new ArrayListBookHandler();
    private static ArrayListCrowdHandler arrayListCrowdHandler = new ArrayListCrowdHandler();
    private static CrowdHandler crowdHandler = new CrowdHandler();
    private static BookHandler bookHandler = new BookHandler();
    private static UserHandler userHandler = new UserHandler();
    /*
    Books
     */

    // Add book to database or update existing one
    public static void createBook(Book book) {
        /*
        PUT /book
        data: book object
        response: book object
         */
        NetworkHelper.sendPutRequest("/book", new Gson().toJson(book, Book.class), bookHandler);
    }

    public static void getBookByIsbnOwner(String isbn, String owner) {
        /*
        GET /book/:isbn/:owner
        respone: book object
         */
        NetworkHelper.sendGetRequest("/book/" + isbn + "/" + owner, bookHandler);
    }

    public static void addRenter(String isbn, String owner, String renter) {
        /*
        PUT /book/:isbn/:owner/addrenter
        data: username : String # username of renter
        response: none
         */
        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("username", renter);
        NetworkHelper.sendPutRequest("/book/" + isbn + "/" + owner + "/addrenter", jsonObj.getAsString(), null);
    }

    public static void removeRenter(String isbn, String owner, String renter) {
        /*
        PUT /book/:isbn/:owner/removerenter
        data: username : String # username of renter
        response: none
         */
        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("username", renter);
        NetworkHelper.sendPutRequest("/book/" + isbn + "/" + owner + "/removerenter", jsonObj.getAsString(), null);
    }

    /*
    Crowds
     */

    public static void createCrowd(Crowd crowd) {
        /*
        POST /crowd
        data: crowd object
        response: crowd object (with correct _id)
         */
        NetworkHelper.sendPostRequest("/crowd", new Gson().toJson(crowd, Crowd.class), crowdHandler);
    }

    public static void getCrowd(String crowdID) {
        /*
        GET /crowd/:crowdId
        response: crowd object
        */
        NetworkHelper.sendGetRequest("/crowd/" + crowdID, crowdHandler);
    }

    public static void getCrowds() {
        /*
         GET /crowd
        response: arraylistcrowd
        */
        NetworkHelper.sendGetRequest("/crowd", arrayListCrowdHandler);
    }

    public static void addCrowdMember(String crowdId, String username) {
        /*
        PUT /crowd/:crowdId/addmember
        data: username : String
        response: none
         */
        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("username", username);
        NetworkHelper.sendPutRequest("/crowd/" + crowdId + "/addememeber", jsonObj.getAsString(), null);
    }

    public static void removeCrowdMember(String crowdId, String username) {
        /*
        PUT /crowd/:crowdId/removemember
        data: String : username
        response: none
         */
        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("username", username);
        NetworkHelper.sendPutRequest("/crowd/"+crowdId+"/removemember", jsonObj.getAsString(), null);
    }

    /*
    Users
     */

    public static void createUser(String username) {
        // todo, also, add to api
    }

    public static void getUser(String username){
        /*
         GET /api/user/:username
         response: user object
          */
        NetworkHelper.sendGetRequest("/user/"+username, userHandler);
    }
}
