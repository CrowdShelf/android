package com.crowdshelf.app.ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crowdshelf.app.io.DBEvent;
import com.crowdshelf.app.models.Book;
import com.crowdshelf.app.ui.activities.MainTabbedActivity;
import com.squareup.otto.Subscribe;

import java.util.List;

import io.realm.Realm;
import ntnu.stud.markul.crowdshelf.R;


public class UserScreenFragment extends Fragment implements BookGridViewFragment.OnBookGridViewFragmentInteractionListener {
    private static final String TAG = "UserScreenFragment";
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
        //bookGridViewFragment.onDestroy();
        Log.i(TAG, "onDestroy: realm, super");
        realm.close();
        super.onDestroy();
    }

    public void updateBookShelf(List<Book> userBooks) {
        Log.i(TAG, "updateBookShelf");
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
