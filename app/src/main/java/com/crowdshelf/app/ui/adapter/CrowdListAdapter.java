package com.crowdshelf.app.ui.adapter;

import com.crowdshelf.app.MainController;
import com.crowdshelf.app.models.Book;
import com.crowdshelf.app.models.Crowd;
import com.crowdshelf.app.models.MemberId;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import ntnu.stud.markul.crowdshelf.R;

/**
 * Created by markus on 19.10.2015.
 */
public class CrowdListAdapter extends BaseAdapter {
    private static final String TAG = "CrowdListAdapter";
    private Context mContext;
    private List<Crowd> mItems;
    private Realm realm;

    public CrowdListAdapter(Context context, List<Crowd> items) {
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
            convertView = inflater.inflate(R.layout.listitem_crowd, parent, false);

            // initialize the view holder
            viewHolder = new ViewHolder();
            viewHolder.crowdNameTextView = (TextView) convertView.findViewById(R.id.crowdNameTextView);
            viewHolder.crowdPictureImageView = (ImageView) convertView.findViewById(R.id.crowdPictureImageView);
            viewHolder.numBooksInCrowdTextView = (TextView) convertView.findViewById(R.id.numBooks);
            viewHolder.numMembersInCrowdTextView = (TextView) convertView.findViewById(R.id.numMembers);
            convertView.setTag(viewHolder);
        } else {
            // recycle the already inflated view
            viewHolder = (ViewHolder) convertView.getTag();
        }
        realm = Realm.getDefaultInstance();
        Crowd crowd = mItems.get(position);
        List<Book> books = new ArrayList<>();
        for (MemberId u : crowd.getMembers()){
            books.addAll(realm.where(Book.class)
                    .equalTo("owner", u.getId())
                    .findAll());
        }
        realm.close();

        LetterTileProvider letterTileProvider = new LetterTileProvider(mContext);
        final Resources res = mContext.getResources();
        final int tileSize = res.getDimensionPixelSize(R.dimen.letter_tile_size);

        viewHolder.numMembersInCrowdTextView.setText(String.valueOf(crowd.getMembers().size()));
        viewHolder.numBooksInCrowdTextView.setText(String.valueOf(books.size()));
        viewHolder.crowdNameTextView.setText(crowd.getName());
        viewHolder.crowdPictureImageView.setImageBitmap(letterTileProvider.getLetterTile(crowd.getName(), tileSize));

        return convertView;
    }

    private static class ViewHolder {
        ImageView crowdPictureImageView;
        TextView crowdNameTextView;
        TextView numBooksInCrowdTextView;
        TextView numMembersInCrowdTextView;
    }


}
