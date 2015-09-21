package com.crowdshelf.app;

import com.crowdshelf.app.io.DBEvent;
import com.crowdshelf.app.io.DBEventType;
import com.crowdshelf.app.io.network.GetBookInfoAsyncTask;
import com.crowdshelf.app.io.network.NetworkController;
import com.crowdshelf.app.models.Book;
import com.crowdshelf.app.models.BookInfo;
import com.crowdshelf.app.models.Crowd;
import com.crowdshelf.app.models.MemberId;
import com.crowdshelf.app.models.User;
import com.crowdshelf.app.ui.activities.MainTabbedActivity;
import com.squareup.otto.Bus;

import java.util.List;

import io.realm.Realm;

/**
 * Created by Torstein on 02.09.2015.
 */
public class MainController {
    static Realm realm = Realm.getDefaultInstance();
    Bus bus = new Bus();


    // TODO: Handle revisions

    //todo  get the user of this app
    private static User mainUser = new User();

    /*
    Users
     */

    public static void createUser(String username) {
        // Create a new user
        User user = new User();
        user.setUsername(username);
        NetworkController.createUser(user);
    }

    public static void getUser(String userId) {
        User user = realm.where(User.class)
                .equalTo("id", userId)
                .findFirst();
        if (user == null) {
            NetworkController.getUser(userId);
        } else {
            MainTabbedActivity.getBus().post(new DBEvent(DBEventType.USER_READY, userId));
        }
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

    public static void getCrowd(String crowdId) {
        Crowd crowd = realm.where(Crowd.class)
                .equalTo("id", crowdId)
                .findFirst();
        if (crowd == null) {
            NetworkController.getCrowd(crowdId);
        } else {
            MainTabbedActivity.getBus().post(new DBEvent(DBEventType.CROWD_READY, crowdId));
        }
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

    public static void createBook(Book book) {
        // This book is never stored in the books hash map. It is sent to the server,
        // then retrieved to be stored with the correct _id
        NetworkController.createBook(book);
}

    public static void getBook(String bookId) {
        Book book = realm.where(Book.class)
                .equalTo("id", bookId)
                .findFirst();
        if (book == null) {
            NetworkController.getBook(bookId);
        } else {
            MainTabbedActivity.getBus().post(new DBEvent(DBEventType.BOOK_READY, bookId));
        }
    }

    public static void getBookInfo(String isbn) {
        BookInfo bookInfo = realm.where(BookInfo.class)
                .equalTo("isbn", isbn)
                .findFirst();
        if (bookInfo == null) {
            GetBookInfoAsyncTask.getBookInfo(isbn);
        } else {
            MainTabbedActivity.getBus().post(new DBEvent(DBEventType.BOOKINFO_READY, isbn));
        }
    }

    public static void getBooksOwned(String userId) {
        List<Book> books = realm.where(Book.class)
            .equalTo("owner", userId)
            .findAll();
        if (books.size() == 0) {
            NetworkController.getBooksOwned(userId);
        } else {
            // MainTabbedActivity.getBus().post(new DBEvent(DBEventType.BOOK_EADY, isbn));
        }
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
