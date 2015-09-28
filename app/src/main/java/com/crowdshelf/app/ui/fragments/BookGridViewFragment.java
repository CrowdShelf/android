package com.crowdshelf.app.ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.crowdshelf.app.models.Book;
import com.crowdshelf.app.ui.adapter.BookGridViewAdapter;

import java.util.ArrayList;
import java.util.List;

import ntnu.stud.markul.crowdshelf.R;

/**
 * Created by markuslund92 on 19.09.15.
 */
public class BookGridViewFragment extends Fragment implements AdapterView.OnItemClickListener {

    private OnBookGridViewFragmentInteractionListener mListener;
    private List<Book> mItems;    // GridView items list
    private BookGridViewAdapter mAdapter;    // GridView adapter

    public List<Book> getmItems() {
        return mItems;
    }

    public void setmItems(List<Book> newmItems) {
        this.mItems.clear();
        this.mItems.addAll(newmItems);
        this.mAdapter.notifyDataSetChanged();
    }

    public BookGridViewAdapter getmAdapter() {
        return mAdapter;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnBookGridViewFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnBookGridViewFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // initialize the items list
        mItems = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // inflate the root view of the fragment
        View fragmentView = inflater.inflate(R.layout.fragment_book_gridview, container, false);

        // initialize the adapter
        mAdapter = new BookGridViewAdapter(getActivity(), mItems);

        // initialize the GridView
        GridView gridView = (GridView) fragmentView.findViewById(R.id.gridView);
        gridView.setAdapter(mAdapter);
        gridView.setOnItemClickListener(this);

        return fragmentView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // retrieve the GridView item
        Book item = mItems.get(position);

        mListener.itemInBookGridViewClicked(item.getIsbn());
    }


    public interface OnBookGridViewFragmentInteractionListener {
        public void itemInBookGridViewClicked(String isbn);
    }
}