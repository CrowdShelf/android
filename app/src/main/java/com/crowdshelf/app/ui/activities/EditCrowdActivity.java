package com.crowdshelf.app.ui.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.crowdshelf.app.MainController;
import com.crowdshelf.app.io.DbEvent;
import com.crowdshelf.app.io.DbEventType;
import com.crowdshelf.app.models.Crowd;
import com.crowdshelf.app.models.MemberId;
import com.crowdshelf.app.models.User;
import com.crowdshelf.app.ui.adapter.LetterTileProvider;
import com.crowdshelf.app.ui.adapter.UserListAdapter;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmList;
import ntnu.stud.markul.crowdshelf.R;

public class EditCrowdActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnKeyListener, TextView.OnEditorActionListener, TextWatcher {
    private Realm realm;
    private String TAG = "EditCrowdActivity";
    private ArrayList<User> crowdMembers;
    private ArrayList<String> membersStrings;
    private UserListAdapter listAdapter;
    private EditText addUserNameTextField;
    private EditText crowdNameEditText;
    private String crowdID;
    private ImageView crowdImageView;
    private LetterTileProvider letterTileProvider;
    private int mGroupImagePixelSize;
    private ListView userListView;
    private User selectedContexMenuUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainTabbedActivity.getBus().register(this);
        realm = Realm.getDefaultInstance();
        setContentView(R.layout.activity_create_crowd);
        crowdMembers = new ArrayList<>();
        listAdapter = new UserListAdapter(this, crowdMembers);

        setTitle("Create Group");
        MainController.getUser(MainTabbedActivity.getMainUserId(), DbEventType.GET_USER);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        letterTileProvider = new LetterTileProvider(this);

        userListView = (ListView) findViewById(R.id.memberListView);
        crowdNameEditText = (EditText) findViewById(R.id.crowdNameEditText);
        addUserNameTextField = (EditText) findViewById(R.id.crowdMemberTextField);
        crowdImageView = (ImageView) findViewById(R.id.editCrowdImageView);

        mGroupImagePixelSize = getResources().getDimensionPixelSize(R.dimen.letter_tile_size);


        addUserNameTextField.setOnEditorActionListener(this);
        crowdNameEditText.addTextChangedListener(this);

        userListView.setAdapter(listAdapter);
        userListView.setOnItemClickListener(this);
        registerForContextMenu(userListView);

        Intent intent = getIntent();
        crowdID = intent.getStringExtra("crowdID");
        crowdNameEditText.setOnKeyListener(this);
        findViewById(R.id.crowdMemberTextField).setOnKeyListener(this);
        if (!crowdID.isEmpty()) {

            findViewById(R.id.deleteCrowdButton).setVisibility(View.VISIBLE);

            Crowd crowd = realm.where(Crowd.class).equalTo("id", crowdID).findFirst();
            crowdNameEditText.setText(crowd.getName());
//            crowdImageView.setImageBitmap(letterTileProvider.getLetterTile(crowd.getName(), getResources().getDimensionPixelSize(R.dimen.letter_tile_size)));
            RealmList<MemberId> members = crowd.getMembers();
            for (MemberId m : members) {
                MainController.getUser(m.getId(), DbEventType.EditCrowdActivity_ADD_USERS);
            }
        }
        else {
            findViewById(R.id.leaveCrowdButton).setVisibility(View.INVISIBLE);
            findViewById(R.id.crowdBooksButton).setVisibility(View.INVISIBLE);
            findViewById(R.id.deleteCrowdButton).setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == R.id.memberListView) {
            ListView lv = (ListView) v;
            AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) menuInfo;
            User obj = (User) lv.getItemAtPosition(acmi.position);

