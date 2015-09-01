package ntnu.stud.markul.crowdshelf.models;

import java.util.ArrayList;

/**
 * Created by Torstein on 01.09.2015.
 */
public class UserBook extends Book{
    private User owner;
    private ArrayList<User> rentedTo;
    private int numberOfCopies;

    public UserBook(String isbn, User owner, ArrayList<User> rentedTo, int numberOfCopies) {
        super(isbn);
        this.owner = owner;
        this.rentedTo = rentedTo;
        this.numberOfCopies = numberOfCopies;

        owner.getShelf().addBook(this);
    }

    public void rentOut(User user) {
        //TODO send shit to server
        if (!rentedTo.contains(user)) {
            rentedTo.add(user);
        }
    }

    public void takeInReturn(User user) {
        // TODO send shit to server
        rentedTo.remove(user);
    }

    public int getNumberOfCopies() {
        return numberOfCopies;
    }

    public void setNumberOfCopies(int numberOfCopies) {
        this.numberOfCopies = numberOfCopies;
    }
}
