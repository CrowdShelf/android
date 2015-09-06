package com.crowdshelf.app;

/**
 * Created by markuslund92 on 01.09.15.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.crowdshelf.app.jsonModels.GoogleBooksVolumeInfo;

import java.util.List;

import ntnu.stud.markul.crowdshelf.R;


public class GridViewAdapter extends ArrayAdapter {
    private Context mContext;
    private List<GoogleBooksVolumeInfo> books;

    public GridViewAdapter(Context context, int resource, List<GoogleBooksVolumeInfo> objects) {
        super(context, resource, objects);
        mContext = context;
        books = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View grid = inflater.inflate(R.layout.book_single, null);
        TextView textView = (TextView) grid.findViewById(R.id.book_text);
        ImageView imageView = (ImageView) grid.findViewById(R.id.book_image);
        textView.setText(books.get(position).getTitle());
        if (books.get(position).getImageLinks() == null){
            imageView.setImageResource(R.mipmap.ic_launcher);
        }else{
            new ImageLoadTask(books.get(position).getImageLinks().getThumbnail(), imageView).execute();
        }
        return grid;
    }
}