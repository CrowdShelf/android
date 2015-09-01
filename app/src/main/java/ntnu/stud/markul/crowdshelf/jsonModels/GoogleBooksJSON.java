package ntnu.stud.markul.crowdshelf.jsonModels;

import java.util.ArrayList;

/**
 * Created by markuslund92 on 01.09.15.
 */
public class GoogleBooksJSON {
    private Integer totalItems;
    private ArrayList<GoogleBooksItem> items;

    public ArrayList<GoogleBooksItem> getItems(){
        return items;
    }
}
