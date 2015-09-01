package ntnu.stud.markul.crowdshelf;

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
    // TODO find a way to get User instance of the person using this app
    private static User me = new User("me");

    public static void addBook(Book book) {
        /* PUT /api/book
        isbn: String
        owner: String,
        availableForRent: Integer,
        rentedTo: Array[string],
        numberOfCopies: Integer,
         */
        String isbn = book.getBookInfo().getIsbn();
        String owner = me.getName();
        int availableForRent = (book.isAvailableForRent()) ? 1 : 0;
        ArrayList<String> rentedTo = new ArrayList<String>();
        for (User u: book.getRentedTo()) {
            rentedTo.add(u.getName());
        }
        String jsonData = null;
        NetworkHelper.sendPutRequest("api/book", jsonData);
    }

    public static Book getBook(User owner, String isbn) {
        // GET /book/:isbn/:owner
        NetworkHelper.sendGetRequest("api/book/"+isbn+"/"+owner.toString());
        return new Book();
    }

    public static void addRenter(User renter, Book book) {
        // PUT /book/:isbn/:owner/addrenter/:renter
        String isbn = book.getBookInfo().getIsbn();
        String ownerS = book.getOwner().getName();
        String renterS = renter.getName();
        NetworkHelper.sendPutRequest("api/book/"+isbn+"/"+ownerS+"/addrenter/"+renterS, null);
    }

    public static void removeRenter(User renter, Book book) {
        // PUT /book/:isbn/:owner/removerenter/:renter
        String isbn = book.getBookInfo().getIsbn();
        String ownerS = book.getOwner().getName();
        String renterS = renter.getName();
        NetworkHelper.sendPutRequest("api/book/"+isbn+"/"+ownerS+"/removerenter/"+renterS, null);
    }

    public static void createCrowd(String name, ArrayList<User> members) {
        /* POST /api/crowd
    name: String,
    creator: String,
    members: Array[String]
         */
        NetworkHelper.sendPostRequest("api/book/"+isbn+"/"+ownerS+"/removerenter/"+renterS, null);
    }

    public static Crowd getCrowd(String crowdID) {
        return null;
    }

    public static void addCrowdMember(Crowd crowd, User user) {
        // PUT /api/crowd/:crowdId
    }

    public static void removeCrowdMember(Crowd crowd, User user) {

    }
}
