package com.crowdshelf.app.ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crowdshelf.app.models.Book;
import com.crowdshelf.app.ui.activities.MainTabbedActivity;

import java.util.List;

import io.realm.Realm;
import ntnu.stud.markul.crowdshelf.R;


public class UserScreenFragment extends Fragment implements BookGridViewFragment.OnBookGridViewFragmentInteractionListener {
    private static final String TAG = "UserScreenFragment";
    private OnUserScreenFragmentInteractionListener mListener;
    private Realm realm;
    private BookGridViewFragment ownedBooksGridViewFragment;
    private BookGridViewFragment lentedBooksGridViewFragment;
    private BookGridViewFragment borrowedBooksGridViewFragment;

    public UserScreenFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static UserScreenFragment newInstance() {
        UserScreenFragment fragment = new UserScreenFragment();
        return fragment;
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

        borrowedBooksGridViewFragment = (BookGridViewFragment) getChildFragmentManager().findFragmentById(R.id.borrowedBooks);
        lentedBooksGridViewFragment = (BookGridViewFragment) getChildFragmentManager().findFragmentById(R.id.lentedBooks);
        ownedBooksGridViewFragment = (BookGridViewFragment) getChildFragmentManager().findFragmentById(R.id.ownedBooks);

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
        //ownedBooksGridViewFragment.onDestroy();
        Log.i(TAG, "onDestroy: realm, super");
        realm.close();
        super.onDestroy();
    }

    public void updateOwnedBookShelf(List<Book> userBooks) {
        ownedBooksGridViewFragment.setmItems(userBooks);
    }

    public void updateBorrowedBookShelf(List<Book> userBooks) {
        borrowedBooksGridViewFragment.setmItems(userBooks);
    }

    public void updateLentedBooks(List<Book> userBooks) {
        lentedBooksGridViewFragment.setmItems(userBooks);
    }


    @Override
    public void itemInBookGridViewClicked(String bookID) {

        mListener.itemInUserShelfClicked(bookID);
    }

    public interface OnUserScreenFragmentInteractionListener {
        // TODO: Update argument type and name
        public void itemInUserShelfClicked(String bookID);
    }
}
