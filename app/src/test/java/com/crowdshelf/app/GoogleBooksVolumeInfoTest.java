package com.crowdshelf.app;

import android.os.Build;

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
    }


}
