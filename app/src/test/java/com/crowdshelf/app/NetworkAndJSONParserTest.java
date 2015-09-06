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
    Book bookActual;
    Book bookExpected;
    Book newBookActual;
    Book newBookExpected;
    User userActual;
    User userExpected;
    Crowd crowdActual;
    Crowd crowdExpected;

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
        bookExpected.set_id("55eb61c7b939d9110027e527");
        bookExpected.setIsbn("1231313");
        bookExpected.setOwner("torstein");
        ArrayList<String> rentedTo = new ArrayList<String>();
        rentedTo.add("øyvind");
        rentedTo.add("esso");
        bookExpected.setRentedTo(rentedTo);
        bookExpected.setNumberOfCopies(5);
        bookExpected.setNumAvailableForRent(4);

        crowdExpected = new Crowd();
        crowdExpected.set_id("55e9d5f1e4b003e0910cba58");
        crowdExpected.setName("The best crowd");
        crowdExpected.setOwner("esso");
        ArrayList<String> members = new ArrayList<String>();
        crowdExpected.setMembers(members);

        /*
        newBookExpected = new Book();
        newBookExpected.setIsbn("123321");
        newBookExpected.setOwner("torstein");
        newBookExpected.setNumberOfCopies(5);
        newBookExpected.setNumAvailableForRent(4);
        */
    }

    @Test
    public void testGetUser() throws Exception {
        userActual = mC.getUser("torstein");
        Assert.assertEquals("\nUser: ", userExpected, userActual);

        // Verify that the books contained in the User object is retrieved and stored properly
        bookActual = mC.getBookById("55eb61c7b939d9110027e527");
        bookExpected.toString();
        bookActual.toString();
        Assert.assertEquals("\nBooks in user object: ", bookExpected, bookActual);
    }

    @Test
    public void testGetCrowd() throws Exception {
        //crowdActual = mC.getCrowd("55e9d5f1e4b003e0910cba58");
        //Assert.assertEquals(crowdExpected, crowdActual);
    }

    @Test
    public void testCreateBook() throws Exception {
        nC.createBook(newBookExpected);
        for (Book b : mC.getBooksByIsbn(newBookExpected.getIsbn())) {
            if (b.getOwner().getName().equals(newBookExpected.getOwner().getName())) {
                newBookActual = b;
            }
        }
        Assert.assertEquals(newBookExpected, newBookActual);
    }

    @Test
    public void testGetBook() throws Exception {

    }

    @Test
    public void testAddRenter() throws Exception{
    }

    @Test
    public  void testRemoveRenter() throws Exception {
    }

    @Test
    public void testCreateCrowd() throws Exception {
    }



    @Test
    public void testAddCrowdMember() throws Exception {
    }

    @Test
    public void testRemoveCrowdMember() throws Exception {
    }

    @Test
    public void testCreateUser() throws Exception {

    }

}
