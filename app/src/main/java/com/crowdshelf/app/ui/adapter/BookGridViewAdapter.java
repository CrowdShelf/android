package com.crowdshelf.app.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.crowdshelf.app.models.BookInfo;

import java.util.List;

import ntnu.stud.markul.crowdshelf.R;

/**
 * Created by markuslund92 on 19.09.15.
 */
public class BookGridViewAdapter extends BaseAdapter {
    private Context mContext;
    private List<BookInfo> mItems;

    public BookGridViewAdapter(Context context, List<BookInfo> items) {
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
        BookInfo bookInfo = mItems.get(position);
//        BookInfo bookInfo = item.getBookInfo();
        //TODO: Wait until bookInfo is initialized async
        Bitmap bitmap = BitmapFactory.decodeByteArray(bookInfo.getArtworkByteArray(), 0, bookInfo.getArtworkByteArray().length);
        viewHolder.bookCoverImageView.setImageBitmap(bitmap);
        viewHolder.bookTitleTextView.setText(bookInfo.getTitle());
        return convertView;
    }

    private static class ViewHolder {
        ImageView bookCoverImageView;
        TextView bookTitleTextView;
    }
}