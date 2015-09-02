package ntnu.stud.markul.crowdshelf.models;

import java.util.ArrayList;

import ntnu.stud.markul.crowdshelf.BookInfoGetter;
import ntnu.stud.markul.crowdshelf.NetworkController;

/**
 * Created by Torstein on 01.09.2015.
 */
public class Book{
    private String id;
    private BookInfo bookInfo;
    private User owner;
    private ArrayList<User> rentedTo;
    private int numberOfCopies;
    private boolean availableForRent = true;

    public Book(String id, String isbn, User owner, ArrayList<User> rentedTo, int numberOfCopies) {
        this.id = id;
        this.owner = owner;
        this.rentedTo = rentedTo;
        this.numberOfCopies = numberOfCopies;
        bookInfo = BookInfoGetter.getBookInfo(isbn);
        owner.getShelf().addBook(this);
    }

    public Book(String isbn, User owner, ArrayList<User> rentedTo, int numberOfCopies) {
        /*
        When creating a _NEW_ book, id should be set to -1. The book should
        then be sent to the server, in order to add it there, and then retrieved from the server again
        in order to get the real id
         */
        this.id = "-1";
        this.owner = owner;
        this.rentedTo = rentedTo;
        this.numberOfCopies = numberOfCopies;
        bookInfo = BookInfoGetter.getBookInfo(isbn);
        owner.getShelf().addBook(this);
    }

    public void updateId() {
        this.id = NetworkController.getBook(owner, bookInfo.getIsbn()).getId();
    }

    public ArrayList<User> getRentedTo() {
        return rentedTo;
    }

    public User getOwner() {
        return owner;
    }

    public int getAvailableForRent() {
        return (availableForRent ? 1 : 0);
    }

    public void setAvailableForRent(boolean availableForRent) {
        this.availableForRent = availableForRent;
    }

    public void rentOut(User user) {
        NetworkController.addRenter(user, this);
        if (!rentedTo.contains(user)) {
            rentedTo.add(user);
        }
    }

    public void takeInReturn(User user) {
        NetworkController.removeRenter(user, this);
        rentedTo.remove(user);
    }

    public int getNumberOfCopies() {
        return numberOfCopies;
    }

    public void setNumberOfCopies(int numberOfCopies) {
        this.numberOfCopies = numberOfCopies;
    }

    public BookInfo getBookInfo() {
        return bookInfo;
    }

    public boolean equals(Book book)
    {
        if (bookInfo.getIsbn() == book.getBookInfo().getIsbn())
        {
            return true;
        } else {
            return false;
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
