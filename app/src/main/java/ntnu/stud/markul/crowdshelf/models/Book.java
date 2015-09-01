package ntnu.stud.markul.crowdshelf.models;

import android.media.Image;

/**
 * Created by Torstein on 01.09.2015.
 */
public class Book {
    private String isbn, title, subtitle, author, publisher, pubDate;
    private Image artwork; // Maybe we need multiple sizes

    public Book(String isbn) {
        this.isbn = isbn;
        retrieveInfo();
    }

    private void retrieveInfo() {
        //TODO get info and store
    }
}
