package com.crowdshelf.app;

import android.os.Build;

import com.crowdshelf.app.NetworkController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import ntnu.stud.markul.crowdshelf.BuildConfig;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
@RunWith(RobolectricGradleTestRunner.class)
public class NetworkHelperTest {

    @Test
    public void testSendPostRequest() throws Exception {
        /*
        {"_id":"55e6dcfff3f1beaf21673c8e","name":"The best crowd","owner":"esso","members":["esso","torstein"]}
         */

        //String jsonString = {"_id":"55e6dcfff3f1beaf21673c8e","name":"The best crowd","owner":"esso","members":["esso","torstein"]}
        String crowdID = "55e6dcfff3f1beaf21673c8e";
        String userName = "esso";
        //NetworkController.getUser(userName);
        //NetworkController.getCrowd(crowdID);

        String isbn = "2139803";
        NetworkController.getBook(userName, isbn);
    }

    public void testSendPutRequest() throws Exception {

    }

    public void testSendGetRequest() throws Exception {

    }

    public void testHandleJsonResponse() throws Exception {

    }
}