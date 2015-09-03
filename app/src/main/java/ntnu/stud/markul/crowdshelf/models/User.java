package ntnu.stud.markul.crowdshelf.models;

import java.util.ArrayList;

import ntnu.stud.markul.crowdshelf.MainController;

/**
 * Created by Torstein on 01.09.2015.
 */
public class User {
    private String name;
    private ArrayList<Book> booksOwned;
    private ArrayList<Book> booksRented;
    private ArrayList<String> crowds; // crowd _id
    //private Shelf shelf;

    // todo rework
    private void setShelf(ArrayList<String> books) {
        ArrayList<Book> shelfBooks = new ArrayList<Book>();
        for(String _id : books) {
            shelfBooks.add(MainController.getBook(_id));
        }
        //this.shelf = new Shelf(shelfBooks);
    }

    public ArrayList<Crowd> getCrowds() {
        return MainController.getCrowds(crowds);
    }

    public String getName() {
        return name;
    }
}
