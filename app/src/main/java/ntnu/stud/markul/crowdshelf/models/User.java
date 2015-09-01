package ntnu.stud.markul.crowdshelf.models;

/**
 * Created by Torstein on 01.09.2015.
 */
public class User {
    private String name;
    private Shelf shelf;

    public User(String name, Shelf shelf) {
        this.name = name;
        this.shelf = shelf;
    }

    public Shelf getShelf() {
        return shelf;
    }
}
