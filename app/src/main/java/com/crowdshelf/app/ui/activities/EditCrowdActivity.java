package com.crowdshelf.app.ui.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.crowdshelf.app.MainController;
import com.crowdshelf.app.io.DbEvent;
import com.crowdshelf.app.io.DbEventType;
import com.crowdshelf.app.models.Crowd;
import com.crowdshelf.app.models.MemberId;
import com.crowdshelf.app.models.User;
import com.crowdshelf.app.ui.adapter.UserListAdapter;
import com.squareup.otto.Subscribe;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import ntnu.stud.markul.crowdshelf.R;

public class EditCrowdActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{
    private Realm realm;
    private String TAG = "EditCrowdActivity";
    private ArrayList<User> crowdMembers;
    private ArrayList<String> membersStrings;
    private UserListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainTabbedActivity.getBus().register(this);
        realm = Realm.getDefaultInstance();
        setContentView(R.layout.activity_create_crowd);
        crowdMembers = new ArrayList<>();
        listAdapter = new UserListAdapter(this, crowdMembers);

        ListView lv = (ListView)findViewById(R.id.memberListView);
        lv.setAdapter(listAdapter);
        lv.setOnItemClickListener(this);
        Intent intent = getIntent();
        String crowdID = intent.getStringExtra("crowdID");
        EditText crowdNameEditText = (EditText) findViewById(R.id.crowdNameEditText);
        if (!crowdID.isEmpty()){
            findViewById(R.id.createCrowdButton).setVisibility(View.INVISIBLE);
            findViewById(R.id.updateCrowdButton).setVisibility(View.VISIBLE);
            Crowd crowd = realm.where(Crowd.class).equalTo("id", crowdID).findFirst();
            crowdNameEditText.setText(crowd.getName());
            RealmList<MemberId> members = crowd.getMembers();
            for (MemberId m : members) {
                MainController.getUser(m.getId(), DbEventType.EditCrowdActivity_ADD_USERS);
            }
        }
    }



    public void updateCrowd(View view) {
        EditText crowdNameEditText = (EditText) findViewById(R.id.crowdNameEditText);
        String crowdName = crowdNameEditText.getText().toString();
        Toast.makeText(this,crowdName,Toast.LENGTH_SHORT).show();
        membersStrings = new ArrayList<>();
        membersStrings.add(MainTabbedActivity.getMainUserId());
        for (User u : crowdMembers){
            membersStrings.add(u.getId());
        }
        MainController.createCrowd(crowdName, MainTabbedActivity.getMainUserId(), membersStrings, DbEventType.USER_CROWDS_CHANGED);
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
                // Apparently need to delay the implementation
                String delay = user.getId();
                if (delay.isEmpty()) {
                Toast.makeText(EditCrowdActivity.this, "Username does not excist", Toast.LENGTH_SHORT).show();
            }
                if (!crowdMembers.contains(user)) {
                    crowdMembers.add(user);
                    listAdapter.notifyDataSetChanged();
                }
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> a, View v, final int position, long id) {
        new AlertDialog.Builder(this)
                .setTitle("Delete entry")
                .setMessage("Are you sure you want to remove this entry?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        Toast.makeText(EditCrowdActivity.this, "Removed", Toast.LENGTH_SHORT).show();
                        crowdMembers.remove(position);
                        listAdapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
        MainTabbedActivity.getBus().unregister(this);
    }

    public void addUserToCrowdClicked(View view) {
        EditText usernameEditText = (EditText) findViewById(R.id.crowdMember);
        String username = usernameEditText.getText().toString();
//        Uncomment when getUserIDByUsername is created
//        String userID = getUserIDByUsername(username);
//        members.add(getUserIDByUsername(username));
        if (username.length() > 0) {
            MainController.getUser(username, DbEventType.EditCrowdActivity_ADD_USERS);
        }
    }
}
