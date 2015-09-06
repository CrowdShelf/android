package com.crowdshelf.app.network;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import com.crowdshelf.app.models.Book;
import com.crowdshelf.app.models.Crowd;

/**
 * Created by Torstein on 01.09.2015.
 */
public class NetworkController {
    /*
    Books
     */

    // Add book to database or update existing one
    public static void createBook(Book book) {
        /*
        PUT /book
        data: book object
         */
        NetworkHelper.sendPutRequest("/book", new Gson().toJson(book, Book.class));
    }

    // Obsolete, should never be needed!
    @Deprecated
    public static void getBook(String owner, String isbn) {
        // GET /book/:isbn/:owner
        NetworkHelper.sendGetRequest("/book/" + isbn + "/" + owner);
    }

    public static void addRenter(String isbn, String owner, String renter) {
        /*
        PUT /book/:isbn/:owner/addrenter
        data: username : String # username of renter
         */
        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("username", renter);
        NetworkHelper.sendPutRequest("/book/" + isbn + "/" + owner + "/addrenter", jsonObj.getAsString());
    }

    public static void removeRenter(String isbn, String owner, String renter) {
        /*
        PUT /book/:isbn/:owner/removerenter
        data: username : String # username of renter
         */
        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("username", renter);
        NetworkHelper.sendPutRequest("/book/"+isbn+"/"+owner+"/removerenter", jsonObj.getAsString());
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
        NetworkHelper.sendPostRequest("/crowd", new Gson().toJson(crowd, Crowd.class));
    }

    public static void getCrowd(String crowdID) {
        // GET /crowd/:crowdId
        NetworkHelper.sendGetRequest("/crowd/" + crowdID);
    }

    public static void addCrowdMember(String crowdId, String username) {
        /*
        PUT /crowd/:crowdId/addmember
        data: username : String
         */
        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("username", username);
        NetworkHelper.sendPutRequest("/crowd/" + crowdId + "/addememeber", jsonObj.getAsString());
    }

    public static void removeCrowdMember(String crowdId, String username) {
        /*
        PUT /crowd/:crowdId/removemember
        data: String : username
         */
        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("username", username);
        NetworkHelper.sendPutRequest("/crowd/"+crowdId+"/removemember", jsonObj.getAsString());
    }

    /*
    Users
     */

    public static void createUser(String username) {
        // todo, also, add to api
    }

    public static void getUser(String username){
        // GET /api/user/:username
        NetworkHelper.sendGetRequest("/user/"+username);
    }
}
