package com.crowdshelf.app.ui.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.crowdshelf.app.MainController;
import com.crowdshelf.app.io.DbEvent;
import com.crowdshelf.app.io.DbEventType;
import com.crowdshelf.app.models.Crowd;
import com.crowdshelf.app.models.MemberId;
import com.crowdshelf.app.models.User;
import com.squareup.otto.Subscribe;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import ntnu.stud.markul.crowdshelf.R;

public class EditCrowdActivity extends AppCompatActivity {
    private Realm realm;
    private String TAG = "EditCrowdActivity";
    private List<String> usernames = new ArrayList<>();
    TextView membersTextView;
    String textViewString = "Usernames:\n";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainTabbedActivity.getBus().register(this);
        realm = Realm.getDefaultInstance();
        setContentView(R.layout.activity_create_crowd);
        membersTextView = (TextView) findViewById(R.id.membersTextView);
        Button updateButton = (Button) findViewById(R.id.createCrowdButton);
        updateButton.setText("Update crowd");
        Intent intent = getIntent();
        String crowdID = intent.getStringExtra("crowdID");
        Crowd crowd = realm.where(Crowd.class).equalTo("id", crowdID).findFirst();
        EditText crowdNameEditText = (EditText) findViewById(R.id.crowdNameEditText);
        crowdNameEditText.setText(crowd.getName());
        RealmList<MemberId> members = crowd.getMembers();
        for (MemberId m : members){
            MainController.getUser(m.getId(), DbEventType.EditCrowdActivity_ADD_USERS);
        }
    }

    @Subscribe
    public void handleDBEvents(DbEvent event) {
        realm.refresh();
        Log.i(TAG, "Handle DB Event: " + event.getDbEventType());
        switch (event.getDbEventType()) {
            case EditCrowdActivity_ADD_USERS:
                User user = realm.where(User.class)
                        .equalTo("id", event.getDbObjectId())
                        .notEqualTo("id", MainTabbedActivity.getMainUserId())
                        .findFirst();


                usernames.add(user.getUsername());
                // TODO:: display list instead of textView
                membersTextView.setText(membersTextView.getText() + user.getUsername() + "\n");

                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
        MainTabbedActivity.getBus().unregister(this);
    }
}
