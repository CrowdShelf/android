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
    private static HashMap<String, HashSet<String>> isbnToId = new HashMap<String, HashSet<String>>(); // KEY = isbn, Value = Set[Book _id]
    private static HashMap<String, User> users = new HashMap<String, User>(); // KEY = username

    //todo  get the user of this app
    private static User mainUser = new User();

    /*
    Users
     */

    public static void createUser(String username) {
        // Create a new user
        User user = new User();
        user.setUsername(username);
        NetworkController.createUser(username);
    }

    public static User getUser(String username) {
        NetworkController.getUser(username);
        return users.get(username);
    }

    public static List<User> getUsers(List<String> usernames) {
        ArrayList<User> usersObjs = new ArrayList<User>();
        for (String username : usernames) {
            usersObjs.add(getUser(username));
        }
        return usersObjs;
    }

    public static void receiveUser(User user) {
        // Called ONLY when a user is sent from server
        users.put(user.getUsername(), user);
        receiveBooks(user.getBooksOwned());
        receiveBooks(user.getBooksRented());
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
        crowd.setMembers(members);
        NetworkController.createCrowd(crowd);
    }

    public static Crowd getCrowd(String _id) {
        NetworkController.getCrowd(_id);
        return crowds.get(_id);
    }

    public static List<Crowd> getCrowds(List<String> crowdIds) {
        ArrayList<Crowd> crowdObjs= new ArrayList<Crowd>();
        for (String crowdId : crowdIds) {
            crowdObjs.add(getCrowd(crowdId));
        }
        return crowdObjs;
    }

    public static void receiveCrowd(Crowd crowd) {
        // Called ONLY when a crowd is sent from server
        crowds.put(crowd.getId(), crowd);
        receiveUsers(crowd.getMembers());
    }

    @Deprecated
    public static void receiveCrowds(List<Crowd> crowds) {
        for (Crowd c : crowds) {
            receiveCrowd(c);
        }
    }

    /*
    Books
     */

    public static void createBook(String isbn, int numberOfCopies, int numAvailableForRent) {
        // This book is never stored in the books hashmap. It is sent to the server,
        // then retrieved to get the correct _id
        Book book = new Book();
        book.setId("-1");
        book.setIsbn(isbn);
        book.setOwner(mainUser.getUsername());
        book.setNumberOfCopies(numberOfCopies);
        book.setNumAvailableForRent(numAvailableForRent);
        NetworkController.createBook(book);
    }

    public static void receiveBook(Book book) {
        // Called ONLY when a book is sent from server
        books.put(book.getId(), book);
        coupleIsbnToId(book.getIsbn(), book.getId());
    }

    @Deprecated
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

    public static Book getBookByIsbnOwner (String isbn, String owner) {
        //todo this is ugly
        NetworkController.getBookByIsbnOwner(isbn, owner);
        for (String _id : isbnToId.get(isbn)) {
            Book b = books.get(_id);
            if (b.getOwner().equals(owner)) {
                return b;
            }
        }
        return null;
    }

    public static List<Book> getBooksByIsbn (String isbn) {
        // Look up all stored books with the given isbn, e.g. the same book owned by different users

        // Todo needs rework. Should it return _all_ books with the given ISBN, or only
        // those that belong to a crowd you are a member of?

        return null;
    }

}
