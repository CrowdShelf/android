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

import io.realm.Realm;
import io.realm.RealmConfiguration;
import ntnu.stud.markul.crowdshelf.R;

public class MainTabbedActivity extends AppCompatActivity implements
        ScannerScreenFragment.OnScannerScreenInteractionListener,
        UserScreenFragment.OnUserScreenFragmentInteractionListener,
        ViewPager.OnPageChangeListener, BookGridViewFragment.OnBookGridViewFragmentInteractionListener {

    // projectToken for dev: 93ef1952b96d0faa696176aadc2fbed4
    // projectToken for testing: 9f321d1662e631f2995d9b8f050c4b44
    private static String projectToken = "93ef1952b96d0faa696176aadc2fbed4"; // e.g.: "1ef7e30d2a58d27f4b90c42e31d6d7ad"
    public static final String TAG = "MainTabbedActivity";
    private static final int GET_BOOK_CLIKCED_ACTION = 2;
    public final int GET_SCANNED_BOOK_ACTION = 1;
    SectionsPagerAdapter mSectionsPagerAdapter;
    private UserScreenFragment userScreenFragment;
    ViewPager mViewPager;
    private Realm realm;
    private static Bus bus = new Bus(ThreadEnforcer.ANY); // ThreadEnforcer.ANY lets any thread post to the bus (but only main thread can subscribe)
    private static String mainUserId = "5602a211a0913f110092352a";
    private String lastScannedBookIsbn;
    private List<BookInfo> userBookInfos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tabbed);

        MixpanelAPI mixpanel = MixpanelAPI.getInstance(this, projectToken);
        mixpanel.track("AppLaunched");

        // Set up database
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this).build();
        Realm.deleteRealm(realmConfiguration); // Clean slate
        Realm.setDefaultConfiguration(realmConfiguration); // Make this Realm the default
        MainController.onCreate();

        MainTabbedActivity.getBus().register(this);
        realm = Realm.getDefaultInstance();

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.addOnPageChangeListener(this);

        userScreenFragment = UserScreenFragment.newInstance();
        userBookInfos = new ArrayList<>();

        Toast.makeText(this, "Swipe to the left to go to the scanner", Toast.LENGTH_LONG).show();
    }

    @Subscribe
    public void handleViewBook(DBEvent event) {
        realm.refresh();
        Log.i(TAG, "handleViewBook - event: " + event.getDbEventType());
        switch (event.getDbEventType()) {
            case SCAN_COMPLETE_GET_BOOKINFO:
                String bookId = "5603d891e4b0d3b5acc4981c";
                lastScannedBookIsbn = event.getDbObjectId();

                Book b1 = realm.where(Book.class)
                        .equalTo("isbn", lastScannedBookIsbn)
                        .equalTo("owner", mainUserId)
                        .findFirst();

                if (b1 == null) {
                    Log.i(TAG, "handleViewBook - SCAN_COMPLETE_GET_BOOKINFO - b1: " + b1);
                    //Book does not exist
                    startViewBook(ScannedBookActions.NOT_OWNING_OR_RENTING, lastScannedBookIsbn, "");
                } else {
                    Log.i(TAG, "handleViewBook - SCAN_COMPLETE_GET_BOOKINFO - b1isbn: " + b1.getIsbn() + " b1owner: " + b1.getOwner());

                    startViewBook(ScannedBookActions.NOT_OWNING_OR_RENTING, lastScannedBookIsbn, "");
                }
                break;


            case BOOK_CHANGED:
                break;
            case ADD_BOOKINFO_USERSHELF:
                BookInfo bi = realm.where(BookInfo.class)
                        .equalTo("isbn", event.getDbObjectId())
                        .findFirst();
                userBookInfos.add(bi);
                userScreenFragment.updateBookShelf(userBookInfos);
        }
    }

    private void loginUser(String username) {
        //TODO: Login user
        Toast.makeText(MainTabbedActivity.this, "User: " + username + " logged in.", Toast.LENGTH_SHORT).show();
        //TODO: populate userShelfISBNs with users books.
    }

    @Override
    public void onDestroy() {
        MainController.onDestroy();
        realm.close();
        MainTabbedActivity.getBus().unregister(this);
        super.onDestroy();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(TAG, "onActivityResult");
        if (requestCode == GET_SCANNED_BOOK_ACTION) {
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

                        MainController.getBookInfo(lastScannedBookIsbn, DBEventType.ADD_BOOKINFO_USERSHELF);
//                        Log.i(TAG, "onActivityResult createBook");
//                        MainController.createBook(b1, DBEventType.ADD_BOOKINFO_USERSHELF);
//                        Log.i(TAG, "onActivityResult createdBook");
                }
            }
        } else if (requestCode == GET_BOOK_CLIKCED_ACTION) {
            Log.i(TAG, "onActivityResult  - GET_BOOK_CLICKED_ACTION");

            if (resultCode == RESULT_OK) {
                Log.i(TAG, "onActivityResult  - GET_BOOK_CLICKED_ACTION - RESULT_OK");

                int enumInt = data.getIntExtra("result", 0);
                String isbn = data.getStringExtra("isbn");
                Log.i(TAG, "onActivityResult - GET_BOOK_CLICKED_ACTION - isbn: " + isbn);

                ScannedBookActions action = ScannedBookActions.fromValue(enumInt);

                Log.i(TAG, "onActivityResult - GET_BOOK_CLICKED_ACTION - action: " + action);
                switch (action) {
                    case REMOVE_BUTTON_CLICKED:
                        BookInfo bookInfo = null;
                        for (BookInfo bi : userBookInfos) {
                            if (bi.getIsbn().equals(isbn)) {
                                bookInfo = bi;
                                break;
                            }
                        }
                        if (userBookInfos.size() > 0) {
                            Log.i(TAG, "onActivityResult - REMOVE_BUTTON_CLICKED userbooksInfo object isbn: " + userBookInfos.get(0).getIsbn());
                            Log.i(TAG, "onActivityResult - REMOVE_BUTTON_CLICKED isbn: " + isbn);
                        }

                        userBookInfos.remove(bookInfo);
                        userScreenFragment.updateBookShelf(userBookInfos);


                }
            }
        }
    }//onActivityResult

    public void startViewBook(ScannedBookActions scannedBookAction, String ISBN, String bookId) {
        Intent intent = new Intent(this, ViewBookActivity.class);
        intent.putExtra("SCANNEDBOOKACTION", scannedBookAction.value);
        intent.putExtra("ISBN", ISBN);
        intent.putExtra("BOOKID", bookId);
        lastScannedBookIsbn = ISBN;
        startActivityForResult(intent, GET_SCANNED_BOOK_ACTION);
    }

    @Override
    public void isbnReceived(String isbn) {
        MainController.getBookInfo(isbn, DBEventType.SCAN_COMPLETE_GET_BOOKINFO);
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

    @Override
    public void itemInUserShelfClicked(String isbn) {

    }

    @Override
    public void itemInBookGridViewClicked(String isbn) {
        Intent intent = new Intent(this, ViewBookActivity.class);
        intent.putExtra("ISBN", isbn);
        startActivityForResult(intent, GET_BOOK_CLIKCED_ACTION);
    }

    public void scannerButtonClicked(View view) {
        mViewPager.setCurrentItem(1);
    }

    public void allBooksButtonClicked(View view) {
        Toast.makeText(MainTabbedActivity.this, "At this page you can, in the next version, see all the books you have the possibility to borrow", Toast.LENGTH_LONG).show();
        MixpanelAPI mixpanel = MixpanelAPI.getInstance(this, projectToken);
        mixpanel.track("AllBooksClicked");
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
    public static Bus getBus() {
        return bus;
    }

    public static String getMainUserId() {
        return mainUserId;
    }

    public static String getProjectToken() {
        return projectToken;
    }

}