            menu.setHeaderTitle(obj.getName());
            menu.add(0, acmi.position, 0, "Show books");
            menu.add(0, acmi.position, 1, "Remove " + obj.getName() + " from group");

        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        selectedContexMenuUser = (User) userListView.getItemAtPosition(item.getItemId());
        switch (item.getOrder()){
            case 0:
                Intent intent = new Intent(this, BookGridViewActivity.class);
                intent.putExtra("userID", selectedContexMenuUser.getId());
                intent.putExtra("shelf", "ninjahack");
                startActivity(intent);
                break;

            case 1:
                new AlertDialog.Builder(this)
                        .setTitle("Remove user?")
                        .setMessage("Are you sure you want to remove this user?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                                Toast.makeText(EditCrowdActivity.this, "Removed", Toast.LENGTH_SHORT).show();
                                crowdMembers.remove(selectedContexMenuUser);
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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                super.onBackPressed();
                return true;
            case R.id.saveCrowdButton:
                if (crowdNameEditText.getText().toString().isEmpty()){
                    Toast.makeText(EditCrowdActivity.this, "Group name can not be empty", Toast.LENGTH_SHORT).show();
                }
                else {
                    updateCrowdButtonClicked();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void leaveCrowdButtonClick(View v){
        new AlertDialog.Builder(this)
                .setTitle("Leave Group?")
                .setMessage("Are you sure you want to leave this group?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        MainController.removeCrowdMember(crowdID, MainTabbedActivity.getMainUserId(), DbEventType.USER_CROWDS_CHANGED);
                        MainTabbedActivity.getMixpanel().track("LeaveCrowd");
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

    public void deleteCrowdButtonClick(View v) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Group?")
                .setMessage("Are you sure you want to remove this group?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        MainController.deleteCrowd(crowdID, DbEventType.USER_CROWDS_CHANGED);
                        MainTabbedActivity.getMixpanel().track("DeleteCrowd");
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


    private void updateCrowdButtonClicked() {
        String crowdName = crowdNameEditText.getText().toString();
        Toast.makeText(this, crowdName, Toast.LENGTH_SHORT).show();
        membersStrings = new ArrayList<>();
        for (User u : crowdMembers) {
            membersStrings.add(u.getId());
        }
        if (!crowdID.isEmpty()) {
            MainController.deleteCrowd(crowdID, DbEventType.USER_CROWDS_CHANGED);
        }
        MainController.createCrowd(crowdName, MainTabbedActivity.getMainUserId(), membersStrings, DbEventType.USER_CROWDS_CHANGED);
        MainTabbedActivity.getMixpanel().track("CreateCrowd");
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
                break;
            case GET_USER:
                User user2 = realm.where(User.class)
                        .equalTo("id", event.getDbObjectId())
                        .findFirst();
                if (!crowdMembers.contains(user2)) {
                    crowdMembers.add(user2);
                    listAdapter.notifyDataSetChanged();
                }
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> a, View v, final int position, long id) {
        this.openContextMenu(v);
//        new AlertDialog.Builder(this)
//                .setTitle("Delete entry")
//                .setMessage("Are you sure you want to remove this entry?")
//                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        // continue with delete
//                        Toast.makeText(EditCrowdActivity.this, "Removed", Toast.LENGTH_SHORT).show();
//                        crowdMembers.remove(position);
//                        listAdapter.notifyDataSetChanged();
//                    }
//                })
//                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        // do nothing
//                    }
//                })
//                .setIcon(android.R.drawable.ic_dialog_alert)
//                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
        MainTabbedActivity.getBus().unregister(this);
    }

    public void addUserToCrowdClicked(View view) {

        String username = addUserNameTextField.getText().toString();
//        Uncomment when getUserIDByUsername is created
//        String userID = getUserIDByUsername(username);
//        members.add(getUserIDByUsername(username));
        if (!username.isEmpty()) {
            MainController.getUserByUsername(username, DbEventType.EditCrowdActivity_USERNAME_RECEIVED);
            addUserNameTextField.setText("");
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
                        findViewById(R.id.crowdMemberTextField).requestFocus();
                        break;
                    case R.id.crowdMemberTextField:
                        break;
                }
                return true;
            }
        }
        return false; // pass on to other listeners.
    }

    public void leaveCrowdClicked(View view) {
        MainController.removeCrowdMember(crowdID, MainTabbedActivity.getMainUserId(), DbEventType.USER_CROWDS_CHANGED);
        Toast.makeText(EditCrowdActivity.this, "Remove " + MainTabbedActivity.getMainUserId(), Toast.LENGTH_SHORT).show();
        finish();
    }

    public void crowdBooksClick(View view) {
        Intent intent = new Intent(this, BookGridViewActivity.class);
        intent.putExtra("shelf", crowdID);
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_edit_crowd, menu);
        return true;
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        Log.i(TAG, "onEditorAction");
        switch (actionId) {
            case EditorInfo.IME_ACTION_DONE:
                addUserToCrowdClicked(null);
                break;
        }
        return true;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (count > 0) {
            crowdImageView.setImageBitmap(letterTileProvider.getLetterTile(String.valueOf(s), mGroupImagePixelSize));
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
    }
}
