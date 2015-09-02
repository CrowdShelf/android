package ntnu.stud.markul.crowdshelf;

import java.util.HashMap;

import ntnu.stud.markul.crowdshelf.models.Book;
import ntnu.stud.markul.crowdshelf.models.Crowd;
import ntnu.stud.markul.crowdshelf.models.User;

/**
 * Created by Torstein on 02.09.2015.
 */
public class MainController {
    private static HashMap<String, Crowd> crowds = new HashMap<String, Crowd>(); // KEY = crowd _id
    private static HashMap<String, Book> books = new HashMap<String, Book>(); // KEY = book _id NOT ISBN
    private static HashMap<String, User> users = new HashMap<String, User>(); // KEY = username

    //todo  get the user of this app
    private static User mainUser = new User("me");

    // Create a _NEW_ user
    public static void createUser() {
        //todo
    }

    public static User getUser(String username) {
        if (!users.containsKey(username)) {
            NetworkController.getUser(username);
        }
        return users.get(username);
    }

    // Called ONLY when a user is sent from server
    public static void retrieveUser(User user) {
        users.put(user.getName(), user);
    }

    public static void createCrowd(){
        //todo
    }

    public static Crowd getCrowd(String _id) {
        if (!crowds.containsKey(_id)) {
            NetworkController.getCrowd(_id);
        }
        return crowds.get(_id);
    }

    // Called ONLY when a crowd is sent from server
    public static void retrieveCrowd(Crowd crowd) {
        crowds.put(crowd.get_id(), crowd);
    }

    public static void createBook(String isbn, int numberOfCopies) {
        // This book is never stored in the books hashmap. It is sent to the server
        Book book = new Book("-1",isbn, mainUser, null, numberOfCopies);
        NetworkController.addBook(book);
    }

    public static Book getBook(String _id) {
        if (!books.containsKey(_id)) {
            NetworkController.getBook(_id);
        }
        return books.get(_id);
    }

    // Called ONLY when a book is sent from server
    public static void retrieveBook(Book book) {
        books.put(book.get_id(), book);
    }

}
