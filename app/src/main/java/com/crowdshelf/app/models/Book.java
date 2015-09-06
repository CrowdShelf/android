package com.crowdshelf.app.models;

import java.util.ArrayList;

import com.crowdshelf.app.bookInfo.BookInfoGetter;
import com.crowdshelf.app.MainController;
import com.crowdshelf.app.network.NetworkController;

/**
 * Created by Torstein on 01.09.2015.
 */
public class Book{
    private String _id;
    private String isbn;
    private String owner;
    private ArrayList<String> rentedTo;
    private int numAvailableForRent;
    private int numberOfCopies;

    public ArrayList<User> getRentedTo() {
        return MainController.getUsers(rentedTo);
    }

    public User getOwner() {
        return MainController.getUser(owner);
    }

    public String getIsbn() {
        return isbn;
    }

    public int getNumAvailableForRent() {
        return numAvailableForRent;
    }

    public void setNumAvailableForRent(int numAvailableForRent) {
        this.numAvailableForRent = numAvailableForRent;
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

    public String toString() {
        return "_id: " + String.valueOf(_id) +
                "\nisbn: " + String.valueOf(isbn) +
                "\nowner: " + String.valueOf(owner) +
                "\nnumAvailableForRent: " + String.valueOf(numAvailableForRent) +
                "\nnumberOfCopies: " + String.valueOf(numberOfCopies) +
                "\nrentedTo: " + String.valueOf(rentedTo);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        } else {
            final Book book = (Book) obj;
            return this.isbn.equals(book.isbn)
                    && this.owner.equals(book.owner)
                    && this.rentedTo.equals(book.rentedTo)
                    && this.numAvailableForRent == book.numAvailableForRent
                    && this.numberOfCopies == book.numberOfCopies;
        }
    }
}
