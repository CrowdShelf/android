package com.crowdshelf.app;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.crowdshelf.app.ui.activities.MainTabbedActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;

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