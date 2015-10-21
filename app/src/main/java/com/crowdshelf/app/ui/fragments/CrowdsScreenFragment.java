package com.crowdshelf.app.ui.fragments;

import com.crowdshelf.app.models.Crowd;
import com.crowdshelf.app.ui.activities.MainTabbedActivity;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import java.util.Set;

import io.realm.Realm;
import ntnu.stud.markul.crowdshelf.R;


public class CrowdsScreenFragment extends Fragment {

    private static final String TAG = "CrowdsScreenFragment";
    private Realm realm;
    private String crowdToShowId;

    private OnFragmentInteractionListener mListener;
    private CrowdListFragment crowdListFragment;

    public CrowdsScreenFragment() {
        // Required empty public constructor
    }


    public static CrowdsScreenFragment newInstance() {
        CrowdsScreenFragment fragment = new CrowdsScreenFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        MainTabbedActivity.getBus().register(this);
        realm = Realm.getDefaultInstance();
        // Inflate the layout for this fragment

        try {
            View view = inflater.inflate(R.layout.fragment_crowds_screen, container, false);
            crowdListFragment = (CrowdListFragment) getChildFragmentManager().findFragmentById(R.id.crowdListFragment);
            return view;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public void updateCrowdList(Set<Crowd> crowds) {
        if (crowdListFragment != null){
            crowdListFragment.setmItems(crowds);
        }
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy: realm, bus, super");
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
