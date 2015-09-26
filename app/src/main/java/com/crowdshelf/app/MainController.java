package com.crowdshelf.app;

import android.util.Log;

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
import io.realm.RealmList;

/**
 * Created by Torstein on 02.09.2015.
 */
public class MainController {
    private static Realm realm = Realm.getDefaultInstance();
    private static Bus bus = MainTabbedActivity.getBus();

    //todo  get the user of this app
    private static User mainUser = new User();

    /*
    Users
     */

    public static void login(String username, DBEventType dbEventType) {
        // Todo implement this.. wait for server implementation.. passwords..
    }

    public static void createUser(User user, DBEventType dbEventType) {
        // Create a new user
        NetworkController.createUser(user, dbEventType);
    }

    public static void getUser(String userId, DBEventType dbEventType) {
        User user = realm.where(User.class)
                .equalTo("id", userId)
                .findFirst();
        if (user == null) {
            NetworkController.getUser(userId, dbEventType);
        } else {
            bus.post(new DBEvent(dbEventType, userId));
        }
    }

    /*
    Crowds
     */

    public static void createCrowd(String name, String ownerId, List<MemberId> members, DBEventType dbEventType){
        // Create new crowd
        Crowd crowd = new Crowd();
        crowd.setName(name);
        crowd.setOwner(ownerId);
        RealmList<MemberId> memberIds = new RealmList<MemberId>();
        memberIds.addAll(members);
        crowd.setMembers(memberIds);
        NetworkController.createCrowd(crowd, dbEventType);
    }

    public static void getCrowd(String crowdId, DBEventType dbEventType) {
        Crowd crowd = realm.where(Crowd.class)
                .equalTo("id", crowdId)
                .findFirst();
        if (crowd == null) {
            NetworkController.getCrowd(crowdId, dbEventType);
        } else {
            bus.post(new DBEvent(dbEventType, crowdId));
        }
    }

    /*
    Books
     */

    public static void createBook(Book book, DBEventType dbEventType) {
        // This book is never stored in the database. It is sent to the server,
        // then retrieved to be stored with the correct _id
        Log.i(MainTabbedActivity.TAG, "MainController - createBook");
        NetworkController.createBook(book, dbEventType);
    }

    public static void getBook(String bookId, DBEventType dbEventType) {
        Book book = realm.where(Book.class)
                .equalTo("id", bookId)
                .findFirst();
        if (book == null) {
            NetworkController.getBook(bookId, dbEventType);
        } else {
            bus.post(new DBEvent(dbEventType, bookId));
        }
    }

    public static void getBookInfo(String isbn, DBEventType dbEventType) {
        BookInfo bookInfo = realm.where(BookInfo.class)
                .equalTo("isbn", isbn)
                .findFirst();
        if (bookInfo == null) {
            GetBookInfoAsyncTask.getBookInfo(isbn, dbEventType);
        } else {
            bus.post(new DBEvent(dbEventType, isbn));
        }
    }

    /*
    Get books owned and rented by a given user
     */
    public static void getBooks(String userId, DBEventType dbEventType) {
        List<Book> books = realm.where(Book.class)
                .equalTo("owner", userId)
                .or()
                .equalTo("rentedTo", userId)
                .findAll();
        if (books.size() == 0) {
            NetworkController.getBooksOwnedAndRented(userId, dbEventType);
        } else {
            bus.post(dbEventType);
        }
    }

    /*
    Get books owned by a given user
     */
    public static void getBooksOwned(String userId, DBEventType dbEventType) {
        List<Book> books = realm.where(Book.class)
            .equalTo("owner", userId)
            .findAll();
        if (books.size() == 0) {
            NetworkController.getBooksOwned(userId, dbEventType);
        } else {
            bus.post(new DBEvent(dbEventType, userId));
        }
    }

    /*
    Get books rented by a given user
     */
    public static void getBooksRented(String userId, DBEventType dbEventType) {
        List<Book> books = realm.where(Book.class)
                .equalTo("rentedTo", userId)
                .findAll();
        if (books.size() == 0) {
            NetworkController.getBooksRented(userId, dbEventType);
        } else {
            bus.post(new DBEvent(dbEventType, userId));
        }
    }

    public static void onDestroy() {
        realm.close();
    }
}
