package com.crowdshelf.app;

import android.os.Build;

import com.crowdshelf.app.io.network.serializers.BookSerializer;
import com.crowdshelf.app.models.Book;
import com.crowdshelf.app.models.Crowd;
import com.crowdshelf.app.models.User;
import com.crowdshelf.app.io.network.NetworkController;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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

        book1 = new Book();
        book1.setIsbn("141414");
        book1.setOwner("131313");
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Book.class, new BookSerializer())
                .create();
        String jsonString = gson.toJson(book1, Book.class);
        System.out.print(jsonString);
    }

    @Test
    public void test() {

    }
}
