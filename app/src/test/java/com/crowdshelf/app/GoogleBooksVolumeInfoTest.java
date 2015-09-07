package com.crowdshelf.app;

import android.os.Build;

import com.crowdshelf.app.bookInfo.GoogleBooksVolumeInfo;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import ntnu.stud.markul.crowdshelf.BuildConfig;

/**
 * Created by markuslund92 on 07.09.15.
 */
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
@RunWith(RobolectricGradleTestRunner.class)
public class GoogleBooksVolumeInfoTest {

    @Test
    public void getAuthorsAsString_returnsStringWithAuthors_ListOfAuthors(){
        GoogleBooksVolumeInfo i = new GoogleBooksVolumeInfo();
        String[] authors1 = {"Forfatter", "Formutter", "Harry Bjarne"};
        Assert.assertEquals("Forfatter, Formutter, Harry Bjarne", HelperMethods.getAuthorsAsString(authors1));
        String[] authors2 = {"Forfatter Formutter"};
        Assert.assertEquals("Forfatter Formutter", HelperMethods.getAuthorsAsString(authors2));
    }


}
