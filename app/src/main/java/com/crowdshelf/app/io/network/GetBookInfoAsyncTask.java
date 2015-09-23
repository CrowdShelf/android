package com.crowdshelf.app.io.network;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.crowdshelf.app.bookInfo.GoogleBooksMain;
import com.crowdshelf.app.bookInfo.GoogleBooksVolumeInfo;
import com.crowdshelf.app.io.DBEvent;
import com.crowdshelf.app.io.DBEventType;
import com.crowdshelf.app.models.BookInfo;
import com.crowdshelf.app.ui.activities.MainTabbedActivity;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import io.realm.Realm;

/**
 * Created by markuslund92 on 07.09.15.
 */
public class GetBookInfoAsyncTask {
    private static String googleBooksAPIUrl = "https://www.googleapis.com/books/v1/volumes?q=isbn:";

    public static void getBookInfo(final String isbn, final DBEventType dbEventType) {
        new AsyncTask<Void, Void, BookInfo>() {
            @Override
            protected BookInfo doInBackground(Void... params) {
                try {
                    URL url = new URL(googleBooksAPIUrl + isbn);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");

                    StringBuilder result = new StringBuilder();
                    String line;
                    BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    while ((line = rd.readLine()) != null) {
                        result.append(line);
                    }
                    rd.close();

                    GoogleBooksMain main = new Gson().fromJson(result.toString(), GoogleBooksMain.class);

                    if (main != null && main.getTotalItems() > 0) {
                        GoogleBooksVolumeInfo info = main.getItems().get(0).getVolumeInfo();
                        String title = info.getTitle();
                        String subtitle = info.getSubTitle();
                        String author = getAuthorsAsString(info.getAuthors());
                        String publisher = info.getPublisher();
                        String pubDate = info.getPublishedDate();
                        String description = info.getDescription();
                        URL imgUrl = new URL(info.getImageLinks().getThumbnail());
                        HttpURLConnection connection = (HttpURLConnection) imgUrl.openConnection();
                        connection.setDoInput(true);
                        connection.connect();
                        InputStream input = connection.getInputStream();
                        Bitmap artwork = BitmapFactory.decodeStream(input);

                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        artwork.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        byte[] artworkByteArray = stream.toByteArray();

                        return new BookInfo(isbn, title, subtitle, author, publisher, pubDate, artworkByteArray, description);
                    } else {
                        //TODO: Do something if books does not exist in google books
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(BookInfo result) {
                putBookInfoInDatabase(result, dbEventType);
            }
        }.execute();
    }

    private static void putBookInfoInDatabase(BookInfo bookInfo, DBEventType dbEventType) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(bookInfo);
        realm.commitTransaction();
        realm.close();
        MainTabbedActivity.getBus().post(new DBEvent(dbEventType, bookInfo.getIsbn()));
    }

    public static String getAuthorsAsString(String[] authors) {
        String authorString = "";
        for (int i = 0; i < authors.length; i++) {
            if (i == authors.length-1){
                authorString += authors[i];
            }else{
                authorString += authors[i];
                authorString += ", ";
            }
        }
        return authorString;
    }
}