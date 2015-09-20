package com.crowdshelf.app.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import ntnu.stud.markul.crowdshelf.R;

/**
 * Created by Torstein on 20.09.2015.
 */
public class RealmActivity extends Activity {

    private Realm realm;

    @Override
     protected void onCreate(Bundle savedInstanceState) {
        Log.d("NETDBTEST", "Create default Realm Configuration");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this).build();
        Realm.deleteRealm(realmConfiguration); // Clean slate
        Realm.setDefaultConfiguration(realmConfiguration); // Make this Realm the default
    }

    @Override
     protected void onDestroy() {
        super.onDestroy();
    }
}