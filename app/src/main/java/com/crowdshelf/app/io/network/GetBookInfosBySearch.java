package com.crowdshelf.app.io.network;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.crowdshelf.app.io.DbEvent;
import com.crowdshelf.app.io.DbEventType;
import com.crowdshelf.app.models.BookInfo;
import com.crowdshelf.app.models.googleBookInfo.GoogleBooksIndustryIdentifier;
import com.crowdshelf.app.models.googleBookInfo.GoogleBooksItem;
import com.crowdshelf.app.models.googleBookInfo.GoogleBooksMain;
import com.crowdshelf.app.models.googleBookInfo.GoogleBooksVolumeInfo;
import com.crowdshelf.app.ui.activities.MainTabbedActivity;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

/**
 * Created by markuslund92 on 07.09.15.
 */
public class GetBookInfosBySearch {
    private static String googleBooksAPIUrl = "https://www.googleapis.com/books/v1/volumes?q=";
    private static final String TAG = "GetBookInfosBySearch";

    public static void getBookInfos(final String query, final DbEventType dbEventType) {

        new AsyncTask<Void, Void, List<BookInfo>>() {
            @Override
            protected List<BookInfo> doInBackground(Void... params) {
                try {
                    query.replace(" ", "+");
                    URL url = new URL(googleBooksAPIUrl + query);
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
                    List<BookInfo> bookInfos = new ArrayList<BookInfo>();

                    for (GoogleBooksItem item: main.getItems()) {
                        GoogleBooksVolumeInfo info = item.getVolumeInfo();
                        String isbn = "";
                        for (GoogleBooksIndustryIdentifier industryIdentifier: info.getIndustryIdentifiers()) {
                            if (industryIdentifier.getType().equals("ISBN_10")) {
                                isbn = industryIdentifier.getIdentifier();
                            }
                        }
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

                        bookInfos.add(new BookInfo(isbn, title, subtitle, author, publisher, pubDate, artworkByteArray, description));
                    }

                   return bookInfos;
                } catch (Exception e) {
                    Log.w(TAG, e.getMessage());
                }
                return null;
            }

            @Override
            protected void onPostExecute(List<BookInfo> results) {
                for (BookInfo result: results) {
                    putBookInfoInDatabase(result, dbEventType);
                }
            }
        }.execute();
    }

    private static void putBookInfoInDatabase(BookInfo bookInfo, DbEventType dbEventType) {
        if (bookInfo == null){
            return;
        }
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(bookInfo);
        realm.commitTransaction();
        realm.close();

        MainTabbedActivity.getBus().post(new DbEvent(dbEventType, bookInfo.getIsbn()));
        Log.i(TAG, "Added BookInfo for ISBN: " + bookInfo.getIsbn());
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