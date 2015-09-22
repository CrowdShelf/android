package com.crowdshelf.app.ui.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crowdshelf.app.io.DBEvent;
import com.crowdshelf.app.models.Book;
import com.crowdshelf.app.models.Crowd;
import com.crowdshelf.app.models.MemberId;
import com.crowdshelf.app.ui.activities.MainTabbedActivity;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import ntnu.stud.markul.crowdshelf.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CrowdsScreenFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CrowdsScreenFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CrowdsScreenFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Realm realm;
    private String crowdToShowId;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CrowdsScreenFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CrowdsScreenFragment newInstance(String param1, String param2) {
        CrowdsScreenFragment fragment = new CrowdsScreenFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Subscribe
    public void handleViewBook(DBEvent event) {
        realm.refresh();
        Log.d(MainTabbedActivity.TAG, "realmpath: " + realm.getPath());
        switch (event.getDbEventType()) {
            case CROWD_BOOKS_CHANGED:
                Log.i(MainTabbedActivity.TAG, "MainTabbedActivity - handleViewBook - BOOKINFO_READY");
                String crowdId = event.getDbObjectId();

                if (crowdId.equals(crowdToShowId)) {
                    Crowd c = realm.where(Crowd.class)
                            .equalTo("id", crowdId)
                            .findFirst();
                    List<Book> books = new ArrayList<Book>();
                    for (MemberId memberId: c.getMembers()) {
                        // todo: we might want to get another subset of books, but this will work for now
                        books.addAll(realm.where(Book.class)
                            .equalTo("owner", memberId.getId())
                            .findAll());
                    }
                    // todo: show these books
                }
                break;
        }
    }

    public CrowdsScreenFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MainTabbedActivity.getBus().register(this);
        realm = Realm.getDefaultInstance();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_crowds_screen, container, false);

    }

    @Override
    public void onDestroy() {
        realm.close();
        MainTabbedActivity.getBus().unregister(this);
        super.onDestroy();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
//        try {
//            mListener = (OnUserScreenFragmentInteractionListener) activity;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(activity.toString()
//                    + " must implement OnUserScreenFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
