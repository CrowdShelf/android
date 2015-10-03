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
    //use the following annotation and declare an ActivityTestRule for your activity under test
    @Rule
    public ActivityTestRule<MainTabbedActivity> mActivityRule = new ActivityTestRule(MainTabbedActivity.class);

    //use @Before to setup your test fixture
    @Before
    public void setUp() {
    }


    //release resources by using
    @After
    public void tearDown() {  }
}