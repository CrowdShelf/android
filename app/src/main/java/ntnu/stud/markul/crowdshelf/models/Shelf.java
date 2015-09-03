package ntnu.stud.markul.crowdshelf.models;

import java.util.ArrayList;

/**
 * Created by Torstein on 01.09.2015.
 */
public class Shelf {

    // todo this whole class needs to be reworked and retougth

    private ArrayList<Book> books;
    private User owner;

    public Shelf(ArrayList<Book> books) {
        this.books = books;
    }

    public ArrayList<Book> getBooks() {
        return books;
    }

    public void addBook(Book book) {
        if (!books.contains(books)) {
            books.add(book);
        }
    }

    public void removeBook(Book book) {
        books.remove(book);
    }
}
