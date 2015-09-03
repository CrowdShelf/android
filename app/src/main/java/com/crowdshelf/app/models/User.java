package com.crowdshelf.app.models;

import java.util.ArrayList;

import com.crowdshelf.app.MainController;

/**
 * Created by Torstein on 01.09.2015.
 */
public class User {
    private String username;
    private ArrayList<Book> booksRented;
    private ArrayList<Book> booksOwned;
    private ArrayList<String> crowds; // crowd _id
    //private Shelf shelf;

    // todo rework
    private void setShelf(ArrayList<String> books) {
        ArrayList<Book> shelfBooks = new ArrayList<Book>();
        for(String _id : books) {
            shelfBooks.add(MainController.getBook(_id));
        }
        //this.shelf = new Shelf(shelfBooks);
    }

    public ArrayList<Crowd> getCrowds() {
        return MainController.getCrowds(crowds);
    }

    public String getName() {
        return username;
    }

    public String toString() {
        return "username: " + username;
    }
}
