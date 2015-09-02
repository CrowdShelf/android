package ntnu.stud.markul.crowdshelf;

import com.google.gson.Gson;

import java.lang.reflect.Array;
import java.util.ArrayList;

import ntnu.stud.markul.crowdshelf.models.Book;
import ntnu.stud.markul.crowdshelf.models.Crowd;
import ntnu.stud.markul.crowdshelf.models.Shelf;
import ntnu.stud.markul.crowdshelf.models.User;

/**
 * Created by Torstein on 01.09.2015.
 */
public class NetworkController {
    // add book to db or update existing one
    public static void addBook(Book book) {
        /* PUT /api/book
        _id: String # ps! should be -1 for new book
        isbn: String
        owner: String,
        availableForRent: Integer,
        rentedTo: Array[string],
        numberOfCopies: Integer,
         */
        NetworkHelper.sendPutRequest("/book", new Gson().toJson(book, Book.class));
    }

    public static Book getBook(User owner, String isbn) {
        // GET /book/:isbn/:owner
        NetworkHelper.sendGetRequest("/book/"+isbn+"/"+owner.toString());
    }

    public static void addRenter(User renter, Book book) {
        // PUT /book/:isbn/:owner/addrenter
        String isbn = book.getBookInfo().getIsbn();
        String ownerS = book.getOwner().getName();
        NetworkHelper.sendPutRequest("/book/"+isbn+"/"+ownerS+"/addrenter", new Gson().toJson(renter, User.class));
    }

    public static void removeRenter(User renter, Book book) {
        // PUT /book/:isbn/:owner/removerenter
        String isbn = book.getBookInfo().getIsbn();
        String ownerS = book.getOwner().getName();
        NetworkHelper.sendPutRequest("/book/"+isbn+"/"+ownerS+"/removerenter", new Gson().toJson(renter, User.class));
    }

    public static void createCrowd(Crowd crowd) {
        /* POST /api/crowd
    _id: String # PS! should be -1 for new crowd
    name: String,
    creator: String,
    members: Array[String]
         */
        NetworkHelper.sendPostRequest("/crowd", new Gson().toJson(crowd, Crowd.class));
    }

    public static Crowd getCrowd(String crowdID) {
        // GET /api/crowd/:crowdId
        NetworkHelper.sendGetRequest("/crowd/"+crowdID);
    }

    public static void addCrowdMember(Crowd crowd, User user) {
        // PUT /api/crowd/:crowdId/addmember
        NetworkHelper.sendPutRequest("/crowd/"+crowd.getId()+"/addememeber", new Gson().toJson(user, User.class));
    }

    public static void removeCrowdMember(Crowd crowd, User user) {
        // PUT /api/crowd/:crowdId/removemember
        NetworkHelper.sendPutRequest("/crowd/"+crowd.getId()+"/removemember", new Gson().toJson(user, User.class));
    }

    public static User getUser(String username){
        // GET /api/user/:username
        NetworkHelper.sendGetRequest("/user/"+username);
    }
}
