package com.crowdshelf.app;

import android.os.Build;

import com.crowdshelf.app.models.Book;
import com.crowdshelf.app.models.Crowd;
import com.crowdshelf.app.models.User;
import com.crowdshelf.app.io.network.NetworkController;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import ntnu.stud.markul.crowdshelf.BuildConfig;

/**
 * Created by Torstein on 04.09.2015.
 */
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
@RunWith(RobolectricGradleTestRunner.class)
public class NetworkAndJSONParserTest {
    NetworkController nC;
    MainController mC;
    Book bookActual, bookExpected, newBookActual, newBookExpected, book1, book2;
    User userActual, userExpected, userRenter, user1;
    Crowd crowdActual, crowdExpected;

    @Before
    public void setup() {
        nC = new NetworkController();
        mC = new MainController();

        /*

        bookExpected = new Book();
        bookExpected.setId("55f01fdef0a5fad2120bb1dc");
        bookExpected.setIsbn("23981038");
        bookExpected.setOwner("esso");
        ArrayList<String> rentedTo = new ArrayList<String>();
        rentedTo.add("torstein");
        bookExpected.setRentedTo(rentedTo);
        bookExpected.setNumberOfCopies(5);
        bookExpected.setNumAvailableForRent(3);


        newBookExpected = new Book();
        newBookExpected.setIsbn("123321");
        newBookExpected.setOwner("torstein");
        newBookExpected.setNumberOfCopies(5);
        newBookExpected.setNumAvailableForRent(4);


        userRenter = new User();
        userRenter.setUsername("katja");
        */
    }

    @Test
    public void testMain() throws Exception {
        testGetCrowds();
    }

    public void testGetCrowds() throws Exception {
        userExpected = new User();
        userExpected.setUsername("esso");
        List<Book> booksOwned = new ArrayList<Book>();
        List<Book> booksRented = new ArrayList<Book>();
        List<String> crowds = new ArrayList<String>();
        crowds.add("55f01f29f0a5fad2120bb1db");

        book1 = new Book();
        book1.setId("55f0661d4384b31100c056a6");
        book1.setIsbn("1231313");
        book1.setOwner("morten");

        book2 = new Book();
        book2.setId("55f01fdef0a5fad2120bb1dc");
        book2.setIsbn("23981038");
        book2.setOwner("esso");
        ArrayList<String> rentedTo2 = new ArrayList<String>();
        rentedTo2.add("torstein");

        booksRented.add(book1);

        crowdExpected = new Crowd();
        crowdExpected.setId("55f01f29f0a5fad2120bb1db");
        crowdExpected.setName("herpaderp");
        crowdExpected.setOwner("esso");
        List<User> members = new ArrayList<User>();
        members.add(userExpected);

        mC.retrieveCrowd("55f01f29f0a5fad2120bb1db"); // Download crowd from server
        crowdActual = MainController.getCrowd("55f01f29f0a5fad2120bb1db"); // Get the one stored locally
        Assert.assertEquals(crowdExpected, crowdActual);
    }

    public void testCreateBook() throws Exception {
        nC.createBook(newBookExpected);
        /*
        for (Book b : mC.getBooksByIsbnOwnedByYourCrowds(newBookExpected.getIsbn())) {
            if (b.getOwner().getUsername().equals(newBookExpected.getOwner().getUsername())) {
                newBookActual = b;
            }
        }
        */
        Assert.assertEquals(newBookExpected, newBookActual);
    }

    public void testCreateCrowd() throws Exception {
    }

    public void testAddCrowdMember() throws Exception {
    }

    public void testRemoveCrowdMember() throws Exception {
    }

    public void testCreateUser() throws Exception {

    }

}
