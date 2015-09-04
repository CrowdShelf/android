package com.crowdshelf.app;

import android.os.Build;

import com.crowdshelf.app.models.Book;
import com.crowdshelf.app.models.Crowd;
import com.crowdshelf.app.models.User;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.*;

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
        userExpected.setUsername("esso");
        ArrayList<String> booksRented = new ArrayList<String>();
        booksRented.add("55e6ff461b24e45b3e778286");
        ArrayList<String> booksOwned = new ArrayList<String>();
        booksOwned.add("55e42cf9ac182ab84eb1cc8e");
        booksOwned.add("55e4309907d15611002188d8");
        userExpected.setBooksOwned(booksOwned);
        userExpected.setBooksRented(booksRented);

        bookExpected = new Book();
        bookExpected.set_id("55e6ff461b24e45b3e778286");
        bookExpected.setIsbn("1231313");
        bookExpected.setOwner("torstein");
        ArrayList<String> rentedTo = new ArrayList<String>();
        rentedTo.add("øyvind");
        rentedTo.add("esso");
        bookExpected.setNumberOfCopies(5);
        bookExpected.setAvailableForRent(4);

        crowdExpected = new Crowd();
        crowdExpected.set_id("55e6dcfff3f1beaf21673c8e");
        crowdExpected.setName("The best crowd");
        crowdExpected.setOwner("esso");
        ArrayList<String> members = new ArrayList<String>();
        rentedTo.add("esso");
        rentedTo.add("torstein");
        crowdExpected.setMembers(members);

        newBookExpected.setIsbn("123321");
        newBookExpected.setOwner("esso");
        newBookExpected.setNumberOfCopies(4);
        newBookExpected.setAvailableForRent(4);
    }

    @Test
    public void testGetUser() throws Exception {
        userActual = mC.getUser("esso");
        Assert.assertEquals(userExpected, userActual);
    }

    @Test
    // Test that the books contained within a received user object matches the created book objects
    public void testGetBookWithUser() throws Exception {
        bookActual = mC.getBook("55e6ff461b24e45b3e778286");
        Assert.assertEquals(bookExpected, bookActual);
    }

    @Test
    public void testGetCrowd() throws Exception {
        crowdActual = mC.getCrowd("55e6dcfff3f1beaf21673c8e");
        Assert.assertEquals(crowdExpected, crowdActual);
    }

    @Test
    public void testCreateBook() throws Exception {
        nC.createBook(newBookExpected);
        for (Book b : mC.getBooksByISBN(newBookExpected.getIsbn())) {
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
