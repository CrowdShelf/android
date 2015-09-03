package com.crowdshelf.app; /**
 * Created by markuslund92 on 01.09.15.
 */
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import ntnu.stud.markul.crowdshelf.R;

public class BookGrid extends BaseAdapter{
    private Context mContext;
    private final String[] web;
    private final String[] Imageid;

    public BookGrid(Context c,String[] web,String[] Imageid ) {
        mContext = c;
        this.Imageid = Imageid;
        this.web = web;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return web.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            grid = new View(mContext);
            grid = inflater.inflate(R.layout.book_single, null);
            TextView textView = (TextView) grid.findViewById(R.id.book_text);
            ImageView imageView = (ImageView)grid.findViewById(R.id.book_image);
            textView.setText(web[position]);
            new ImageLoadTask(Imageid[position], imageView).execute();
        } else {
            grid = (View) convertView;
        }

        return grid;
    }
}