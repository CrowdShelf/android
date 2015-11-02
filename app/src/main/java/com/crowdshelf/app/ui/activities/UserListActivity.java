package com.crowdshelf.app.ui.activities;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import java.util.List;

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

        setTitle("Tap to borrow from user");
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
            if (crowdBook.getIsbn().equals(ISBN) && !crowdBook.getOwner().equals(MainTabbedActivity.getMainUserId())){
                String gotBook = crowdBook.getOwner();
                if (!crowdBook.getRentedTo().isEmpty()){
                    gotBook = crowdBook.getRentedTo();
                }
                User user = realm.where(User.class)
                        .equalTo("id", gotBook)
                        .notEqualTo("id", MainTabbedActivity.getMainUserId())
                        .findFirst();
                if (!usersWithBook.contains(user) && user != null) {
                    usersWithBook.add(user);
                    listAdapter.notifyDataSetChanged();
                }
            }
        }
        if (usersWithBook.isEmpty()) {
            User user = new User();
            user.setName("No user in your crowds got this book");
            usersWithBook.add(user);
            listAdapter.notifyDataSetChanged();
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
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public void onItemClick(AdapterView<?> a, View v, int position, long id) {
        User u = usersWithBook.get(position);
        if (u != null) {
            Book bookToRent = realm.where(Book.class)
                    .equalTo("isbn", ISBN)
                    .equalTo("owner", u.getId())
                    .equalTo("rentedTo", "")
                    .findFirst();
            if (bookToRent == null) {
                bookToRent = realm.where(Book.class)
                        .equalTo("isbn", ISBN)
                        .notEqualTo("owner", userID)
                        .equalTo("rentedTo", u.getId())
                        .findFirst();
            }
            Toast.makeText(UserListActivity.this, "Book was borrowed", Toast.LENGTH_SHORT).show();
            MainController.addRenter(bookToRent.getId(), userID, DbEventType.USER_BOOKS_CHANGED);

            finish();
        }
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy: realm, bus, super");
        realm.close();
        MainTabbedActivity.getBus().unregister(this);
        super.onDestroy();
    }
}
