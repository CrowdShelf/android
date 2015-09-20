package com.crowdshelf.app;

import android.app.Activity;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import com.crowdshelf.app.activities.MainActivity;
import com.crowdshelf.app.network.NetworkController;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class NetworkAndDBInstrumentationTest extends ActivityInstrumentationTestCase2<MainActivity> {
    Activity mActivity;
    public NetworkAndDBInstrumentationTest() {
        super(MainActivity.class);
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();

        // Injecting the Instrumentation instance is required
        // for your test to run with AndroidJUnitRunner.
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        mActivity = getActivity();
    }

    @Test
    public void typeOperandsAndPerformAddOperation() {
        NetworkController.getCrowd("55f01f29f0a5fad2120bb1db");
        System.out.print("ok");
        Log.d("1TEST", "1TEST");
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }
}