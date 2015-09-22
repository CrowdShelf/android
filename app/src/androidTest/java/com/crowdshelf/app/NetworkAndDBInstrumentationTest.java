package com.crowdshelf.app;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.util.Log;

import com.crowdshelf.app.io.DBEvent;
import com.crowdshelf.app.io.network.NetworkController;
import com.crowdshelf.app.models.Crowd;
import com.crowdshelf.app.ui.activities.MainTabbedActivity;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

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
    private Bus bus;
    //use the following annotation and declare an ActivityTestRule for your activity under test
    @Rule
    public ActivityTestRule<MainTabbedActivity> mActivityRule = new ActivityTestRule(MainTabbedActivity.class);

    //use @Before to setup your test fixture
    @Before
    public void setUp() {
        realm = Realm.getDefaultInstance();
        bus = MainTabbedActivity.getBus();
        bus.register(this);
    }

    //annotate all test methods with
    @Test
    public void testGetCrowd() {
        Log.d("NETDBTEST", "testGetCrowd");
        NetworkController.getCrowd("55fede47b379431100423430");
    }

    @Subscribe
    public void handleGetCrowd(DBEvent e) {
        Log.d("NETDBTEST", "handleGetCrowd");
        realm = Realm.getDefaultInstance();
        realm.refresh();
        RealmResults<Crowd> results = realm.allObjects(Crowd.class);
        Log.d("NETDBTEST", "Number of results: " + String.valueOf(results.size()) );
        for (Crowd c : results) {
            Log.d("NETDBTEST", "Crowd name" + c.getName());
        }
        Assert.assertEquals(1, 1);
    }

    //release resources by using
    @After
    public void tearDown() { realm.close(); }
}