package com.crowdshelf.app.ui.activities;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import com.crowdshelf.app.MainController;
import com.crowdshelf.app.ScannedBookActions;
import com.crowdshelf.app.io.DbEventOk;
import com.crowdshelf.app.io.DbEventType;
import com.crowdshelf.app.models.Book;
import com.crowdshelf.app.models.Crowd;
import com.crowdshelf.app.models.MemberId;
import com.crowdshelf.app.models.User;
import com.crowdshelf.app.ui.fragments.BookGridViewFragment;
import com.crowdshelf.app.ui.fragments.CrowdsScreenFragment;
import com.crowdshelf.app.ui.fragments.ScannerScreenFragment;
import com.crowdshelf.app.ui.fragments.UserScreenFragment;
import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.squareup.otto.ThreadEnforcer;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

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
    // projectToken for testing: 9f321d1662e631f2995d9b8f050c4b44
    private static String projectToken = "93ef1952b96d0faa696176aadc2fbed4"; // e.g.: "1ef7e30d2a58d27f4b90c42e31d6d7ad"
    private static Bus bus = new Bus(ThreadEnforcer.ANY); // ThreadEnforcer.ANY lets any thread post to the bus (but only main thread can subscribe)
    public final int SCANNED_BOOK_ACTION = 1;
    public final int LOGIN = 3;
    SectionsPagerAdapter mSectionsPagerAdapter;
    ViewPager mViewPager;
    private UserScreenFragment userScreenFragment;
    private MainController mainController;
    private Realm realm;
    private RealmConfiguration realmConfiguration;
    private String lastScannedBookIsbn;

    private static String mainUserId; //= "5602a211a0913f110092352a";
    private static String mainUserLoginToken;
    private static String mainUserPassword;
    private static Set<Crowd> userCrowds = new HashSet<>();
    public static Set<Book> userCrowdBooks = new HashSet<>();
    private static Set<Book> ownedBooks = new HashSet<>();
    private static Set<Book> borrowedBooks = new HashSet<>();
    private static Set<Book> lentedBooks = new HashSet<>();
    private int updateUserBooksRan = 0;
    private CrowdsScreenFragment crowdScreenFragment;

    public static MixpanelAPI getMixpanel() {
        return mixpanel;
    }

    private static MixpanelAPI mixpanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tabbed);

        mixpanel = MixpanelAPI.getInstance(this, projectToken);
        mixpanel.track("AppLaunched");

        // Set up database
        realmConfiguration = new RealmConfiguration.Builder(this).build();
