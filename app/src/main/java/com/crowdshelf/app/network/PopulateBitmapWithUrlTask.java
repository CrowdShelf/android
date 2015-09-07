package com.crowdshelf.app.network;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by markuslund92 on 07.09.15.
 */
public class PopulateBitmapWithUrlTask extends AsyncTask<Void, Void, Bitmap> {

    private String url;
    private Bitmap bitmap;

    public PopulateBitmapWithUrlTask(String url, Bitmap bitmap) {
        this.url = url;
        this.bitmap = bitmap;
    }

    @Override
    protected Bitmap doInBackground(Void... params) {
        try {
            URL urlConnection = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) urlConnection
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        super.onPostExecute(result);
        this.bitmap = result;
    }

}
