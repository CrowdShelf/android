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
import android.widget.Toast;

import com.crowdshelf.app.MainController;
import com.crowdshelf.app.ScannedBookActions;
import com.crowdshelf.app.io.DBEvent;
import com.crowdshelf.app.models.Book;
import com.crowdshelf.app.models.BookInfo;
import com.crowdshelf.app.ui.fragments.CrowdsScreenFragment;
import com.crowdshelf.app.ui.fragments.ScannerScreenFragment;
import com.crowdshelf.app.ui.fragments.UserScreenFragment;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import ntnu.stud.markul.crowdshelf.R;

public class MainTabbedActivity extends AppCompatActivity implements
        ScannerScreenFragment.OnScannerScreenInteractionListener,
        UserScreenFragment.OnUserScreenFragmentInteractionListener,
        ViewPager.OnPageChangeListener{

    public static final String TAG = "com.crowdshelf.app";
    public final int GET_SCANNED_BOOK_ACTION = 1;

    private Realm realm;
    private static Bus bus = new Bus();
    private static String mainUserId = "";

    public static Bus getBus() {
        return bus;
    }

    public static String getMainUserId() {
        return mainUserId;
    }

    SectionsPagerAdapter mSectionsPagerAdapter;
    ViewPager mViewPager;
    private UserScreenFragment userScreenFragment;
    private List<Book> userBooks;
    private String lastScannedBookIsbn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tabbed);

        // Set up database
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this).build();
        Realm.deleteRealm(realmConfiguration); // Clean slate
        Realm.setDefaultConfiguration(realmConfiguration); // Make this Realm the default

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
        userBooks = new ArrayList<>();
    }

    @Subscribe
    public void handleViewBook(DBEvent event) {
        realm.refresh();
        switch (event.getDbEventType()) {
            case BOOKINFO_READY:
                String bookInfoISBN = event.getDbObjectId();
                // Determine if you own the book you just scanned:
                Book book = realm.where(Book.class)
                        .equalTo("isbn", bookInfoISBN)
                        .equalTo("owner", mainUserId)
                        .findFirst();
                if (book != null) {
                    startViewBook(ScannedBookActions.ADD, bookInfoISBN, book.getId());
                    return;
                }

                // Determine if you rent the book you just scanned:
                book = realm.where(Book.class)
                        .equalTo("isbn", bookInfoISBN)
                        .equalTo("rentedTo", mainUserId)
                        .findFirst();
                if (book != null) {
                    startViewBook(ScannedBookActions.RETURN, bookInfoISBN, book.getId());
                    return;
                }

                // Case: you don't own or rent the book you just scanned:
                book = new Book();
                book.setIsbn(bookInfoISBN);
                book.setOwner(getMainUserId());
                startViewBook(ScannedBookActions.ADD, bookInfoISBN, "");
                break;
        }
    }

    private void loginUser(String username) {
        //TODO: Login user
        Toast.makeText(MainTabbedActivity.this, "User: " + username + " logged in.", Toast.LENGTH_SHORT).show();
        //TODO: populate userShelfISBNs with users books.
    }

    public void viewBookByISBN(String ISBN) {
        MainController.getBookInfo(ISBN);
    }

    public void viewBookID(String id) {
        MainController.getBook(id);
    }

    @Override
    public void onDestroy() {
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
        if (id == R.id.action_settings) {
            loginUser("markus");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(TAG, "onActivityResult");
        if (requestCode == GET_SCANNED_BOOK_ACTION) {
            if(resultCode == RESULT_OK){
                int enumInt = data.getIntExtra("result", 0);
                ScannedBookActions action = ScannedBookActions.fromValue(enumInt);

                Log.i(TAG, "onActivityResult action: " + action);
                switch (action){
                    case ADD:
                        //TODO: Add book to user shelf
                        Book book = new Book();
                        book.setIsbn(lastScannedBookIsbn);
                        userBooks.add(book);
                        userScreenFragment.updateBookShelf(userBooks);
                    case RETURN:
                        //TODO: Return book to owner
                    case BORROW:
                        //TODO Borrow book from owner
                }
            }
            if (resultCode == RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }//onActivityResult

    public void startViewBook(ScannedBookActions scannedBookAction,String ISBN, String bookId) {
        Toast.makeText(getBaseContext(), "ISBN: " + ISBN, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, ViewBookActivity.class);
        intent.putExtra("SCANNEDBOOKACTION", scannedBookAction.value);
        intent.putExtra("ISBN", ISBN);
        intent.putExtra("BOOKID", bookId);
        lastScannedBookIsbn = ISBN;
        startActivityForResult(intent, GET_SCANNED_BOOK_ACTION);
    }

    @Override
    public void isbnReceived(String ISBN) {
        viewBookByISBN(ISBN);
    }

    @Override
    public void removeBookFromShelf(String isbn) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position){
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
            switch (position){
                case 0:
                    return userScreenFragment;
                case 1:
                    return ScannerScreenFragment.newInstance(null, null);
                case 2:
                    return CrowdsScreenFragment.newInstance(null, null);
                default:
                    return CrowdsScreenFragment.newInstance(null, null);
            }

        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
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
