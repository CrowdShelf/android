package com.crowdshelf.app.models;

import java.util.ArrayList;

import com.crowdshelf.app.MainController;

/**
 * Created by Torstein on 01.09.2015.
 */
public class User {
    private String username;
    private ArrayList<String> booksRented; // book _id
    private ArrayList<String> booksOwned; // book _id
    private ArrayList<String> crowds; // crowd _id
    //private Shelf shelf;

    public void setCrowds(ArrayList<String> crowds) {
        this.crowds = crowds;
    }

    public void setBooksRented(ArrayList<String> booksRented) {
        this.booksRented = booksRented;
    }

    public void setBooksOwned(ArrayList<String> booksOwned) {
        this.booksOwned = booksOwned;
    }

    public String getUsername() {
        return username;
    }

    public ArrayList<String> getBooksRented() {
        return booksRented;
    }

    public ArrayList<String> getBooksOwned() {
        return booksOwned;
    }

    // todo rework
    private void setShelf(ArrayList<String> books) {
        ArrayList<Book> shelfBooks = new ArrayList<Book>();
        for(String _id : books) {
            //shelfBooks.add(MainController.getBook(_id));
        }
        //this.shelf = new Shelf(shelfBooks);
    }

    public ArrayList<Crowd> getCrowds() {
        return MainController.getCrowds(crowds);
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return username;
    }

    public String toString() {
        return "username: " + username + booksOwned.toString() + booksRented.toString() + crowds.toString();
    }

    public void addRentedBook(Book book) {String _id = book.get_id();
        if (!booksRented.contains(_id)) {
            booksRented.add(book.get_id());
        }
    }

    public void addOwnedBook(Book book) {
        String _id = book.get_id();
        if (!booksOwned.contains(_id)) {
            booksOwned.add(book.get_id());
        }
    }

    // For JUnit testing
    public boolean equals(User user) {
        return this.username.equals(user.getName())
                && this.booksOwned.equals(user.getBooksOwned())
                && this.booksRented.equals(user.getBooksRented())
                && this.crowds.equals(user.getCrowds());
    }
}
