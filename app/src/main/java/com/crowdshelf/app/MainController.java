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
    private static Realm realm;

    //todo  get the user of this app
    private static User mainUser = new User();

    public static void onCreate() {
        realm = Realm.getDefaultInstance();
    }

    /*
    Users
     */

    public static void login(String username, DBEventType dbEventType) {
        NetworkController.login(username, dbEventType);

    }

    public static void createUser(User user, DBEventType dbEventType) {
        // This user is never stored in the database. It is sent to the server,
        // then retrieved to be stored with the correct _id
        NetworkController.createUser(user, dbEventType);
    }

    public static void getUser(String userId, DBEventType dbEventType) {
        User user = realm.where(User.class)
                .equalTo("id", userId)
                .findFirst();
        if (user == null) {
            NetworkController.getUser(userId, dbEventType);
        } else {
            MainTabbedActivity.getBus().post(new DBEvent(dbEventType, userId));
        }
    }

    /*
    Crowds
     */

    public static void createCrowd(String name, String ownerId, List<MemberId> members, DBEventType dbEventType){
        // This crowd is never stored in the database. It is sent to the server,
        // then retrieved to be stored with the correct _id
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
            MainTabbedActivity.getBus().post(new DBEvent(dbEventType, crowdId));
        }
    }

    /*
    Books
     */

    public static void createBook(Book book, DBEventType dbEventType) {
        // This book is never stored in the database. It is sent to the server,
        // then retrieved to be stored with the correct _id
        NetworkController.createBook(book, dbEventType);
    }

    public static void removeBook(String bookId, DBEventType dbEventType) {
        // This book is never stored in the database. It is sent to the server,
        // then retrieved to be stored with the correct _id
        NetworkController.removeBook(bookId, dbEventType);
    }

    public static void getBook(String bookId, DBEventType dbEventType) {
        Book book = realm.where(Book.class)
                .equalTo("id", bookId)
                .findFirst();
        if (book == null) {
            NetworkController.getBook(bookId, dbEventType);
        } else {
            MainTabbedActivity.getBus().post(new DBEvent(dbEventType, bookId));
        }
    }

    public static void getBookInfo(String isbn, DBEventType dbEventType) {
        BookInfo bookInfo = realm.where(BookInfo.class)
                .equalTo("isbn", isbn)
                .findFirst();
        if (bookInfo == null) {
            GetBookInfoAsyncTask.getBookInfo(isbn, dbEventType);
        } else {
            MainTabbedActivity.getBus().post(new DBEvent(dbEventType, isbn));
        }
    }

    /*
    Get books owned and rented by a given user
     */
    public static void getBooks(String userId, DBEventType dbEventType) {
        NetworkController.getBooksOwnedAndRented(userId, dbEventType);
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
            MainTabbedActivity.getBus().post(new DBEvent(dbEventType, userId));
        }
    }

    /*
    Get books rented to a given user
     */
    public static void getBooksRented(String userId, DBEventType dbEventType) {
        List<Book> books = realm.where(Book.class)
                .equalTo("rentedTo", userId)
                .findAll();
        if (books.size() == 0) {
            NetworkController.getBooksRented(userId, dbEventType);
        } else {
            MainTabbedActivity.getBus().post(new DBEvent(dbEventType, userId));
        }
    }

    // todo Find out what books to get. Books owned AND rented by the users in the crowd?
    public static void getCrowdBooks(String crowdId, DBEventType dbEventType) {
    }

    public static void onDestroy() {
        realm.close();
    }
}
