package com.crowdshelf.app.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.crowdshelf.app.models.Book;
import com.crowdshelf.app.models.BookInfo;
import com.crowdshelf.app.models.User;

import java.util.ArrayList;

import ntnu.stud.markul.crowdshelf.R;

/**
 * Created by markus on 30.09.15.
 */
public class SearchResultAdapter extends ArrayAdapter<BookInfo> {
    public SearchResultAdapter(Context context, ArrayList<BookInfo> books) {
        super(context, 0, books);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        BookInfo bookInfo = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listitem_search_result, parent, false);
        }
        // Lookup view for data population
        ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView);
        TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
        TextView tvEmail = (TextView) convertView.findViewById(R.id.tvEmail);
        // Populate the data into the template view using the data object
        Bitmap bitmap = BitmapFactory.decodeByteArray(bookInfo.getArtworkByteArray(), 0, bookInfo.getArtworkByteArray().length);
        imageView.setImageBitmap(bitmap);
        tvName.setText(bookInfo.getTitle());
        tvEmail.setText(bookInfo.getAuthor());
        // Return the completed view to render on screen
        return convertView;
    }
}