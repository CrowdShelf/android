package com.crowdshelf.app.ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.crowdshelf.app.io.DBEvent;
import com.crowdshelf.app.models.Book;
import com.crowdshelf.app.models.BookInfo;
import com.crowdshelf.app.ui.activities.MainTabbedActivity;
import com.squareup.otto.Subscribe;

import java.util.List;

import io.realm.Realm;
import ntnu.stud.markul.crowdshelf.R;


public class UserScreenFragment extends Fragment implements BookGridViewFragment.OnBookGridViewFragmentInteractionListener {

    private OnUserScreenFragmentInteractionListener mListener;
    private Realm realm;
    private BookGridViewFragment bookGridViewFragment;

    public UserScreenFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static UserScreenFragment newInstance() {
        UserScreenFragment fragment = new UserScreenFragment();
        return fragment;
    }

    @Subscribe
    public void handleViewBook(DBEvent event) {
        realm.refresh();
        Log.d(MainTabbedActivity.TAG, "realmpath: " + realm.getPath());
        switch (event.getDbEventType()) {
            case USER_BOOKS_CHANGED:
                Log.i(MainTabbedActivity.TAG, "MainTabbedActivity - handleViewBook - BOOKINFO_READY");
                String userId = event.getDbObjectId();

                if (userId.equals(MainTabbedActivity.getMainUserId())) {
                    List<Book> books = realm.where(Book.class)
                            .equalTo("owner", userId)
                            .findAll();
                    // todo: show these books
                }
                break;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MainTabbedActivity.getBus().register(this);
        realm = Realm.getDefaultInstance();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_screen, container, false);
        List<Fragment> userScreenFragments = getChildFragmentManager().getFragments();
        bookGridViewFragment = null;

        for (Fragment fragment: userScreenFragments){
            if (fragment.getClass().equals(BookGridViewFragment.class)){
                bookGridViewFragment = (BookGridViewFragment) fragment;
            }
        }
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnUserScreenFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnUserScreenFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroy() {
        realm.close();
        MainTabbedActivity.getBus().unregister(this);
        super.onDestroy();
    }

    public void updateBookShelf(List<BookInfo> userBooks) {
        Log.i(MainTabbedActivity.TAG, "UserScreenFragment - updateBookShelf");
        bookGridViewFragment.setmItems(userBooks);
    }


    @Override
    public void itemInBookGridViewClicked(String isbn) {

        mListener.itemInUserShelfClicked(isbn);
    }

    public interface OnUserScreenFragmentInteractionListener {
        // TODO: Update argument type and name
        public void itemInUserShelfClicked(String isbn);
    }
}
