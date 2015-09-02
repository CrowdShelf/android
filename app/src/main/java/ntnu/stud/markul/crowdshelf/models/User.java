package ntnu.stud.markul.crowdshelf.models;

import java.util.ArrayList;

/**
 * Created by Torstein on 01.09.2015.
 */
public class User {
    private String name;
    private Shelf shelf;
    private ArrayList<Crowd> memberOf = new ArrayList<Crowd>();

    public User(String name, Shelf shelf) {
        this.name = name;
        this.shelf = shelf;
    }

    public User(String name) {
        this.name = name;
    }

    public void setShelf(Shelf shelf) {
        this.shelf = shelf;
    }

    public Shelf getShelf() {
        return shelf;
    }

    public String getName() {
        return name;
    }
}
