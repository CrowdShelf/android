package ntnu.stud.markul.crowdshelf.models;

import java.util.ArrayList;

/**
 * Created by Torstein on 01.09.2015.
 */
public class Book{
    private User owner;
    private ArrayList<User> rentedTo;
    private int numberOfCopies;
    private BookInfo bookInfo;
    private boolean availableForRent = true;

    public Book(BookInfo bookInfo, User owner, ArrayList<User> rentedTo, int numberOfCopies) {
        this.bookInfo = bookInfo;
        this.owner = owner;
        this.rentedTo = rentedTo;
        this.numberOfCopies = numberOfCopies;

        owner.getShelf().addBook(this);
    }

    public ArrayList<User> getRentedTo() {
        return rentedTo;
    }

    public User getOwner() {
        return owner;
    }

    public boolean isAvailableForRent() {
        return availableForRent;
    }

    public void setAvailableForRent(boolean availableForRent) {
        this.availableForRent = availableForRent;
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

    public BookInfo getBookInfo() {
        return bookInfo;
    }

    public boolean equals(Book book)
    {
        if (bookInfo.getIsbn() == book.getBookInfo().getIsbn())
        {
            return true;
        } else {
            return false;
        }
    }
}
