package com.crowdshelf.app.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.crowdshelf.app.MainController;
import com.crowdshelf.app.io.DBEvent;
import com.crowdshelf.app.io.DBEventType;
import com.crowdshelf.app.models.Book;
import com.crowdshelf.app.models.BookInfo;
import com.crowdshelf.app.ui.activities.MainTabbedActivity;
import com.squareup.otto.Subscribe;

import java.util.List;

import io.realm.Realm;
import ntnu.stud.markul.crowdshelf.R;

/**
 * Created by markuslund92 on 19.09.15.
 */
public class BookGridViewAdapter extends BaseAdapter {
    private static final String TAG = "BookGridViewAdapter";
    private Context mContext;
    private List<Book> mItems;
    private Realm realm;

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
            viewHolder.progressBar = (ProgressBar) convertView.findViewById(R.id.progressBar);
            convertView.setTag(viewHolder);
        } else {
            // recycle the already inflated view
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Book book = mItems.get(position);
        realm = Realm.getDefaultInstance();
        BookInfo bookInfo = realm.where(BookInfo.class)
                .equalTo("isbn", book.getIsbn())
                .findFirst();
        realm.close();
        if (bookInfo == null) {
            viewHolder.progressBar.setVisibility(View.VISIBLE);
            viewHolder.bookCoverImageView.setVisibility(View.INVISIBLE);
            viewHolder.bookTitleTextView.setVisibility(View.INVISIBLE);
        } else {
            Bitmap bitmap = BitmapFactory.decodeByteArray(bookInfo.getArtworkByteArray(), 0, bookInfo.getArtworkByteArray().length);
            viewHolder.bookCoverImageView.setImageBitmap(bitmap);
            viewHolder.progressBar.setVisibility(View.INVISIBLE);
            viewHolder.bookCoverImageView.setVisibility(View.VISIBLE);
            viewHolder.bookTitleTextView.setText(bookInfo.getTitle());
            viewHolder.bookTitleTextView.setVisibility(View.VISIBLE);

        }

        return convertView;
    }


    private static class ViewHolder {
        ImageView bookCoverImageView;
        TextView bookTitleTextView;
        ProgressBar progressBar;
    }
}