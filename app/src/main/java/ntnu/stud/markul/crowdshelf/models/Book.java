package ntnu.stud.markul.crowdshelf.models;

import java.util.ArrayList;

import ntnu.stud.markul.crowdshelf.BookInfoGetter;
import ntnu.stud.markul.crowdshelf.MainController;
import ntnu.stud.markul.crowdshelf.NetworkController;

/**
 * Created by Torstein on 01.09.2015.
 */
public class Book{
    private String _id;
    private String isbn;
    private String owner;
    private int numberOfCopies;
    private boolean availableForRent;
    private ArrayList<String> rentedTo;

    public ArrayList<User> getRentedTo() {
        return MainController.getUsers(rentedTo);
    }

    public User getOwner() {
        return MainController.getUser(owner);
    }

    public int getAvailableForRent() {
        return (availableForRent ? 1 : 0);
    }

    public void setAvailableForRent(boolean availableForRent) {
        this.availableForRent = availableForRent;
    }

    public void rentOut(User user) {
        NetworkController.addRenter(this.isbn, owner, user.getName());
        if (!rentedTo.contains(user.getName())) {
            rentedTo.add(user.getName());
        }
    }

    public void takeInReturn(User user) {
        NetworkController.removeRenter(this.isbn, owner, user.getName());
        rentedTo.remove(user.getName());
    }

    public int getNumberOfCopies() {
        return numberOfCopies;
    }

    public void setNumberOfCopies(int numberOfCopies) {
        this.numberOfCopies = numberOfCopies;
    }

    public BookInfo getBookInfo() {
        return BookInfoGetter.getBookInfo(this.isbn);
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setRentedTo(ArrayList<String> rentedTo) {
        this.rentedTo = rentedTo;
    }
}
