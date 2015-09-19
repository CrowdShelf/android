package com.crowdshelf.app.models;

import java.util.ArrayList;
import java.util.List;

import com.crowdshelf.app.MainController;
import com.crowdshelf.app.bookInfo.BookInfo;
import com.crowdshelf.app.bookInfo.BookInfoGetter;
import com.crowdshelf.app.network.NetworkController;

/**
 * Created by Torstein on 01.09.2015.
 */
public class Book{
    private String _id;
    private String isbn;
    private String owner;
    private List<String> rentedTo = new ArrayList<String>();
    private int numAvailableForRent;
    private int numberOfCopies;

    public String getId() {
        return _id;
    }

    public void setId(String _id) {
        this._id = _id;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public User getOwner() {
        return MainController.getUser(owner);
    }

    public String getOwnerName() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public List<String> getRentedToName() {
        return rentedTo;
    }

    public List<User> getRentedTo() {
        return MainController.getUsers(rentedTo);
    }

    public void setRentedTo(ArrayList<String> rentedTo) {
        this.rentedTo = rentedTo;
    }

    public int getNumAvailableForRent() {
        return numAvailableForRent;
    }

    public void setNumAvailableForRent(int numAvailableForRent) {
        this.numAvailableForRent = numAvailableForRent;
    }

    public int getNumberOfCopies() {
        return numberOfCopies;
    }

    public void setNumberOfCopies(int numberOfCopies) {
        this.numberOfCopies = numberOfCopies;
    }

    public void addRenter(String username) {
        NetworkController.addRenter(this.isbn, owner, username);
        if (!rentedTo.contains(username)) {
            rentedTo.add(username);
        }
    }

    public void removeRenter(String username) {
        NetworkController.removeRenter(this.isbn, owner, username);
        rentedTo.remove(username);
    }

    public BookInfo getBookInfo() {

        return BookInfoGetter.getBookInfo(this.isbn);
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
