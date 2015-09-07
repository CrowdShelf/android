package com.crowdshelf.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import com.crowdshelf.app.models.Book;
import com.crowdshelf.app.models.Crowd;
import com.crowdshelf.app.models.User;
import com.crowdshelf.app.network.NetworkController;

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

    public static ArrayList<User> getUsers(ArrayList<String> usernames) {
        ArrayList<User> usersObjs = new ArrayList<User>();
        for (String username : usernames) {
            usersObjs.add(getUser(username));
        }
        return usersObjs;
    }

    public static void retrieveUser(User user) {
        // Called ONLY when a user is sent from server
        users.put(user.getUsername(), user);
    }

    /*
    Crowds
     */

    public static void createCrowd(String name, String owner, ArrayList<String> members){
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

    public static ArrayList<Crowd> getCrowds(ArrayList<String> crowdIds) {
        ArrayList<Crowd> crowdObjs= new ArrayList<Crowd>();
        for (String crowdId : crowdIds) {
            crowdObjs.add(getCrowd(crowdId));
        }
        return crowdObjs;
    }

    public static void retrieveCrowd(Crowd crowd) {
        // Called ONLY when a crowd is sent from server
        crowds.put(crowd.getId(), crowd);
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

    public static void retrieveBook(Book book) {
        // Called ONLY when a book is sent from server
        books.put(book.getId(), book);
        coupleIsbnToId(book.getIsbn(), book.getId());
    }

    public static void coupleIsbnToId(String isbn, String _id) {
        if (!isbnToId.containsKey(isbn)) {
            isbnToId.put(isbn, new HashSet<String>());
        }
        isbnToId.get(isbn).add(_id);
    }

    public static Book getBookById(String _id) {
        NetworkController.getBookById(_id);
        return books.get(_id);
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

    public static ArrayList<Book> getBooksById (ArrayList<String> _ids) {
        ArrayList<Book> booksObj = new ArrayList<Book>();
        for (String _id : _ids) {
            booksObj.add(getBookById(_id));
        }
        return booksObj;
    }

    public static ArrayList<Book> getBooksByIsbn (String isbn) {
        // Look up all stored books with the given isbn, e.g. the same book owned by different users
        ArrayList<Book> booksObj = new ArrayList<Book>();
        for(String _id : isbnToId.get(isbn)) {
            booksObj.add(getBookById(_id));
        }
        return booksObj;
    }



}
