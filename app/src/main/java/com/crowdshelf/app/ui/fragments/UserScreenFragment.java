package com.crowdshelf.app.ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crowdshelf.app.models.Book;
import com.crowdshelf.app.ui.activities.MainTabbedActivity;

import java.util.List;

import ntnu.stud.markul.crowdshelf.R;


public class UserScreenFragment extends Fragment implements BookGridViewFragment.OnBookGridViewFragmentInteractionListener {

    private OnUserScreenFragmentInteractionListener mListener;

    private BookGridViewFragment bookGridViewFragment;

    // TODO: Rename and change types and number of parameters
    public static UserScreenFragment newInstance() {
        UserScreenFragment fragment = new UserScreenFragment();
        return fragment;
    }

    public UserScreenFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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

    public void updateBookShelf(List<Book> userBooks){
        Log.i(MainTabbedActivity.TAG, "UserScreenFragment - updateBookShelf");
        bookGridViewFragment.setmItems(userBooks);
    }


    @Override
    public void itemInBookGridViewClicked(Book book) {
        mListener.itemInUserShelfClicked(book);
    }

    public interface OnUserScreenFragmentInteractionListener {
        // TODO: Update argument type and name
        public void itemInUserShelfClicked(Book book);
    }
}
