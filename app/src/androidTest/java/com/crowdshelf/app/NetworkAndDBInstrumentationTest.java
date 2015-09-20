package com.crowdshelf.app;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.ApplicationTestCase;
import android.test.suitebuilder.annotation.LargeTest;
import android.util.Log;

import com.crowdshelf.app.activities.MainActivity;
import com.crowdshelf.app.activities.RealmActivity;
import com.crowdshelf.app.models.Crowd;
import com.crowdshelf.app.network.NetworkController;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.realm.Realm;
import io.realm.RealmResults;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class NetworkAndDBInstrumentationTest {
    private Realm realm;
    //use the following annotation and declare an ActivityTestRule for your activity under test
    @Rule
    public ActivityTestRule<RealmActivity> mActivityRule = new ActivityTestRule(RealmActivity.class);

    //use @Before to setup your test fixture
    @Before
    public void setUp() { realm = Realm.getDefaultInstance(); }

    //annotate all test methods with
    @Test
    public void testGetCrowd() {
        Log.d("NETDBTEST", "testGetCrowd");
        NetworkController.getCrowds();

        realm.beginTransaction();
        RealmResults<Crowd> results = realm.allObjects(Crowd.class);
        Log.d("NETDBTEST", "Number of results: " + String.valueOf(results.size()) );
        for (Crowd c : results) {
            Log.d("NETDBTEST", "Crowd name" + c.getName());
        }
        realm.commitTransaction();
        Assert.assertEquals(1, 1);
    }

    //release resources by using
    @After
    public void tearDown() { realm.close(); }
}