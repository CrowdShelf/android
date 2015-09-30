package com.crowdshelf.app.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.crowdshelf.app.MainController;
import com.crowdshelf.app.ScannedBookActions;
import com.crowdshelf.app.io.DBEvent;
import com.crowdshelf.app.io.DBEventType;
import com.crowdshelf.app.models.Book;
import com.crowdshelf.app.models.BookInfo;
import com.crowdshelf.app.models.User;
import com.crowdshelf.app.ui.fragments.BookGridViewFragment;
import com.crowdshelf.app.ui.fragments.ScannerScreenFragment;
import com.crowdshelf.app.ui.fragments.UserScreenFragment;
import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.squareup.otto.ThreadEnforcer;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import ntnu.stud.markul.crowdshelf.R;

public class MainTabbedActivity extends AppCompatActivity implements
        ScannerScreenFragment.OnScannerScreenInteractionListener,
        UserScreenFragment.OnUserScreenFragmentInteractionListener,
        ViewPager.OnPageChangeListener, BookGridViewFragment.OnBookGridViewFragmentInteractionListener {

    public static final String TAG = "MainTabbedActivity";
    private static final int GET_BOOK_CLICKED_ACTION = 2;
    // projectToken for dev: 93ef1952b96d0faa696176aadc2fbed4
    // projectToken for user: 9f321d1662e631f2995d9b8f050c4b44
    private static String projectToken = "9f321d1662e631f2995d9b8f050c4b44"; // e.g.: "1ef7e30d2a58d27f4b90c42e31d6d7ad"
    private static Bus bus = new Bus(ThreadEnforcer.ANY); // ThreadEnforcer.ANY lets any thread post to the bus (but only main thread can subscribe)
    private static String mainUserId; //= "5602a211a0913f110092352a";
    public final int SCANNED_BOOK_ACTION = 1;
    public final int USERNAME = 3;
    SectionsPagerAdapter mSectionsPagerAdapter;
    ViewPager mViewPager;
    private UserScreenFragment userScreenFragment;
    private Realm realm;
    private String lastScannedBookIsbn;

    private List<Book> userBooks;
    private List<BookInfo> userBookInfos;
    private RealmConfiguration realmConfiguration;

    public static Bus getBus() {
        return bus;
    }

    public static String getMainUserId() {
        return mainUserId;
    }

    public static String getProjectToken() {
        return projectToken;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tabbed);

        MixpanelAPI mixpanel = MixpanelAPI.getInstance(this, projectToken);
        mixpanel.track("AppLaunched");

        // Set up database
        realmConfiguration = new RealmConfiguration.Builder(this).build();
//        Realm.deleteRealm(realmConfiguration); // Clean slate
        Realm.setDefaultConfiguration(realmConfiguration); // Make this Realm the default
        MainController.onCreate();

        MainTabbedActivity.getBus().register(this);
        realm = Realm.getDefaultInstance();

        Intent intent = new Intent(this, LoginActivity.class);
        startActivityForResult(intent, USERNAME);


        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.addOnPageChangeListener(this);

        userScreenFragment = UserScreenFragment.newInstance();
        userBookInfos = new ArrayList<BookInfo>();
        userBooks = new ArrayList<Book>();

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_testing);
        item.setVisible(false);
        super.onPrepareOptionsMenu(menu);
        return true;
    }

    @Subscribe
    public void handleDBEvents(DBEvent event) {
        realm.refresh();
        Log.i(TAG, "handleViewBook - event: " + event.getDbEventType());
        switch (event.getDbEventType()) {
            case SCAN_COMPLETE_GET_BOOKINFO:
                lastScannedBookIsbn = event.getDbObjectId();

                Book b1 = realm.where(Book.class)
                        .equalTo("isbn", lastScannedBookIsbn)
                        .equalTo("owner", mainUserId)
                        .findFirst();

                if (b1 == null) {
                    Log.i(TAG, "handleViewBook - SCAN_COMPLETE_GET_BOOKINFO - b1: " + b1);
                    //Book does not exist
                    startViewBook(ScannedBookActions.NOT_OWNING_OR_RENTING, lastScannedBookIsbn);
                } else {
                    Log.i(TAG, "handleViewBook - SCAN_COMPLETE_GET_BOOKINFO - b1isbn: " + b1.getIsbn() + " b1owner: " + b1.getOwner());

                    startViewBook(ScannedBookActions.NOT_OWNING_OR_RENTING, lastScannedBookIsbn);
                }
                break;

            case BOOK_CHANGED:
                break;
            case ADD_BOOK_USERSHELF:
                Book book = realm.where(Book.class)
                        .equalTo("id", event.getDbObjectId())
                        .findFirst();
                MainController.getBookInfo(book.getIsbn(), DBEventType.BOOK_INFO_RECEIVED_ADD_TO_USERSHELF);
                break;

            case CREATE_BOOK_AFTER_ADD:
                Book newlyCreatedBook = realm.where(Book.class)
                        .equalTo("id", event.getDbObjectId())
                        .findFirst();
                userBooks.add(newlyCreatedBook);
                userScreenFragment.updateBookShelf(userBooks);
                break;

            case GET_BOOK_AFTER_ADD:
                Book bookAdded = realm.where(Book.class)
                        .equalTo("id", event.getDbObjectId())
                        .findFirst();
                MainController.getBookInfo(bookAdded.getIsbn(), DBEventType.BOOK_INFO_RECEIVED_ADD_TO_USERSHELF);
                break;

            case BOOK_INFO_RECEIVED_ADD_TO_USERSHELF:
                Log.i(TAG, "Should add to user shelf");
                Book bookToAdd = realm.where(Book.class)
                        .equalTo("isbn", event.getDbObjectId())
                        .equalTo("owner", getMainUserId())
                        .findFirst();
                if (bookToAdd != null){
                    userBooks.add(bookToAdd);
                    userScreenFragment.updateBookShelf(userBooks);
                }
                break;
            case BOOK_REMOVED:
        }
    }

    private void loginUser(String username) {
        //TODO: Login user
        Toast.makeText(MainTabbedActivity.this, "User: " + username + " logged in.", Toast.LENGTH_SHORT).show();
        //TODO: populate userShelfISBNs with users books.
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(TAG, "onActivityResult");
        switch (requestCode) {
            case SCANNED_BOOK_ACTION:
                if (resultCode == RESULT_OK) {
                    int enumInt = data.getIntExtra("result", 0);
                    ScannedBookActions action = ScannedBookActions.fromValue(enumInt);

                    Log.i(TAG, "onActivityResult action: " + action);
                    switch (action) {
                        case ADD_BUTTON_CLICKED:
                            //TODO: Add book to user shelf

                            Book b1 = new Book();
                            b1.setIsbn(lastScannedBookIsbn);
                            b1.setOwner(mainUserId);
                            b1.setAvailableForRent(true);

                            //MainController.getBookInfo(lastScannedBookIsbn, DBEventType.CREATE_BOOK_AFTER_ADD);
                            Log.i(TAG, "onActivityResult createBook");
                            MainController.createBook(b1, DBEventType.CREATE_BOOK_AFTER_ADD);
                            Log.i(TAG, "onActivityResult createdBook");
                    }
                }
                break;
            case GET_BOOK_CLICKED_ACTION:
                Log.i(TAG, "onActivityResult  - GET_BOOK_CLICKED_ACTION");

                if (resultCode == RESULT_OK) {
                    int enumInt = data.getIntExtra("result", -1);
                    ScannedBookActions action = ScannedBookActions.fromValue(enumInt);


                    String bookID = data.getStringExtra("bookID");

                    switch (action) {
                        case REMOVE_BUTTON_CLICKED:
                            Log.i(TAG, "REMOVE_BUTTON_CLICKED");
                            for (Book b : userBooks) {
                                if (b.getId().equals(bookID)) {
                                    Log.i(TAG, "Book deleted: " + b.getId());
                                    Book bookStoredOnServer = realm.where(Book.class)
                                            .equalTo("isbn", b.getIsbn())
                                            .findFirst();
                                    userBooks.remove(b);
                                    userScreenFragment.updateBookShelf(userBooks);
                                    MainController.removeBook(b.getId(), DBEventType.BOOK_REMOVED);
                                    break;
                                }
                            }
                    }
                }
                break;
            case USERNAME:
                if (resultCode == RESULT_OK) {
                    String username = data.getStringExtra("username");
                    Log.i(TAG, "Username: " + username);
                    User u = realm.where(User.class)
                            .equalTo("username", username)
                            .findFirst();
                    mainUserId = u.getId();
                    Log.i(TAG, "Set main user: username " + u.getUsername() + " id " + u.getId());
                    MainController.getBooks(u.getId(), DBEventType.ADD_BOOK_USERSHELF);
                    Toast.makeText(this, "Swipe right to go to the scanner", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }//onActivityResult

    public void startViewBook(ScannedBookActions scannedBookAction, String ISBN) {
        Intent intent = new Intent(this, ViewBookActivity.class);
        intent.putExtra("SCANNEDBOOKACTION", scannedBookAction.value);
        intent.putExtra("ISBN", ISBN);
        lastScannedBookIsbn = ISBN;
        startActivityForResult(intent, SCANNED_BOOK_ACTION);
    }

    public void fakeScan(View v) {
        isbnReceived("9780670921607");
    }

    @Override
    public void isbnReceived(String isbn) {
        MainController.getBookInfo(isbn, DBEventType.SCAN_COMPLETE_GET_BOOKINFO);
    }


    @Override
    public void itemInUserShelfClicked(String bookID) {
        //TODO: Remove this method
    }

    @Override
    public void itemInBookGridViewClicked(String bookID) {
        Log.i(TAG, "bookID clicked: " + bookID);
        Intent intent = new Intent(this, ViewBookActivity.class);
        intent.putExtra("bookID", bookID);
        startActivityForResult(intent, GET_BOOK_CLICKED_ACTION);
    }

    public void scannerButtonClicked(View view) {
        mViewPager.setCurrentItem(1);
    }

    public void allBooksButtonClicked(View view) {
        Toast.makeText(MainTabbedActivity.this, "At this page you can, in the next version, see all the books you have the possibility to borrow", Toast.LENGTH_LONG).show();
        MixpanelAPI mixpanel = MixpanelAPI.getInstance(this, projectToken);
        mixpanel.track("AllBooksClicked");
    }

    public void changeUser(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivityForResult(intent, USERNAME);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_tabbed, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_testing) {
            Intent intent = new Intent(this, TestingActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy: MainController, realm, bus, super");
        MainController.onDestroy();
        realm.close();
        MainTabbedActivity.getBus().unregister(this);
        super.onDestroy();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                Log.i(TAG, "onPageSelected position: " + position);
                break;
            case 1:
                Log.i(TAG, "onPageSelected position: " + position);
                break;
            case 2:
                Log.i(TAG, "onPageSelected position: " + position);
                break;
            default:
                Log.i(TAG, "onPageSelected positiondefault: " + position);
                break;
        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position) {
                case 0:
                    return userScreenFragment;
                case 1:
                    return ScannerScreenFragment.newInstance();
//                case 2:
//                    return CrowdsScreenFragment.newInstance(null, null);
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
                case 2:
                    return getString(R.string.title_section3).toUpperCase(l);
            }
            return null;
        }

    }

}
