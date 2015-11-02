package com.crowdshelf.app.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

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

/**
 * Created by MortenAlver on 30.10.2015.
 */
public class OwnerListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private static final String TAG = "OwnerListActivity";
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

        setTitle("Click to return");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        ISBN = intent.getStringExtra("isbn");
        userID = MainTabbedActivity.getMainUserId();
        Log.i(TAG, "ISBN: " + ISBN);

        usersWithBook = new ArrayList<>();

        listAdapter = new UserListAdapter(this, usersWithBook);

        ListView lv = (ListView)findViewById(R.id.userListView);
        lv.setAdapter(listAdapter);
        lv.setOnItemClickListener(this);

        for (Book crowdBook : MainTabbedActivity.userCrowdBooks){
            if (crowdBook.getIsbn().equals(ISBN) && crowdBook.getRentedTo().equals(MainTabbedActivity.getMainUserId())){
                User user = realm.where(User.class)
                        .equalTo("id", crowdBook.getOwner())
                        .findFirst();
                if (!usersWithBook.contains(user) && user != null) {
                    usersWithBook.add(user);
                    listAdapter.notifyDataSetChanged();
                }
                if (usersWithBook.isEmpty()){
                    user = new User();
                    user.setName("No user in your crowds got this book");
                    usersWithBook.add(user);
                    listAdapter.notifyDataSetChanged();
                }
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
                        .notEqualTo("id", MainTabbedActivity.getMainUserId())
                        .findFirst();
                if (!usersWithBook.contains(user) && user != null) {
                    usersWithBook.add(user);
                    listAdapter.notifyDataSetChanged();
                }
                if (usersWithBook.isEmpty()){
                    user = new User();
                    user.setName("No user in your crowds got this book");
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
    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), ViewBookActivity.class);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        myIntent.putExtra("isbn", ISBN);
        startActivityForResult(myIntent, 0);
        finish();
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> a, View v, int position, long id) {
        User u = usersWithBook.get(position);
        Book rentedBook = realm.where(Book.class)
                .equalTo("owner", u.getId())
                .equalTo("rentedTo", MainTabbedActivity.getMainUserId())
                .equalTo("isbn", ISBN)
                .findFirst();
        Toast.makeText(OwnerListActivity.this, "Book was returned", Toast.LENGTH_SHORT).show();
        MainController.removeRenter(rentedBook.getId(), MainTabbedActivity.getMainUserId(), DbEventType.USER_BOOKS_CHANGED);

        finish();
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy: realm, bus, super");
        realm.close();
        MainTabbedActivity.getBus().unregister(this);
        super.onDestroy();
    }
}
