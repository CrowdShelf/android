package com.crowdshelf.app.bookInfo;

import android.util.Log;

import com.crowdshelf.app.network.GetBookInfoAsyncTask;
import com.crowdshelf.app.ui.activities.MainTabbedActivity;

import java.util.HashMap;

/**
 * Created by markuslund92 on 01.09.15.
 */
public class BookInfoGetter {
    private static HashMap<String, BookInfo> bookInfoMap = new HashMap<String, BookInfo>(); // KEY: ISBN
    // The info is stored in a hashmap that is used by all individual books to avoid duplicate images etc.

    private static void downloadBookInfo(String isbn) {
        BookInfo bookInfo = null;
        new GetBookInfoAsyncTask(isbn, bookInfo).execute();
        bookInfoMap.put(isbn, bookInfo);
    }

    public static BookInfo getBookInfo(String isbn) {
        Log.i(MainTabbedActivity.TAG, "BookInfoGetter - getBookInfo");
        if (!bookInfoMap.containsKey(isbn)) {
            Log.i(MainTabbedActivity.TAG, "BookInfoGetter - getBookInfo - isbnNotInHashMap");
            downloadBookInfo(isbn);
        }
        return bookInfoMap.get(isbn);
    }

}
