package com.crowdshelf.app;

import android.util.Log;

import com.crowdshelf.app.io.DbEvent;
import com.crowdshelf.app.io.DbEventType;
import com.crowdshelf.app.io.network.GetBookInfoAsyncTask;
import com.crowdshelf.app.io.network.NetworkController;
import com.crowdshelf.app.models.Book;
import com.crowdshelf.app.models.BookInfo;
import com.crowdshelf.app.models.Crowd;
import com.crowdshelf.app.models.MemberId;
import com.crowdshelf.app.models.User;
import com.crowdshelf.app.ui.activities.MainTabbedActivity;
import com.squareup.otto.Subscribe;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;

/**
 * Created by Torstein on 02.09.2015.
 */
public class MainController {
    private static Realm realm;
    private static final String TAG = "MainController";

    /*
    Must be called on application startup
     */
    public void onCreate() {
        MainTabbedActivity.getBus().register(this);
        realm = Realm.getDefaultInstance();
    }

    @Subscribe
    public void dbEventListener(DbEvent dbEvent) {
        switch (dbEvent.getDbEventType()) {
            case CROWD_READY_GET_BOOKS:
                Crowd crowd = realm.where(Crowd.class)
                        .equalTo("id", dbEvent.getDbObjectId())
                        .findFirst();
                for (MemberId memberId : crowd.getMembers()) {
                    if (memberId.getId().equals(MainTabbedActivity.getMainUserId())) {
                        for (MemberId memberId2 : crowd.getMembers()) {
                            NetworkController.getBooksOwnedAndRented(memberId2.getId(), DbEventType.ON_START_USER_CROWD_BOOKS_READY);
                        }
                        break;
                    }
                }
                break;
            case ON_START_USER_CROWDS_READY:
                /*
                Get all crowds which the main user is a member of,
                then retrieve all the books of the members of these crowds
                (which will include the books of the main user)
                 */
                //MainTabbedActivity.getBus().post(new DbEvent(DbEventType.USER_CROWDS_CHANGED, "all"));
                List<Crowd> crowds = realm.where(Crowd.class)
                        .findAll();
                for (Crowd c : crowds) {
                    for (MemberId memberId : c.getMembers()) {
                        NetworkController.getBooksOwnedAndRented(memberId.getId(), DbEventType.ON_START_USER_CROWD_BOOKS_READY);
                    }
                }
                break;
        }
    }

    /*
    Users
     */

    public static void login(String username, DbEventType dbEventType) {
        NetworkController.login(username, dbEventType);

    }

    public static void createUser(User user, DbEventType dbEventType) {
        // This user is never stored in the database. It is sent to the server,
        // then retrieved to be stored with the correct _id
        NetworkController.createUser(user, dbEventType);
    }

    public static void getUser(String userId, DbEventType dbEventType) {
        NetworkController.getUser(userId, dbEventType);
    }

    /*
    Crowds
     */

    public static void createCrowd(String name, String ownerId, List<String> members, DbEventType dbEventType){
        // This crowd is never stored in the database. It is sent to the server,
        // then retrieved to be stored with the correct _id
        Crowd crowd = new Crowd();
        crowd.setName(name);
        crowd.setOwner(ownerId);
        RealmList<MemberId> memberIds = new RealmList<MemberId>();
        for (String id: members) {
            MemberId memberId = new MemberId();
            memberId.setId(id);
            memberIds.add(memberId);
        }
        crowd.setMembers(memberIds);
        NetworkController.createCrowd(crowd, dbEventType);
    }

    public static void getCrowd(String crowdId, DbEventType dbEventType) {
        NetworkController.getCrowd(crowdId, dbEventType);
    }

    public static void addCrowdMember(String crowdId, String userId, DbEventType dbEventType){
        NetworkController.addCrowdMember(crowdId, userId, dbEventType);
    }

    public static void removeCrowdMember(String crowdId, String userId, DbEventType dbEventType){
        NetworkController.removeCrowdMember(crowdId, userId, dbEventType);
    }

    /*
    Get all the crowds which the given userId is a member of
     */
    public static void getCrowdsByMember(String userId, DbEventType dbEventType) {
        NetworkController.getCrowdsByMember(userId, dbEventType);
    }

