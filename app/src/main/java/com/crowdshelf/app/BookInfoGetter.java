package com.crowdshelf.app;

import android.media.Image;

import com.crowdshelf.app.models.BookInfo;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by markuslund92 on 01.09.15.
 */
public class BookInfoGetter {
    private static HashMap<String, BookInfo> bookInfoMap = new HashMap<String, BookInfo>();
    private String kind;
    private Integer totalItems;
    private ArrayList<com.google.gson.internal.LinkedTreeMap<String, com.google.gson.internal.LinkedTreeMap>> items;
    // The info is stored in a hashmap that is used by all individual books to avoid duplicate images etc.

    private static void downloadBookInfo(String isbn) {
        // TODO get info from google and save to the values below
        String title = "Java for dummies";
        String subtitle = "Learn Java in 4.5 seconds";
        String author = "Torstein Sørnes";
        String publisher = "Sørnes publishing";
        String pubDate = "2015";
        Image artwork = null;
        bookInfoMap.put(isbn, new BookInfo(isbn, title, subtitle, author, publisher, pubDate, artwork));
    }

    public static BookInfo getBookInfo(String isbn) {
        if (!bookInfoMap.containsKey(isbn)) {
            downloadBookInfo(isbn);
        }
        return bookInfoMap.get(isbn);
    }

    @Override
    public String toString() {
        return "Kind: " + kind + " TOTALITEMS: " + totalItems + " ITEMS: " + items.get(0).getClass().getName();
    }
}
