package com.crowdshelf.app.network;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.crowdshelf.app.models.BookInfo;
import com.crowdshelf.app.ui.activities.MainTabbedActivity;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by markuslund92 on 07.09.15.
 */
public class GetBookPreviewInfoAsync extends AsyncTask<Void, Void, BookInfo> {


    private final String isbn;
    private final TextView titleTextView;
    private final ImageView imageView;
    private final TextView infoTextView;

    public GetBookPreviewInfoAsync(String isbn, TextView titleTextView, ImageView imageView, TextView infoTextView) {
        this.isbn = isbn;
        this.titleTextView = titleTextView;
        this.imageView = imageView;
        this.infoTextView = infoTextView;
    }

    @Override
    protected BookInfo doInBackground(Void... params) {
//        try {
//            String json = HelperMethods.getJsonFromGoogleBooksApiUsingISBN(isbn);
//            GoogleBooksMain main = HelperMethods.convertGoogleBooksJsonStringToObject(json);
//
//            assert main != null;
//            if (main.getTotalItems() > 0){
//
//                GoogleBooksVolumeInfo info = main.getItems().get(0).getVolumeInfo();
//                String title = info.getTitle();
//                String subtitle = info.getSubTitle();
//                String author = HelperMethods.getAuthorsAsString(info.getAuthors());
//                String publisher = info.getPublisher();
//                String pubDate = info.getPublishedDate();
//                Bitmap artwork = downloadArtworkFromUrl(info.getImageLinks().getThumbnail());
//                String description = info.getDescription();
//
//                return new BookInfo(isbn, title, subtitle, author, publisher, pubDate, artwork, description);
//            }else{
//                //TODO: Do something if books does not exist in google books
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return null;
    }

    @Override
    protected void onPostExecute(BookInfo result) {
        super.onPostExecute(result);
        Log.i(MainTabbedActivity.TAG, "GetBookPreviewInfoAsync-onPostExecute result title:" + result.getTitle());
        Bitmap bitmap = BitmapFactory.decodeByteArray(result.getArtworkByteArray() , 0, result.getArtworkByteArray().length);
        this.imageView.setImageBitmap(bitmap);
        this.titleTextView.setText(result.getTitle());
        if (this.infoTextView != null){
            this.infoTextView.setText(result.getDescription());
        }
    }

    private Bitmap downloadArtworkFromUrl(String url) {
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



}