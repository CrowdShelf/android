package com.crowdshelf.app.ui.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.crowdshelf.app.MainController;
import com.crowdshelf.app.io.DbEvent;
import com.crowdshelf.app.io.DbEventType;
import com.crowdshelf.app.models.Book;
import com.crowdshelf.app.models.User;
import com.crowdshelf.app.ui.adapter.UserListAdapter;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import io.realm.Realm;
import ntnu.stud.markul.crowdshelf.R;

public class UserListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private static final String TAG = "UserListActivity";
    private Realm realm;
    private ArrayList<User> usersWithBook;
    private String ISBN;
    private String userID;
    private UserListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        realm = Realm.getDefaultInstance();
        MainTabbedActivity.getBus().register(this);

        Intent intent = getIntent();
        ISBN = intent.getStringExtra("ISBN");
        userID = MainTabbedActivity.getMainUserId();
        Log.i(TAG, "ISBN: " + ISBN);

        usersWithBook = new ArrayList<>();

        listAdapter = new UserListAdapter(this, usersWithBook);

        ListView lv = (ListView)findViewById(R.id.userListView);
        lv.setAdapter(listAdapter);
        lv.setOnItemClickListener(this);

        for (Book crowdBook : MainTabbedActivity.userCrowdBooks){
            if (crowdBook.getIsbn().equals(ISBN)){
                MainController.getUser(crowdBook.getOwner(), DbEventType.UserListActivity_USER_READY);
            }
        }
    }

    @Subscribe
    public void handleDBEvents(DbEvent event) {
        realm.refresh();
        Log.i(TAG, "Handle DB Event: " + event.getDbEventType());
        switch (event.getDbEventType()) {
            case UserListActivity_USER_READY:
                User user = realm.where(User.class)
                        .equalTo("id", event.getDbObjectId())
                        .findFirst();
                if (!usersWithBook.contains(user)) {
                    usersWithBook.add(user);
                    listAdapter.notifyDataSetChanged();
                }
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_list, menu);
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

    @Override
    public void onItemClick(AdapterView<?> a, View v, int position, long id) {
        User u = usersWithBook.get(position);
        Book bookToRent = realm.where(Book.class)
                .equalTo("owner", u.getId())
                .equalTo("isbn", ISBN)
                .findFirst();

        MainController.addRenter(bookToRent.getId(), userID, DbEventType.NONE);

        Intent returnIntent = new Intent();
        returnIntent.putExtra("userName", u.getName());
        setResult(RESULT_OK, returnIntent);
        finish();
    }
}