//        Realm.deleteRealm(realmConfiguration); // Clean slate
        Realm.setDefaultConfiguration(realmConfiguration); // Make this Realm the default
        realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.clear(Crowd.class);
        realm.clear(Book.class);
        realm.clear(User.class);
        realm.commitTransaction();
        mainController = new MainController();
        mainController.onCreate();

        MainTabbedActivity.getBus().register(this);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.addOnPageChangeListener(this);
        mViewPager.setOffscreenPageLimit(2);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.tab_indicator));

        userScreenFragment = UserScreenFragment.newInstance();
        crowdScreenFragment = CrowdsScreenFragment.newInstance();

        // Remove shadow between ActionBar and tabs
        getSupportActionBar().setElevation(0);

        Intent intent = new Intent(this, LoginActivity.class);
        startActivityForResult(intent, LOGIN);
    }

    public void showAllOwnedBooksButtonPressed(View v){

        openShowAllBooksActivity("Owned");
    }

    public void showAllBorrowedBooksButtonPressed(View v){

        openShowAllBooksActivity("Borrowed");
    }

    public void showAllRentedOutBooksButtonPressed(View v){
        openShowAllBooksActivity("RentedOut");
    }

    private void openShowAllBooksActivity(String shelf){
        Intent intent = new Intent(this, BookGridViewActivity.class);
        intent.putExtra("shelf", shelf);
        startActivity(intent);
    }


    @Subscribe
    public void handleDBEvents(DbEventOk event) {
        if (event.getDbEventType().equals(DbEventType.NONE)) return;
        realm.refresh();
        Log.i(TAG, "Handle DB Event: " + event.getDbEventType());
        switch (event.getDbEventType()) {
            case REDRAW_USER_BOOKS:
                redrawUserBooks();
            case USER_BOOKS_CHANGED:
                updateUserBooks();
                break;
            case USER_CROWDS_CHANGED:
                updateUserCrowds();
                break;
            case USER_CROWD_BOOKS_CHANGED:
                updateUserCrowdBooks();
                break;
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
            case ADD_BOOK_USERSHELF:
                Book book = realm.where(Book.class)
                        .equalTo("id", event.getDbObjectId())
                        .findFirst();
                MainController.getBookInfo(book.getIsbn(), DbEventType.BOOK_INFO_RECEIVED_ADD_TO_USERSHELF);
                break;

            case CREATE_BOOK_AFTER_ADD:
                updateUserBooks();
                break;

            case GET_BOOK_AFTER_ADD:
                Book bookAdded = realm.where(Book.class)
                        .equalTo("id", event.getDbObjectId())
                        .findFirst();
                MainController.getBookInfo(bookAdded.getIsbn(), DbEventType.BOOK_INFO_RECEIVED_ADD_TO_USERSHELF);
                break;

            case BOOK_INFO_RECEIVED_ADD_TO_USERSHELF:
                updateUserBooks();
                break;
        }
    }

    private void redrawUserBooks(){
        Log.i(TAG, "redrawUserBooks");
        userScreenFragment.updateOwnedBookShelf(ownedBooks);
        userScreenFragment.updateLentedBooks(lentedBooks);
        userScreenFragment.updateBorrowedBookShelf(borrowedBooks);
    }

    public void updateUserBooks() {
        if (mainUserId != null) { //TODO: Should not be necessary
            updateUserBooksRan += 1;
            Log.i(TAG, "updateUserBooksRan: " + updateUserBooksRan);
            List<Book> ownedBooksTemp = realm.where(Book.class)
                    .equalTo("owner", mainUserId)
                    .equalTo("rentedTo", "")
                    .findAll();
            if (ownedBooksTemp != null) {
                ownedBooks.clear();
                ownedBooks.addAll(ownedBooksTemp);
                userScreenFragment.updateOwnedBookShelf(ownedBooks);
            }
            List<Book> lentedBooksTemp = realm.where(Book.class)
                    .equalTo("owner", mainUserId)
                    .notEqualTo("rentedTo", "")
                    .findAll();
            if (lentedBooksTemp != null) {
                lentedBooks.clear();
                lentedBooks.addAll(lentedBooksTemp);
                userScreenFragment.updateLentedBooks(lentedBooks);
            }
            List<Book> borrowedBooksTemp = realm.where(Book.class)
                    .equalTo("rentedTo", mainUserId)
                    .findAll();
            if (borrowedBooksTemp != null) {
                borrowedBooks.clear();
                borrowedBooks.addAll(borrowedBooksTemp);
                userScreenFragment.updateBorrowedBookShelf(borrowedBooks);
            }
        }
    }

    public void updateUserCrowds() {
        List<Crowd> allCrowds = realm.where(Crowd.class)
                .findAll();
        List<Crowd> userCrowdsTemp = new ArrayList<>();
        for (Crowd crowd : allCrowds) {
            for (MemberId memberId : crowd.getMembers()) {
                if (memberId.getId().equals(mainUserId)) {
                    userCrowdsTemp.add(crowd);
                    break;
                }
            }
        }
        userCrowds.clear();
        userCrowds.addAll(userCrowdsTemp);
        crowdScreenFragment.updateCrowdList(userCrowds);
    }

    public void updateUserCrowdBooks() {
        List<Book> userCrowdBooksTemp = new ArrayList<>();
        for (Crowd crowd : userCrowds) {
            for (MemberId memberId : crowd.getMembers()) {
                if (memberId.getId().equals(mainUserId)) {
                    continue;
                } else {
                    /*
                    todo: PS! This may add duplicates
                     */
                    userCrowdBooksTemp.addAll(
                            realm.where(Book.class)
                                    .equalTo("owner", memberId.getId())
                                    .or()
                                    .equalTo("rentedTo", memberId.getId())
                                    .findAll()
                    );
                }
            }
        }
        if (userCrowdBooksTemp != null) {
            userCrowdBooks.clear();
            userCrowdBooks.addAll(userCrowdBooksTemp);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(TAG, "onActivityResult");
        switch (requestCode) {
            case LOGIN:
                if (resultCode == RESULT_OK) {
                    String username = data.getStringExtra("username");
                    User u = realm.where(User.class)
                            .equalTo("username", username)
                            .findFirst();
                    mainUserId = u.getId();
                    Log.i(TAG, "Set main user: username " + u.getUsername() + " id " + u.getId());
                    MainController.getMainUserData(mainUserId);
                    getMixpanel().identify(u.getId());
                    return;
                }
                break;
        }

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Log.d(TAG, "Cancelled scan");
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Log.d(TAG, "Scanned");
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                isbnReceived(result.getContents());
            }
        } else {
            Log.d("MainActivity", "Weird");
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
        }


    }//onActivityResult

    public void startViewBook(ScannedBookActions scannedBookAction, String isbn) {
        Intent intent = new Intent(this, ViewBookActivity.class);
        intent.putExtra("SCANNEDBOOKACTION", scannedBookAction.value);
        intent.putExtra("isbn", isbn);
        intent.putExtra("userID", getMainUserId());
        lastScannedBookIsbn = isbn;
        startActivityForResult(intent, SCANNED_BOOK_ACTION);
    }

    @Override
    public void isbnReceived(String isbn) {
        MainController.getBookInfo(isbn, DbEventType.SCAN_COMPLETE_GET_BOOKINFO);
    }


    @Override
    public void itemInUserShelfClicked(String bookID) {
        //TODO: Remove this method
    }

    @Override
    public void itemInBookGridViewClicked(String bookID) {
        Log.i(TAG, "bookID clicked: " + bookID);
        Book b = realm.where(Book.class).equalTo("id", bookID).findFirst();

        Intent intent = new Intent(this, ViewBookActivity.class);
        intent.putExtra("isbn", b.getIsbn());

        startActivityForResult(intent, GET_BOOK_CLICKED_ACTION);
    }

    public void allBooksButtonClicked(View view) {
        Toast.makeText(MainTabbedActivity.this, "At this page you can, in the next version, see all the books you have the possibility to borrow", Toast.LENGTH_LONG).show();
        MixpanelAPI mixpanel = MixpanelAPI.getInstance(this, projectToken);
        mixpanel.track("AllBooksClicked");
    }

    public void changeUser(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivityForResult(intent, LOGIN);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_tabbed, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();

        ((EditText)searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text)).setTextColor(getResources().getColor(R.color.primary_text));
        ((EditText)searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text)).setHintTextColor(getResources().getColor(R.color.primary_text));

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        // Remove to set hint to search in the phones language
        ((EditText)searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text)).setHint("Search...");

        Drawable drawable = menu.findItem(R.id.open_scanner).getIcon();
        if (drawable != null) {
            drawable.mutate();
            drawable.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.open_scanner:
                IntentIntegrator integrator = new IntentIntegrator(this);
//                integrator.setCaptureActivity(ScannerCaptureActivity.class);
                integrator.setCaptureActivity(ToolbarCaptureActivity.class);
                integrator.setOrientationLocked(false);
                integrator.setPrompt("");
                integrator.setDesiredBarcodeFormats(IntentIntegrator.PRODUCT_CODE_TYPES);
                integrator.initiateScan();
                Toast.makeText(this, "Scan book barcode", Toast.LENGTH_LONG).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy: MainController, realm, bus, super");
        mainController.onDestroy();
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
                updateUserBooks();
                break;
            case 1:
                Log.i(TAG, "onPageSelected position: " + position);
                updateUserCrowds();
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

    public void createCrowdClicked(View view) {
        Intent intent = new Intent(this, EditCrowdActivity.class);
        intent.putExtra("crowdID", "");
        startActivity(intent);
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
                    return crowdScreenFragment;
//                case 2:
//                    return ScannerScreenFragment.newInstance();
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
                    return getString(R.string.title_section3).toUpperCase(l);
//                case 2:
//                    return getString(R.string.title_section2).toUpperCase(l);
            }
            return null;
        }
    }


    public static Bus getBus() {
        return bus;
    }

    public static String getMainUserId() {
        return mainUserId;
    }

    public static Set<Crowd> getUserCrowds() {
        return userCrowds;
    }

    public static Set<Book> getUserCrowdBooks() {
        return userCrowdBooks;
    }


    public static String getProjectToken() {
        return projectToken;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private static class UpdateUserBooksThread extends Thread {
        private final int TIMEOUT = 3000;

        public void run() {
            try {
                for (int i = 0; i < 10; i++) {
                    Thread.sleep(TIMEOUT);
                    MainTabbedActivity.getBus().post(DbEventType.REDRAW_USER_BOOKS);
                }
            } catch (InterruptedException ex) {
            }
        }
    }

    public static String getMainUserLoginToken() {
        return mainUserLoginToken;
    }

    public static void setMainUserLoginToken(String s) {
        mainUserLoginToken = s;
    }

    public static String getMainUserPassword() {
        return mainUserPassword;
    }

    public static void setMainUserPassword(String s) {
        mainUserPassword = s;
    }
}
