package com.crowdshelf.app;

import android.os.Build;

import com.crowdshelf.app.models.Book;
import com.crowdshelf.app.models.Crowd;
import com.crowdshelf.app.models.User;
import com.crowdshelf.app.network.NetworkController;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;

import ntnu.stud.markul.crowdshelf.BuildConfig;

/**
 * Created by Torstein on 04.09.2015.
 */
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
@RunWith(RobolectricGradleTestRunner.class)
public class NetworkAndJSONParserTest {
    NetworkController nC;
    MainController mC;
    Book bookActual, bookExpected, newBookActual, newBookExpected;
    User userActual, userExpected, userRenter;
    Crowd crowdActual, crowdExpected;

    @Before
    public void setup() {
        nC = new NetworkController();
        mC = new MainController();

        userExpected = new User();
        userExpected.setUsername("torstein");
        ArrayList<String> booksOwned = new ArrayList<String>();
        ArrayList<String> booksRented = new ArrayList<String>();
        booksOwned.add("55eb61c7b939d9110027e527");
        userExpected.setBooksOwned(booksOwned);
        userExpected.setBooksRented(booksRented);
        ArrayList<String> crowds = new ArrayList<String>();
        userExpected.setCrowds(crowds);

        bookExpected = new Book();
        bookExpected.setId("55eb61c7b939d9110027e527");
        bookExpected.setIsbn("1231313");
        bookExpected.setOwner("torstein");
        ArrayList<String> rentedTo = new ArrayList<String>();
        rentedTo.add("øyvind");
        rentedTo.add("esso");
        bookExpected.setRentedTo(rentedTo);
        bookExpected.setNumberOfCopies(5);
        bookExpected.setNumAvailableForRent(4);

        crowdExpected = new Crowd();
        crowdExpected.setId("55e9d5f1e4b003e0910cba58");
        crowdExpected.setName("The best crowd");
        crowdExpected.setOwner("esso");
        ArrayList<String> members = new ArrayList<String>();
        crowdExpected.setMembers(members);


        newBookExpected = new Book();
        newBookExpected.setIsbn("123321");
        newBookExpected.setOwner("torstein");
        newBookExpected.setNumberOfCopies(5);
        newBookExpected.setNumAvailableForRent(4);


        userRenter = new User();
        userRenter.setUsername("katja");
    }

    @Test
    public void testGetCrowds() throws Exception {
        nC.getCrowds();
    }

    //@Test
    public void testGetUserWithBooksThenAddAndRemoveRenter() throws Exception {
        testGetUser();
        testGetBookFromUser();
        testAddRenter();
        testRemoveRenter();
    }

    public void testGetUser() throws Exception {
        userActual = mC.getUser("torstein");
        Assert.assertEquals("\nUser: ", userExpected, userActual);
    }

    public void testGetBookFromUser() throws Exception {
        // Verify that the books contained in the User object is retrieved and stored properly
        bookActual = mC.getBookById("55eb61c7b939d9110027e527");
        bookExpected.toString();
        bookActual.toString();
        Assert.assertEquals("\nBooks in user object: ", bookExpected, bookActual);
    }

    public void testAddRenter() throws Exception{
        bookActual.addRenter(userRenter.getUsername());
    }

    public  void testRemoveRenter() throws Exception {

    }

    public void testGetBook() throws Exception {

    }

    public void testGetCrowd() throws Exception {
        //crowdActual = mC.getCrowd("55e9d5f1e4b003e0910cba58");
        //Assert.assertEquals(crowdExpected, crowdActual);
    }

    public void testCreateBook() throws Exception {
        nC.createBook(newBookExpected);
        for (Book b : mC.getBooksByIsbn(newBookExpected.getIsbn())) {
            if (b.getOwner().getUsername().equals(newBookExpected.getOwner().getUsername())) {
                newBookActual = b;
            }
        }
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
