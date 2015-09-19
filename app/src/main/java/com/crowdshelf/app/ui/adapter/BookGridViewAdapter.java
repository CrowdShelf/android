package com.crowdshelf.app.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.crowdshelf.app.bookInfo.BookInfo;
import com.crowdshelf.app.models.Book;
import com.crowdshelf.app.network.GetBookPreviewInfoAsync;
import com.crowdshelf.app.ui.activities.MainTabbedActivity;

import java.util.List;

import ntnu.stud.markul.crowdshelf.R;

/**
 * Created by markuslund92 on 19.09.15.
 */
public class BookGridViewAdapter extends BaseAdapter {
    private Context mContext;
    private List<Book> mItems;

    public BookGridViewAdapter(Context context, List<Book> items) {
        mContext = context;
        mItems = items;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if(convertView == null) {
            // inflate the GridView item layout
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.gridview_book, parent, false);

            // initialize the view holder
            viewHolder = new ViewHolder();
            viewHolder.bookCoverImageView = (ImageView) convertView.findViewById(R.id.book_cover_imageView);
            viewHolder.bookTitleTextView = (TextView) convertView.findViewById(R.id.book_title_TextView);
            convertView.setTag(viewHolder);
        } else {
            // recycle the already inflated view
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // update the item view
        Book item = mItems.get(position);
        BookInfo bookInfo = item.getBookInfo();
        //TODO: Wait until bookInfo is initialized async
        if (bookInfo == null){
            //This is only a backup solution that works...
            new GetBookPreviewInfoAsync(item.getIsbn(), viewHolder.bookTitleTextView, viewHolder.bookCoverImageView, null).execute();
        }
        else {
            viewHolder.bookCoverImageView.setImageBitmap(item.getBookInfo().getArtwork());
            viewHolder.bookTitleTextView.setText(item.getBookInfo().getTitle());
        }


        return convertView;
    }

    private static class ViewHolder {
        ImageView bookCoverImageView;
        TextView bookTitleTextView;
    }
}