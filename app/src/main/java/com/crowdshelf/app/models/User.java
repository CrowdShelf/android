package com.crowdshelf.app.models;

import java.util.ArrayList;
import java.util.List;

import com.crowdshelf.app.MainController;
import com.crowdshelf.app.network.NetworkController;

/**
 * Created by Torstein on 01.09.2015.
 */
public class User {
    private String username;
    private List<Book> booksOwned = new ArrayList<Book>();
    private List<Book> booksRented = new ArrayList<Book>();
    private List<String> crowds = new ArrayList<String>(); // crowd _id

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Book> getBooksOwned() {
        return booksOwned;
    }

    public void setBooksOwned(List<Book> booksOwned) {
        this.booksOwned = booksOwned;
    }

    public List<Book> getBooksRented() {
        return booksRented;
    }

    public void setBooksRented(List<Book> booksRented) {
        this.booksRented = booksRented;
    }

    public List<String> getCrowdsIds() {
        return crowds;
    }

    public List<Crowd> getCrowds() {
        return MainController.getCrowds(crowds);
    }

    public boolean ownsBook(String isbn) {
        for (Book b : booksOwned) {
            if (b.getIsbn().equals(isbn)) {
                return true;
            }
        }
        return false;
    }

    public boolean rentsBook(String isbn) {
        for (Book b : booksRented) {
            if (b.getIsbn().equals(isbn)) {
                return true;
            }
        }
        return false;
    }


    public void setCrowds(List<String> crowds) {
        this.crowds = crowds;
    }

    public void addRentedBook(Book book) {
        if (!booksRented.contains(book)) {
            booksRented.add(book);
        }
    }

    public void addOwnedBook(Book book) {
        if (!booksOwned.contains(book)) {
            booksOwned.add(book);
        }
    }

    public boolean isMemberOf(String crowdId) {
        return crowds.contains(crowdId);
    }

    public List<Book> getBooksRentedToOthers() {
        // As a USER I want a list of all BOOKS I'm lending to others
        List<Book> rented = new ArrayList<Book>();
        for (Book b : booksOwned) {
            if(b.getRentedTo() != null && b.getRentedTo().size() > 0) {
                rented.add(b);
            }
        }
        return rented;
    }

    public String toString() {
        return "username: " + String.valueOf(username) + "\n" +
                String.valueOf(booksOwned) + "\n" +
                String.valueOf(booksRented) + "\n" +
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
