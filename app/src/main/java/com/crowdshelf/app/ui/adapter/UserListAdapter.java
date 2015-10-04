package com.crowdshelf.app.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.crowdshelf.app.models.User;

import java.util.ArrayList;

import ntnu.stud.markul.crowdshelf.R;

/**
 * Created by markus on 30.09.15.
 */
public class UserListAdapter extends ArrayAdapter<User> {
    public UserListAdapter(Context context, ArrayList<User> users) {
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        User user = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listitem_user, parent, false);
        }
        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
        TextView tvEmail = (TextView) convertView.findViewById(R.id.tvEmail);
        // Populate the data into the template view using the data object
        tvName.setText(user.getName());
        tvEmail.setText(user.getEmail());
        // Return the completed view to render on screen
        return convertView;
    }
}
