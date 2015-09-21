package com.crowdshelf.app;

import java.util.List;

import com.crowdshelf.app.io.DBEvent;
import com.crowdshelf.app.io.DBEventType;
import com.crowdshelf.app.models.Book;
import com.crowdshelf.app.models.BookInfo;
import com.crowdshelf.app.models.Crowd;
import com.crowdshelf.app.models.MemberId;
import com.crowdshelf.app.models.User;
import com.crowdshelf.app.io.network.GetBookInfoAsyncTask;
import com.crowdshelf.app.io.network.NetworkController;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import io.realm.Realm;

/**
 * Created by Torstein on 02.09.2015.
 */
public class MainController {
    static Realm realm = Realm.getDefaultInstance();
    Bus bus = new Bus();

    public void test() {
        bus.register(this);
        bus.post(new DBEvent(DBEventType.BOOK, "132"));
    }

    @Subscribe
    public void answerAvailable(DBEvent event) {
        // TODO: React to the event somehow!
    }

    // TODO: Handle revisions

    //todo  get the user of this app
    private static User mainUser = new User();

    /*
    4 types of methods:
    Create: Create new object and send it to server
    Get: Get object stored locally, and if not found, retrieve it
    Retrieve: Send command to download object from server. You need to use this if you want to force downloading the object again.
    Receive: Receive the object from server
     */

    /*
    Users
     */

    public static void createUser(String username) {
        // Create a new user
        User user = new User();
        user.setUsername(username);
        NetworkController.createUser(user);
    }

    public static User getUser(String userId) {
        User user = realm.where(User.class)
                .equalTo("id", userId)
                .findFirst();
        if (user == null) {
            NetworkController.getUser(userId);
        } else {
            return user;
        }
        return null;
    }

    public static void retrieveUser(String username) {
        NetworkController.getUser(username);
    }

    public static void retrieveUsers(List<String> userIds) {
        for (String id : userIds) {
            retrieveUser(id);
        }
    }

    /*
    Crowds
     */

    public static void createCrowd(String name, String ownerId, List<MemberId> members){
        // Create new crowd
        Crowd crowd = new Crowd();
        crowd.setName(name);
        crowd.setOwner(ownerId);
        // todo handle members
        NetworkController.createCrowd(crowd);
    }

    public static Crowd getCrowd(String crowdId) {
        Crowd crowd = realm.where(Crowd.class)
                .equalTo("id", crowdId)
                .findFirst();
        if (crowd == null) {
            NetworkController.getCrowd(crowdId);
        } else {
            return crowd;
        }
        return null;
    }

    public static void retrieveCrowd(String crowdId) {
        NetworkController.getCrowd(crowdId);
    }

    public static void retrieveCrowds(List<String> crowdIds) {
        for (String id : crowdIds) {
            retrieveCrowd(id);
        }
    }

    /*
    Books
     */

    public static void createBook(String isbn, String rentedTo) {
        // This book is never stored in the books hash map. It is sent to the server,
        // then retrieved to be stored with the correct _id
        Book book = new Book();
        book.setIsbn(isbn);
        book.setRentedTo(rentedTo);
        book.setOwner(mainUser.getUsername());
        NetworkController.createBook(book);
}

    public static Book getBook(String bookId) {
        Book book = realm.where(Book.class)
                .equalTo("id", bookId)
                .findFirst();
        if (book == null) {
            NetworkController.getBook(bookId);
        } else {
            return book;
        }
        return null;
    }

    public static BookInfo getBookInfo(String isbn) {
        BookInfo bookInfo = realm.where(BookInfo.class)
                .equalTo("id", isbn)
                .findFirst();
        if (bookInfo == null) {
            GetBookInfoAsyncTask.getBookInfo(isbn);
        } else {
            return bookInfo;
        }
        return null;

    }

    public static List<Book> getBooksOwned(String userId) {
        List<Book> books = realm.where(Book.class)
            .equalTo("owner", userId)
            .findAll();
        if (books.size() == 0) {
            NetworkController.getBooksOwned(userId);
        } else {
            return books;
        }
        return null;
    }

    public static List<Book> getBooksRented(String userId) {
        List<Book> books = realm.where(Book.class)
                .equalTo("rentedTo", userId)
                .findAll();
        if (books.size() == 0) {
            NetworkController.getBooksRented(userId);
        } else {
            return books;
        }
        return null;
    }

    public static List<Book> getBooksByISBN(String isbn) {
        List<Book> books = realm.where(Book.class)
                .equalTo("isbn", isbn)
                .findAll();
        if (books.size() == 0) {
            NetworkController.getBooksByIsbn(isbn);
        } else {
            return books;
        }
        return null;
    }
}
