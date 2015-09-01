package ntnu.stud.markul.crowdshelf.models;

import java.util.ArrayList;

/**
 * Created by Torstein on 01.09.2015.
 */
public class Shelf {
    private ArrayList<Book> books;
    private User owner;

    public Shelf(ArrayList<Book> books) {
        this.books = books;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public ArrayList<Book> getBook() {
        return books;
    }

    public void addBook(Book book) {
        if (!books.contains(books)) {
            books.add(book);
        }
    }

    public void removeBook(BookInfo bookInfo) {
        books.remove(bookInfo);
    }
}
