package com.crowdshelf.app.ui.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.crowdshelf.app.MainController;
import com.crowdshelf.app.io.DbEvent;
import com.crowdshelf.app.io.DbEventType;
import com.crowdshelf.app.models.Crowd;
import com.crowdshelf.app.models.MemberId;
import com.crowdshelf.app.models.User;
import com.crowdshelf.app.ui.adapter.UserListAdapter;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmList;
import ntnu.stud.markul.crowdshelf.R;

public class EditCrowdActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnKeyListener {
    private Realm realm;
    private String TAG = "EditCrowdActivity";
    private ArrayList<User> crowdMembers;
    private ArrayList<String> membersStrings;
    private UserListAdapter listAdapter;
    private EditText usernameEditText;
    private EditText crowdNameEditText;
    private String crowdID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainTabbedActivity.getBus().register(this);
        realm = Realm.getDefaultInstance();
        setContentView(R.layout.activity_create_crowd);
        crowdMembers = new ArrayList<>();
        listAdapter = new UserListAdapter(this, crowdMembers);

        setTitle("Create Crowd");

        ListView lv = (ListView) findViewById(R.id.memberListView);
        crowdNameEditText = (EditText) findViewById(R.id.crowdNameEditText);
        usernameEditText = (EditText) findViewById(R.id.crowdMember);

        lv.setAdapter(listAdapter);
        lv.setOnItemClickListener(this);
        Intent intent = getIntent();
        crowdID = intent.getStringExtra("crowdID");
        crowdNameEditText.setOnKeyListener(this);
        findViewById(R.id.crowdMember).setOnKeyListener(this);
        if (!crowdID.isEmpty()) {
            findViewById(R.id.createCrowdButton).setVisibility(View.INVISIBLE);
            findViewById(R.id.updateCrowdButton).setVisibility(View.VISIBLE);
            findViewById(R.id.deleteCrowdButton).setVisibility(View.VISIBLE);
            Crowd crowd = realm.where(Crowd.class).equalTo("id", crowdID).findFirst();
            crowdNameEditText.setText(crowd.getName());
            RealmList<MemberId> members = crowd.getMembers();
            for (MemberId m : members) {
                MainController.getUser(m.getId(), DbEventType.EditCrowdActivity_ADD_USERS);
            }
        }
    }

    public void deleteCrowdButtonClick(View v){
        new AlertDialog.Builder(this)
                .setTitle("Delete Crowd?")
                .setMessage("Are you sure you want to remove this crowd?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        MainController.deleteCrowd(crowdID, DbEventType.USER_CROWDS_CHANGED);
                        finish();
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


    public void updateCrowd(View view) {
        String crowdName = crowdNameEditText.getText().toString();
        Toast.makeText(this, crowdName, Toast.LENGTH_SHORT).show();
        membersStrings = new ArrayList<>();
        membersStrings.add(MainTabbedActivity.getMainUserId());
        for (User u : crowdMembers) {
            membersStrings.add(u.getId());
        }
        MainController.createCrowd(crowdName, MainTabbedActivity.getMainUserId(), membersStrings, DbEventType.USER_CROWDS_CHANGED);
        finish();
    }

    @Subscribe
    public void handleDBEvents(DbEvent event) {
        realm.refresh();
        Log.i(TAG, "Handle DB Event: " + event.getDbEventType());
        switch (event.getDbEventType()) {
            case EditCrowdActivity_ADD_USERS:
                User user = realm.where(User.class)
                        .equalTo("id", event.getDbObjectId())
                        .findFirst();
                // Apparently need to delay the implementation
                String delay = user.getId();
                if (delay.isEmpty()) {
                    Toast.makeText(EditCrowdActivity.this, "Username does not exist", Toast.LENGTH_SHORT).show();
                }
                if (!crowdMembers.contains(user)) {
                    crowdMembers.add(user);
                    listAdapter.notifyDataSetChanged();
                }
                break;
            case EditCrowdActivity_USERNAME_RECEIVED:
                if (!event.getDbObjectId().equals("all")) {
                    User user2 = realm.where(User.class)
                            .equalTo("id", event.getDbObjectId())
                            .findFirst();
                    if (!crowdMembers.contains(user2)) {
                        crowdMembers.add(user2);
                        listAdapter.notifyDataSetChanged();
                    }
                }
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

        String username = usernameEditText.getText().toString();
//        Uncomment when getUserIDByUsername is created
//        String userID = getUserIDByUsername(username);
//        members.add(getUserIDByUsername(username));
        if (!username.isEmpty()) {
            MainController.getUserByUsername(username, DbEventType.EditCrowdActivity_USERNAME_RECEIVED);
            usernameEditText.setText("");
        }
    }

    @Override
    public boolean onKey(View view, int keyCode, KeyEvent event) {
        if (keyCode == EditorInfo.IME_ACTION_SEARCH ||
                keyCode == EditorInfo.IME_ACTION_DONE ||
                event.getAction() == KeyEvent.ACTION_DOWN &&
                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
            if (!event.isShiftPressed()) {
                Log.v("AndroidEnterKeyActivity", "Enter Key Pressed!");
                switch (view.getId()) {
                    case R.id.crowdNameEditText:
                        findViewById(R.id.crowdNameEditText).clearFocus();
                        findViewById(R.id.crowdMember).requestFocus();
                        break;
                    case R.id.crowdMember:
                        break;
                }
                return true;
            }
        }
        return false; // pass on to other listeners.
    }
}
