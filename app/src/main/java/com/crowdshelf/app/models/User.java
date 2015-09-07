package com.crowdshelf.app.models;

import java.util.ArrayList;

import com.crowdshelf.app.MainController;
import com.crowdshelf.app.network.NetworkController;

/**
 * Created by Torstein on 01.09.2015.
 */
public class User {
    private String username;
    private ArrayList<String> booksRented; // book _id
    private ArrayList<String> booksOwned; // book _id
    private ArrayList<String> crowds; // crowd _id

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ArrayList<String> getBooksOwnedIds() {
        return booksOwned;
    }

    public ArrayList<Book> getBooksOwned() {
        return MainController.getBooksById(booksOwned);
    }

    public void setBooksOwned(ArrayList<String> booksOwned) {
        this.booksOwned = booksOwned;
    }

    public ArrayList<String> getBooksRentedIds() {
        return booksRented;
    }

    public ArrayList<Book> getBooksRented() {
        return MainController.getBooksById(booksRented);
    }

    public void setBooksRented(ArrayList<String> booksRented) {
        this.booksRented = booksRented;
    }

    public ArrayList<String> getCrowdsIds() {
        return crowds;
    }

    public ArrayList<Crowd> getCrowds() {
        return MainController.getCrowds(crowds);
    }

    public void setCrowds(ArrayList<String> crowds) {
        this.crowds = crowds;
    }

    public void addRentedBook(Book book) {String _id = book.getId();
        if (!booksRented.contains(_id)) {
            booksRented.add(book.getId());
        }
    }

    public void addOwnedBook(Book book) {
        String _id = book.getId();
        if (!booksOwned.contains(_id)) {
            booksOwned.add(book.getId());
        }
    }

    public String toString() {
        return "username: " + String.valueOf(username) +
                String.valueOf(booksOwned) +
                String.valueOf(booksRented) +
                String.valueOf(crowds);
    }

    // For JUnit testing
    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        } else {
            final User user = (User) obj;
            return this.username.equals(user.getUsername())
                    && this.booksOwned.equals(user.getBooksOwned())
                    && this.booksRented.equals(user.getBooksRented())
                    && this.crowds.equals(user.getCrowds());
        }
    }
}
