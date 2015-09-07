package com.crowdshelf.app.bookInfo;

import com.crowdshelf.app.network.GetBookInfoAsyncTask;

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
        if (!bookInfoMap.containsKey(isbn)) {
            downloadBookInfo(isbn);
        }
        return bookInfoMap.get(isbn);
    }

}
