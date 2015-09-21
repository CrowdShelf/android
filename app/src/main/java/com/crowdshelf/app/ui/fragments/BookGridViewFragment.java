package com.crowdshelf.app.ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.crowdshelf.app.models.Book;
import com.crowdshelf.app.ui.adapter.BookGridViewAdapter;

import java.util.ArrayList;
import java.util.List;

import ntnu.stud.markul.crowdshelf.R;

/**
 * Created by markuslund92 on 19.09.15.
 */
public class BookGridViewFragment extends Fragment implements AdapterView.OnItemClickListener {

    public List<Book> getmItems() {
        return mItems;
    }

    public BookGridViewAdapter getmAdapter() {
        return mAdapter;
    }

    public void setmItems(List<Book> newmItems) {
        this.mItems.clear();
        this.mItems.addAll(newmItems);
        this.mAdapter.notifyDataSetChanged();
    }

    private List<Book> mItems;    // GridView items list
    private BookGridViewAdapter mAdapter;    // GridView adapter

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
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
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        // retrieve the GridView item
        Book item = mItems.get(position);

        // do something
        Toast.makeText(getActivity(), item.getIsbn(), Toast.LENGTH_SHORT).show();
    }
}