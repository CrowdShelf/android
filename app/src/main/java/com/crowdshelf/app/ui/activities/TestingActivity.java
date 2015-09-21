package com.crowdshelf.app.ui.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.crowdshelf.app.ScannedBookActions;
import com.crowdshelf.app.io.DBEvent;
import com.crowdshelf.app.io.network.NetworkController;
import com.crowdshelf.app.models.Book;
import com.crowdshelf.app.models.Crowd;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.squareup.otto.ThreadEnforcer;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import ntnu.stud.markul.crowdshelf.R;

public class TestingActivity extends AppCompatActivity {

    public TextView outputTextView;
    public EditText inputEditText;

    private Realm realm;
    private static Bus bus = new Bus(ThreadEnforcer.ANY);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing);
        inputEditText = (EditText) findViewById(R.id.inputTextView);
        outputTextView = (TextView) findViewById(R.id.resultTextView);

        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this).build();
//        Realm.deleteRealm(realmConfiguration); // Clean slate
        Realm.setDefaultConfiguration(realmConfiguration); // Make this Realm the default

        MainTabbedActivity.getBus().register(this);
        realm = Realm.getDefaultInstance();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_crowd_testing, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Subscribe
    public void handleViewBook(DBEvent event) {
        realm.refresh();
        Log.d(MainTabbedActivity.TAG, "realmpath: " + realm.getPath());
        switch (event.getDbEventType()) {
            case CROWD_READY:
                Log.i(MainTabbedActivity.TAG, "MainTabbedActivity - handleViewBook - BOOKINFO_READY");
                String crowdId = event.getDbObjectId();
                // Determine if you own the book you just scanned:
                Crowd crowd = realm.where(Crowd.class)
                        .equalTo("id", crowdId)
                        .findFirst();

                outputTextView.setText(crowd.getName());
                outputTextView.setText(crowd.getMembers().get(0).getId());
        }
    }

    public void crowdOnClick(View v){

        NetworkController.getCrowd("55fede47b379431100423430");

        String input = String.valueOf(inputEditText.getText());

//        String result = "crowd" + input;

//        outputTextView.setText(result);


    }

    public void userOnClick(View v){
        String input = String.valueOf(inputEditText.getText());

        String result = "user" + input;

        outputTextView.setText(result);
    }

    public void bookOnClick(View v){

        String input = String.valueOf(inputEditText.getText());

        String result = "book" + input;

        outputTextView.setText(result);

    }
}
