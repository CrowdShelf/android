package com.crowdshelf.app.ui.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.crowdshelf.app.MainController;
import com.crowdshelf.app.models.User;
import com.crowdshelf.app.ui.adapter.UserListAdapter;

import java.util.ArrayList;

import ntnu.stud.markul.crowdshelf.R;

public class UserListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        final ArrayList<User> listOfAllUsers = new ArrayList<User>();
        User u1 = new User();
        User u2 = new User();
        u1.setName("A name!");
        u2.setName("A second name");
        u1.setEmail("asdf@asdf.com");
        u2.setEmail("ola@nordmann.no");

        listOfAllUsers.add(u1);
        listOfAllUsers.add(u2);

        UserListAdapter listAdapter = new UserListAdapter(this, listOfAllUsers);

        ListView lv = (ListView)findViewById(R.id.userListView);
        lv.setAdapter(listAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                User u = listOfAllUsers.get(position);
                Toast.makeText(getBaseContext(), "Clicked on: " + u.getName(), Toast.LENGTH_SHORT).show();
            }
        });
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
}
