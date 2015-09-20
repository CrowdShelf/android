package com.crowdshelf.app;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import com.crowdshelf.app.models.Book;
import com.crowdshelf.app.models.Crowd;
import com.crowdshelf.app.models.User;
import com.crowdshelf.app.network.NetworkController;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Created by Torstein on 02.09.2015.
 */
public class MainController {
    private static HashMap<String, Crowd> crowds = new HashMap<String, Crowd>(); // KEY = crowd _id
    private static HashMap<String, Book> books = new HashMap<String, Book>(); // KEY = book _id (NOT isbn!)
    private static HashMap<String, HashSet<String>> isbnToId = new HashMap<String, HashSet<String>>(); // KEY = isbn, Value = Hashset[Book _id]
    private static HashMap<String, User> users = new HashMap<String, User>(); // KEY = username

    //todo  get the user of this app
    private static User mainUser = new User();

    /*
    4 types of methods:
    Create: Create new object and send it to server
    Get: Get object stored locally, and if not found, retrieve it
    Retrieve: Send command to download object from server. You need to use this if you want to force downloading the object again.
    Receive: Receive the object from server
     */

    /*
    Users
     */

    public static void createUser(String username) {
        // Create a new user
        User user = new User();
        user.setUsername(username);
        NetworkController.createUser(user);
    }

    public static User getUser(String username) {
        if (!users.containsKey(username)) {
            retrieveUser(username);
        }
        return users.get(username);
    }

    public static List<User> getUsers(List<String> usernames) {
        List<User> usersObjs = new ArrayList<User>();
        for (String username : usernames) {
            usersObjs.add(getUser(username));
        }
        return usersObjs;
    }

    public static void retrieveUser(String username) {
        NetworkController.getUser(username);
    }

    public static void retrieveUsers(List<String> usernames) {
        for (String username : usernames) {
            retrieveUser(username);
        }
    }

    public static void receiveUser(User user) {
        // Called ONLY when a user is sent from server
        users.put(user.getUsername(), user);
    }

    public static void receiveUsers(List<User> users) {
        // Called ONLY when a user is sent from server
        for (User u : users) {
            receiveUser(u);
        }
    }

    /*
    Crowds
     */

    public static void createCrowd(String name, String owner, List<User> members){
        // Create new crowd
        Crowd crowd = new Crowd();
        crowd.setName(name);
        crowd.setOwner(owner);
        NetworkController.createCrowd(crowd);
    }

    public static Crowd getCrowd(String crowdId) {
        if (!crowds.containsKey(crowdId)) {
            retrieveCrowd(crowdId);
        }
        return crowds.get(crowdId);
    }

    public static List<Crowd> getCrowds(List<String> crowdIds) {
        ArrayList<Crowd> crowdObjs= new ArrayList<Crowd>();
        for (String crowdId : crowdIds) {
            crowdObjs.add(getCrowd(crowdId));
        }
        return crowdObjs;
    }

    public static void retrieveCrowd(String crowdId) {
        NetworkController.getCrowd(crowdId);
    }

    public static void retrieveCrowds(List<String> crowdIds) {
        for (String id : crowdIds) {
            retrieveCrowd(id);
        }
    }

    public static void receiveCrowd(Crowd crowd) {
        // Called ONLY when a crowd is sent from server
        crowds.put(crowd.getId(), crowd);
    }

    public static void receiveCrowds(List<Crowd> crowds) {
        for (Crowd c : crowds) {
            receiveCrowd(c);
        }
    }

    /*
    Books
     */

    public static void createBook(String isbn, int numberOfCopies, int numAvailableForRent) {
        // This book is never stored in the books hash map. It is sent to the server,
        // then retrieved to be stored with the correct _id
        Book book = new Book();
        book.setIsbn(isbn);
        book.setOwner(mainUser.getUsername());
        NetworkController.createBook(book);
}

    public static void receiveBook(Book book) {
        // Called ONLY when a book is sent from server
        if (book != null) {
            // book.getOwner().addOwnedBook(book);
            /*
            for (User u : book.getRentedTo()) {
                u.addRentedBook(book);
            }
            */
            books.put(book.getId(), book);
            coupleIsbnToId(book.getIsbn(), book.getId());
        }
    }

    public static void receiveBooks(List<Book> books) {
        for (Book b : books) {
            receiveBook(b);
        }
    }

    public static void coupleIsbnToId(String isbn, String _id) {
        if (!isbnToId.containsKey(isbn)) {
            isbnToId.put(isbn, new HashSet<String>());
        }
        isbnToId.get(isbn).add(_id);
    }


    public static List<Book> getBooksByIsbnOwnedByAll (String isbn) {
        // Look up all stored books with the given isbn, e.g. the same book owned by different users
        // Todo needs rework.

        NetworkController.getBooksByIsbn(isbn);

        return null;
    }
}
