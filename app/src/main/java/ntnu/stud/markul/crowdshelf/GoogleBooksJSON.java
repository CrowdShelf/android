package ntnu.stud.markul.crowdshelf;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by markuslund92 on 01.09.15.
 */
public class GoogleBooksJSON {
    private String kind;
    private Integer totalItems;
    private ArrayList<com.google.gson.internal.LinkedTreeMap<String ,com.google.gson.internal.LinkedTreeMap>> items;


    @Override
    public String toString() {
        return "Kind: " + kind + " TOTALITEMS: " + totalItems + " ITEMS: " + items.get(0).getClass().getName();
    }
}
