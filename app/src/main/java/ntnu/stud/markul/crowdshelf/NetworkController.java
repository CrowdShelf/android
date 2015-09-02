package ntnu.stud.markul.crowdshelf;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Array;
import java.util.ArrayList;

import ntnu.stud.markul.crowdshelf.gsonHelpers.BookDeserializer;
import ntnu.stud.markul.crowdshelf.gsonHelpers.CrowdDeserializer;
import ntnu.stud.markul.crowdshelf.gsonHelpers.UserDeserializer;
import ntnu.stud.markul.crowdshelf.models.Book;
import ntnu.stud.markul.crowdshelf.models.Crowd;
import ntnu.stud.markul.crowdshelf.models.Shelf;
import ntnu.stud.markul.crowdshelf.models.User;

/**
 * Created by Torstein on 01.09.2015.
 */
public class NetworkController {
    //todo replace deserializers with serializers
    private static Gson gson = new GsonBuilder()
            .registerTypeAdapter(Book.class, new BookDeserializer())
            .registerTypeAdapter(User.class, new UserDeserializer())
            .registerTypeAdapter(Crowd.class, new CrowdDeserializer())
            .create();

    // add book to db or update existing one
    public static void addBook(Book book) {
        /* PUT /book
        data: book object
         */
        NetworkHelper.sendPutRequest("/book", new Gson().toJson(book, Book.class));
        // TODO should be changed to POST with book object as response
        // TODO handle response
    }

    public static void getBook(User owner, String isbn) {
        // GET /book/:isbn/:owner
        NetworkHelper.sendGetRequest("/book/"+isbn+"/"+owner.toString());
    }

    public static void addRenter(User renter, Book book) {
        /* PUT /book/:isbn/:owner/addrenter
        data: user object
         */
        String isbn = book.getBookInfo().getIsbn();
        String ownerS = book.getOwner().getName();
        NetworkHelper.sendPutRequest("/book/"+isbn+"/"+ownerS+"/addrenter", new Gson().toJson(renter, User.class));
    }

    public static void removeRenter(User renter, Book book) {
        /* PUT /book/:isbn/:owner/removerenter
        data: book object
         */
        String isbn = book.getBookInfo().getIsbn();
        String ownerS = book.getOwner().getName();
        NetworkHelper.sendPutRequest("/book/"+isbn+"/"+ownerS+"/removerenter", new Gson().toJson(renter, User.class));
    }

    public static void createCrowd(Crowd crowd) {
        /* POST /crowd
        data: crowd object
        response: crowd object (for correct _id)
         */
        NetworkHelper.sendPostRequest("/crowd", new Gson().toJson(crowd, Crowd.class));
    }

    public static void getCrowd(String crowdID) {
        // GET /crowd/:crowdId
        NetworkHelper.sendGetRequest("/crowd/"+crowdID);
    }

    public static void addCrowdMember(Crowd crowd, User user) {
        /* PUT /crowd/:crowdId/addmember
        data: user object
         */
        NetworkHelper.sendPutRequest("/crowd/"+crowd.get_id()+"/addememeber", new Gson().toJson(user, User.class));
    }

    public static void removeCrowdMember(Crowd crowd, User user) {
        /* PUT /crowd/:crowdId/removemember
        data: user object
         */
        NetworkHelper.sendPutRequest("/crowd/"+crowd.get_id()+"/removemember", new Gson().toJson(user, User.class));
    }

    public static void getUser(String username){
        // GET /api/user/:username
        NetworkHelper.sendGetRequest("/user/"+username);
    }
}
