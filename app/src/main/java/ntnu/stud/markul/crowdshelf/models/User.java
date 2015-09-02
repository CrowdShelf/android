package ntnu.stud.markul.crowdshelf.models;

import java.util.ArrayList;

import ntnu.stud.markul.crowdshelf.MainController;

/**
 * Created by Torstein on 01.09.2015.
 */
public class User {
    private String name;
    private Shelf shelf;
    private ArrayList<Book> booksOwned;
    private ArrayList<Book> booksRented;
    private ArrayList<Crowd> crowds;

    public User(String name, ArrayList<Book> booksOwned, ArrayList<Book> booksRented, ArrayList<Crowd> crowds) {
        this.name = name;
        this.booksOwned = booksOwned;
        this.booksRented = booksRented;
        this.crowds = crowds;
        // TODO
        // Create shelf from above arrays
    }

    private void setShelf(ArrayList<String> books) {
        ArrayList<Book> shelfBooks = new ArrayList<Book>();
        for(String _id : books) {
            shelfBooks.add(MainController.getBook(_id));
        }
        this.shelf = new Shelf(shelfBooks);
    }

    public Shelf getShelf() {
        return shelf;
    }

    public String getName() {
        return name;
    }
}
