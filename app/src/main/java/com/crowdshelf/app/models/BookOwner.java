package com.crowdshelf.app.models;

import java.util.List;

/**
 * Created by Torstein on 14.09.2015.
 */

// To allow shelf fragments to take a BookOwner (either a user or a crowd) as parameter and get all their books
public interface BookOwner {
    List<Book> getBooks();
    List<Book> getBooksOwned();
    List<Book> getBooksRented();
}
