package com.crowdshelf.app;

/**
 * Created by markuslund92 on 01.09.15.
 */

import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.crowdshelf.app.activities.MainTabbedActivity;
import com.crowdshelf.app.bookInfo.BookInfo;
import com.crowdshelf.app.bookInfo.GoogleBooksVolumeInfo;
import com.crowdshelf.app.network.PopulateImageViewWithUrlTask;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ntnu.stud.markul.crowdshelf.R;


public class GridViewAdapter extends ArrayAdapter implements Serializable{
    private Context mContext;
    private List<BookInfo> books;

//    private BookInfo[] books = {
//            new BookInfo("", "Title", "", "", "", "", null, "Description")
//    };

    public GridViewAdapter(Context context, int resource, List<BookInfo> objects) {
        super(context, resource);
        mContext = context;
        books = objects;
        books.add(new BookInfo("", "Title", "", "", "", "", null, "Description"));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.i(MainTabbedActivity.TAG, "GridViewAdapter - GetView");
        ViewHolder mViewHolder;

        if (convertView == null){
            mViewHolder = new ViewHolder();

            convertView = LayoutInflater.from(mContext).inflate(R.layout.book_single, null);

//            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            convertView = inflater.inflate(R.layout.book_single, parent, false);


            mViewHolder.bookCoverImageView = (ImageView) convertView.findViewById(R.id.book_image);
            mViewHolder.bookTitleTextView = (TextView) convertView.findViewById(R.id.book_text);
            Log.i(MainTabbedActivity.TAG, "GridViewAdapter - bookCoverImageView" + mViewHolder.bookCoverImageView);
            Log.i(MainTabbedActivity.TAG, "GridViewAdapter - bookTitleTextView" + mViewHolder.bookTitleTextView);


            convertView.setTag(mViewHolder);
        }
        else{
            mViewHolder = (ViewHolder) convertView.getTag();
        }


        mViewHolder.bookTitleTextView.setText(books.get(position).getTitle());
        if (books.get(position).getArtwork() == null){
            mViewHolder.bookCoverImageView.setImageResource(R.mipmap.ic_launcher);
        }else{
            mViewHolder.bookCoverImageView.setImageBitmap(books.get(position).getArtwork());
        }
        return convertView;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        Log.i(MainTabbedActivity.TAG, "GridViewAdapter - notifyDataSetChanged");

    }

    static class ViewHolder{
        private TextView bookTitleTextView;
        private ImageView bookCoverImageView;
    }
}