package com.crowdshelf.app.ui.fragments;

import com.crowdshelf.app.models.Crowd;
import com.crowdshelf.app.ui.activities.EditCrowdActivity;
import com.crowdshelf.app.ui.adapter.CrowdListAdapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import ntnu.stud.markul.crowdshelf.R;

public class CrowdListFragment extends Fragment implements AdapterView.OnItemClickListener {

    private CrowdListAdapter mAdapter;
    private List<Crowd> mItems;

    public void setmItems(Set<Crowd> newmItems) {
        this.mItems.clear();
        this.mItems.addAll(newmItems);
        this.mAdapter.notifyDataSetChanged();
    }

    public static CrowdListFragment newInstance() {
        CrowdListFragment fragment = new CrowdListFragment();
        return fragment;
    }

    public CrowdListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mItems = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_crowd_list, container, false);

        // initialize the adapter
        mAdapter = new CrowdListAdapter(getActivity(), mItems);

        // initialize the ListView
        ListView listView = (ListView) fragmentView.findViewById(R.id.crowdListFragmentListView);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(this);

        return fragmentView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Crowd crowd = mItems.get(position);
        Intent intent = new Intent(getContext(), EditCrowdActivity.class);
        intent.putExtra("crowdID", crowd.getId());
        startActivity(intent);
//        Toast.makeText(getContext(), "Clicked on: " + crowd.getName(), Toast.LENGTH_LONG).show();
    }

}
