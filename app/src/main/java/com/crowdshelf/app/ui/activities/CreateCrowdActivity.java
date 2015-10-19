package com.crowdshelf.app.ui.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.crowdshelf.app.MainController;
import com.crowdshelf.app.io.DbEventType;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;
import ntnu.stud.markul.crowdshelf.R;

public class CreateCrowdActivity extends AppCompatActivity {
    private List<String> members = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_crowd);
    }

    public void addUserToCrowdClicked(View view) {
        EditText usernameEditText = (EditText) findViewById(R.id.crowdMember);
        String username = usernameEditText.getText().toString();
//        Uncomment when getUserIDByUsername is created
//        members.add(getUserIDByUsername(username));

        members.add(username);
        TextView membersTextView = (TextView) findViewById(R.id.membersTextView);
        membersTextView.setText(membersTextView.getText() + username + "\n");
        usernameEditText.setText("");
    }

    public void createNewCrowd(View view) {
        EditText crowdNameEditText = (EditText) findViewById(R.id.crowdNameEditText);
        String crowdName = crowdNameEditText.getText().toString();
        members.add(MainTabbedActivity.getMainUserId());
        MainController.createCrowd(crowdName, MainTabbedActivity.getMainUserId(), members, DbEventType.CROWD_CREATED);
    }
}
