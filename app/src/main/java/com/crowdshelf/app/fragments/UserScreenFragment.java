package com.crowdshelf.app.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.crowdshelf.app.GridViewAdapter;
import com.crowdshelf.app.bookInfo.BookInfo;

import java.util.ArrayList;

import ntnu.stud.markul.crowdshelf.R;


public class UserScreenFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";

    private OnUserScreenFragmentInteractionListener mListener;

    private static GridViewAdapter gridViewAdapter;

    private static GridView gridView;
    private static ArrayList<BookInfo> booksAddedArrayList;
    private TextView numberOfBooksTextView;

    // TODO: Rename and change types and number of parameters
    public static UserScreenFragment newInstance(GridViewAdapter gridViewAdapter) {
        UserScreenFragment fragment = new UserScreenFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, gridViewAdapter);
        fragment.setArguments(args);
        return fragment;
    }

    public UserScreenFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            gridViewAdapter = (GridViewAdapter)getArguments().getSerializable(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_screen, container, false);
        gridView = (GridView) view.findViewById(R.id.gridViewShelf);
        numberOfBooksTextView = (TextView) view.findViewById(R.id.numberOfBooksTextView);
        gridView.setAdapter(gridViewAdapter);
        return view;
    }

    public void removeBookFromShelf(String ISBN) {
        if (mListener != null) {
            mListener.removeBookFromShelf(ISBN);
        }
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

    public void initiate(GridViewAdapter gridViewAdapter) {
        gridView.setAdapter(gridViewAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(getContext(), "You clicked " + booksAddedArrayList.get(position).getTitle(), Toast.LENGTH_SHORT).show();
                //TODO Notify mainAcitivty of book clicked
//                booksAddedArrayList.remove(position);
//                gridViewAdapter.notifyDataSetChanged();
            }
        });
    }

    public interface OnUserScreenFragmentInteractionListener {
        // TODO: Update argument type and name
        public void removeBookFromShelf(String isbn);
    }

}
