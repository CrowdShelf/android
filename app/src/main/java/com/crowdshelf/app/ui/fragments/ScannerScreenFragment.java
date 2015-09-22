package com.crowdshelf.app.ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ntnu.stud.markul.crowdshelf.R;

public class ScannerScreenFragment extends Fragment {
    private OnScannerScreenInteractionListener mListener;

    public ScannerScreenFragment() {
        // Required empty public constructor
    }

    public static ScannerScreenFragment newInstance() {
        ScannerScreenFragment fragment = new ScannerScreenFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_scanner_screen, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(String ISBN) {
        if (mListener != null) {
            mListener.isbnReceived(ISBN);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnScannerScreenInteractionListener) activity;
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

    public interface OnScannerScreenInteractionListener {
        // TODO: Update argument type and name
        public void isbnReceived(String ISBN);
    }

}