    /*
    Books
     */

    public static void createBook(Book book, DbEventType dbEventType) {
        // This book is never stored in the database. It is sent to the server,
        // then retrieved to be stored with the correct _id
        NetworkController.createBook(book, dbEventType);
        MainTabbedActivity.getMixpanel().track("BookAdded");
    }

    /*
    Remove a book from realm and from the backend
     */
    public static void removeBook(String bookId, DbEventType dbEventType) {
        realm.beginTransaction();
        Book book = realm.where(Book.class)
                .equalTo("id", bookId)
                .findFirst();
        book.removeFromRealm();
        realm.commitTransaction();
        NetworkController.removeBook(bookId, dbEventType);
        MainTabbedActivity.getMixpanel().track("BookRemoved");
    }

    /*
    Get a book by id
     */
    public static void getBook(String bookId, DbEventType dbEventType) {
        NetworkController.getBook(bookId, dbEventType);
    }

    /*
    Download book info, if not already in database
     */
    public static void getBookInfo(String isbn, DbEventType dbEventType) {
        BookInfo bookInfo = realm.where(BookInfo.class)
                .equalTo("isbn", isbn)
                .findFirst();
        if (bookInfo == null) {
            /*
            // Add a blank BookInfo to be updated when the BookInfo is downloaded, to avoid running
            // multiple threads for getting BookInfo for the same book
            realm.beginTransaction();
            BookInfo bookInfo1 = new BookInfo();
            bookInfo1.setIsbn(isbn);
            realm.copyToRealm(bookInfo1);
            realm.commitTransaction();
            */
            GetBookInfoAsyncTask.getBookInfo(isbn, dbEventType);
        } else {
            MainTabbedActivity.getBus().post(new DbEvent(dbEventType, isbn));
        }
    }

    /*
    Add a user given by its user id as a renter to a book given by its book id
     */
    public static void addRenter(String bookId, String userId, DbEventType dbEventType) {
        // It should not be necessary to update the book object locally. It should be retrieved from
        // server then put into the database and overwrite the old one.
        NetworkController.addRenter(bookId, userId, dbEventType);
        MainTabbedActivity.getMixpanel().track("BorrowBook");

    }

    /*
    Remove a user given by its user id as a renter from a book given by its book id
     */
    public static void removeRenter(String bookId, String userId, DbEventType dbEventType) {
        // It should not be necessary to update the book object locally. It should be retrieved from
        // server then put into the database and overwrite the old one.
        NetworkController.removeRenter(bookId, userId, dbEventType);
        MainTabbedActivity.getMixpanel().track("ReturnBook");

    }

    /*
    Get books owned by and rented to a given user
     */
    public static void getBooks(String userId, DbEventType dbEventType) {
        NetworkController.getBooksOwnedAndRented(userId, dbEventType);
    }

    /*
    Get books owned by a given user
     */
    public static void getBooksOwned(String userId, DbEventType dbEventType) {
        NetworkController.getBooksOwned(userId, dbEventType);
    }

    /*
    Get books rented to a given user
     */
    public static void getBooksRented(String userId, DbEventType dbEventType) {
        NetworkController.getBooksRented(userId, dbEventType);
    }

    /*
    Get the books owned by the members of the given crowd. Does not include books rented to the
    members
     */
    public static void getCrowdBooks(String crowdId, DbEventType dbEventType) {
        getCrowd(crowdId, DbEventType.CROWD_READY_GET_BOOKS);
    }

    /*
    Populate the database with the necessary data on startup:
    Main user (already downloaded on login)
    Main users books
    Main users crowds
    The books of the members of the above crowds
     */
    public static void getMainUserData(String userId) {
        getBooks(userId, DbEventType.USER_BOOKS_CHANGED);
        NetworkController.getCrowdsByMember(userId, DbEventType.ON_START_USER_CROWDS_READY);
    }

    /*
    Must be called on application exit
     */
    public void onDestroy() {
        Log.i("MainController", "onDestroy: bus, realm");
        MainTabbedActivity.getBus().unregister(this);
        realm.close();
    }

}
