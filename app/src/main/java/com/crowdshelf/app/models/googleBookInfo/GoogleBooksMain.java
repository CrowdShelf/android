package com.crowdshelf.app.models.googleBookInfo;

import java.util.ArrayList;

/**
 * Created by markuslund92 on 01.09.15.
 */
public class GoogleBooksMain {
    private Integer totalItems;
    private ArrayList<GoogleBooksItem> items;

    public ArrayList<GoogleBooksItem> getItems(){
        return items;
    }

    public Integer getTotalItems() {
        return totalItems;
    }
}
