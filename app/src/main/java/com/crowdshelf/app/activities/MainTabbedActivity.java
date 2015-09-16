package com.crowdshelf.app.activities;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.crowdshelf.app.GridViewAdapter;
import com.crowdshelf.app.ScannedBookActions;
import com.crowdshelf.app.bookInfo.BookInfo;
import com.crowdshelf.app.bookInfo.BookInfoGetter;
import com.crowdshelf.app.fragments.CrowdsScreenFragment;
import com.crowdshelf.app.fragments.ScannerScreenFragment;
import com.crowdshelf.app.fragments.UserScreenFragment;

import ntnu.stud.markul.crowdshelf.R;

public class MainTabbedActivity extends AppCompatActivity implements
        ScannerScreenFragment.OnScannerScreenInteractionListener,
        UserScreenFragment.OnUserScreenFragmentInteractionListener,
        ViewPager.OnPageChangeListener{

    public static final String TAG = "com.crowdshelf.app";
    public final int GET_SCANNED_BOOK_ACTION = 1;


    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */

    SectionsPagerAdapter mSectionsPagerAdapter;
    ViewPager mViewPager;
    private String lastScannedBookISBN;
    private ArrayList<String> userShelfISBNs;
    private BookInfoGetter bookInfoGetter;
    private UserScreenFragment userScreenFragment;
    private List<BookInfo> booksAddedArrayList;
    private GridViewAdapter userShelfGridViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tabbed);

        bookInfoGetter = new BookInfoGetter();
        booksAddedArrayList = new ArrayList<>();
        userShelfGridViewAdapter = new GridViewAdapter(MainTabbedActivity.this, R.layout.book_single, booksAddedArrayList);
//        userScreenFragment.initiate(userShelfGridViewAdapter);

        userScreenFragment = UserScreenFragment.newInstance(userShelfGridViewAdapter);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

    }

    private void loginUser(String username) {
        //TODO: Login user
        Toast.makeText(MainTabbedActivity.this, "User: " + username + " logged in.", Toast.LENGTH_SHORT).show();
        userShelfISBNs = new ArrayList<>();
        //TODO: populate userShelfISBNs with users books.
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
                        booksAddedArrayList.add(new BookInfo("isbn", "Dette er en tittel!", "subitle", "", "", "", null, "Dette er beskrivelsen av boka."));
                        userShelfGridViewAdapter.notifyDataSetChanged();
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

    @Override
    public void isbnReceived(String ISBN) {
        Toast.makeText(getBaseContext(), "ISBN: " + ISBN, Toast.LENGTH_SHORT).show();
        lastScannedBookISBN = ISBN;
        Intent intent = new Intent(this, ViewBookActivity.class);
        intent.putExtra("ISBN", ISBN);
        startActivityForResult(intent, GET_SCANNED_BOOK_ACTION);
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
            case 1:
                Log.i(TAG, "onPageSelected position: " + position);
            case 2:
                Log.i(TAG, "onPageSelected position: " + position);
            default:
                Log.i(TAG, "onPageSelected positiondefault: " + position);
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
                    return PlaceholderFragment.newInstance(position + 1);
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



    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main_tabbed, container, false);
            return rootView;
        }
    }

}
