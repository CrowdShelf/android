package com.crowdshelf.app.ui.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.crowdshelf.app.MainController;
import com.crowdshelf.app.io.DbEventType;
import com.crowdshelf.app.models.Book;
import com.crowdshelf.app.models.User;
import com.crowdshelf.app.ui.adapter.UserListAdapter;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;
import ntnu.stud.markul.crowdshelf.R;

public class CreateCrowdActivity extends AppCompatActivity implements AdapterView.OnItemClickListener  {
    private List<String> members = new ArrayList<>();
    private UserListAdapter listAdapter;
    private ArrayList<User> usersWithBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_crowd);
        listAdapter = new UserListAdapter(this, usersWithBook);

        ListView lv = (ListView)findViewById(R.id.userListView);
        lv.setAdapter(listAdapter);
        lv.setOnItemClickListener(this);
    }

    public void createNewCrowd(View view) {
        EditText crowdNameEditText = (EditText) findViewById(R.id.crowdNameEditText);
        String crowdName = crowdNameEditText.getText().toString();
        members.add(MainTabbedActivity.getMainUserId());
        MainController.createCrowd(crowdName, MainTabbedActivity.getMainUserId(), members, DbEventType.CROWD_CREATED);
    }

    @Override
    public void onItemClick(AdapterView<?> a, View v, int position, long id) {
        Toast.makeText(CreateCrowdActivity.this, "Pressed someone", Toast.LENGTH_SHORT).show();
    }
}
