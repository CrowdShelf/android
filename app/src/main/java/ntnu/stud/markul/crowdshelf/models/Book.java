package ntnu.stud.markul.crowdshelf.models;

import java.util.ArrayList;

import ntnu.stud.markul.crowdshelf.BookInfoGetter;
import ntnu.stud.markul.crowdshelf.NetworkController;

/**
 * Created by Torstein on 01.09.2015.
 */
public class Book{
    private String _id;
    private BookInfo bookInfo;
    private User owner;
    private ArrayList<User> rentedTo;
    private int numberOfCopies;
    private boolean availableForRent = true;

    public Book(String _id, String isbn, User owner, ArrayList<User> rentedTo, int numberOfCopies) {
        this._id = _id;
        this.owner = owner;
        this.rentedTo = rentedTo;
        this.numberOfCopies = numberOfCopies;
        bookInfo = BookInfoGetter.getBookInfo(isbn);
        owner.getShelf().addBook(this);
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

    public String get_id() {
        return _id;
    }
}
