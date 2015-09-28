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
    private Context mContext;
    private List<Book> mItems;
    private Realm realm;


    public BookGridViewAdapter(Context context, List<Book> items) {
        mContext = context;
        mItems = items;
        realm = Realm.getDefaultInstance();
        MainTabbedActivity.getBus().register(this);
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

        Book book = mItems.get(position);

        MainController.getBookInfo(book.getIsbn(), DBEventType.BOOKINFO_CHANGED);

        BookInfo bookInfo = realm.where(BookInfo.class)
                .equalTo("isbn", book.getIsbn())
                .findFirst();

        if (bookInfo == null) {
            viewHolder.bookCoverImageView.setImageResource(R.mipmap.icon);
            viewHolder.bookTitleTextView.setText("Bookinfo not found");
        } else {
            Bitmap bitmap = BitmapFactory.decodeByteArray(bookInfo.getArtworkByteArray(), 0, bookInfo.getArtworkByteArray().length);
            viewHolder.bookCoverImageView.setImageBitmap(bitmap);
            viewHolder.bookTitleTextView.setText(bookInfo.getTitle());
        }


        return convertView;
    }

    @Subscribe
    public void handleGetBookInfo(DBEvent event) {
        realm.refresh();
        Log.d(MainTabbedActivity.TAG, "BookGridviewAdapter - handleGetBookInfo - event: " + event.getDbEventType());
        switch (event.getDbEventType()) {
            case BOOKINFO_CHANGED:

                break;
        }
    }

    private static class ViewHolder {
        ImageView bookCoverImageView;
        TextView bookTitleTextView;
    }
}