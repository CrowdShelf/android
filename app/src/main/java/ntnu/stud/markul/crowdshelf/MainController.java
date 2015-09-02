package ntnu.stud.markul.crowdshelf;

import java.util.HashMap;

import ntnu.stud.markul.crowdshelf.models.Book;
import ntnu.stud.markul.crowdshelf.models.Crowd;
import ntnu.stud.markul.crowdshelf.models.User;

/**
 * Created by Torstein on 02.09.2015.
 */
public class MainController {
    private static HashMap<String, Crowd> crowds = new HashMap<String, Crowd>(); // KEY = crowd id
    private static HashMap<String, Book> books = new HashMap<String, Book>(); // KEY = book id NOT ISBN
    private static HashMap<String, User> users = new HashMap<String, User>(); // KEY = username

    public MainController() {

    }

    public static User getUser(String username) {
        if (!users.containsKey(username)) {
            NetworkController.getUser(username);
        }
        return users.get(username);
    }

    // Called when a user is sent from server
    public static void retrieveUser(User user) {
        users.put(user.getName(), user);
    }

    public static void retrieveCrowd(Crowd crowd) {
        crowds.put(crowd.getId(), crowd);
    }

    public static void retrieveBook(Book book) {
        books.put(book.getId(), book);
    }

    public Crowd getCrowd(String id) {
        if (!crowds.containsKey(id)) {
            // Create new crowd
        }
        return crowds.get(id);
    }
}
