package com.crowdshelf.app.network;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.crowdshelf.app.HelperMethods;
import com.crowdshelf.app.bookInfo.BookInfo;
import com.crowdshelf.app.bookInfo.GoogleBooksMain;
import com.crowdshelf.app.bookInfo.GoogleBooksVolumeInfo;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by markuslund92 on 07.09.15.
 */
public class GetBookInfoAsyncTask extends AsyncTask<Void, Void, BookInfo> {

    private BookInfo bookInfo;
    private String isbn;

    public GetBookInfoAsyncTask(String isbn, BookInfo bookInfo) {
        this.bookInfo = bookInfo;
        this.isbn = isbn;
    }

    @Override
    protected BookInfo doInBackground(Void... params) {
        try {
            String json = HelperMethods.getJsonFromGoogleBooksApiUsingISBN(isbn);
            GoogleBooksMain main = HelperMethods.convertGoogleBooksJsonStringToObject(json);

            assert main != null;
            if (main.getTotalItems() > 0){

                GoogleBooksVolumeInfo info = main.getItems().get(0).getVolumeInfo();
                String title = info.getTitle();
                String subtitle = info.getSubTitle();
                String author = HelperMethods.getAuthorsAsString(info.getAuthors());
                String publisher = info.getPublisher();
                String pubDate = info.getPublishedDate();
                Bitmap artwork = downloadArtworkFromUrl(info.getImageLinks().getThumbnail());

                return new BookInfo(isbn, title, subtitle, author, publisher, pubDate, artwork);
            }else{
                //TODO: Do something if books does not exist in google books
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(BookInfo result) {
        super.onPostExecute(result);
        this.bookInfo = result;
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