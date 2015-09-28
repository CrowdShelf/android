package com.crowdshelf.app.ui.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.crowdshelf.app.MainController;
import com.crowdshelf.app.ScannedBookActions;
import com.crowdshelf.app.io.DBEvent;
import com.crowdshelf.app.io.DBEventType;
import com.crowdshelf.app.models.User;
import com.squareup.otto.Subscribe;

import io.realm.Realm;
import ntnu.stud.markul.crowdshelf.R;

public class LoginActivity extends AppCompatActivity {
    private Realm realm;
    private String username;
    private String email;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        realm = Realm.getDefaultInstance();
        MainTabbedActivity.getBus().register(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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

    public void register(View view) {
        EditText usernameTextfield = (EditText) findViewById(R.id.usernameTextfield);
        username = usernameTextfield.getText().toString();

        EditText emailTextfield = (EditText) findViewById(R.id.mailTextfield);
        email = emailTextfield.getText().toString();

        EditText nameTextfield = (EditText) findViewById(R.id.nameTextfield);
        name = nameTextfield.getText().toString();

        User user=new User();
        user.setUsername(username);
        user.setName(name);
        user.setEmail(email);

        //MainController.createUser(user, DBEventType.USER_CREATED);

    }

    public void login(View view) {
        EditText usernameTextfield = (EditText) findViewById(R.id.usernameTextfield);
        String username = usernameTextfield.getText().toString();

//        EditText passwordTextfield = (EditText) findViewById(R.id.usernameTextfield);
//        String password = passwordTextfield.getText().toString();
        MainController.login(username, DBEventType.LOGIN);
    }

    @Subscribe
    public void handleLogin(DBEvent event) {
        realm.refresh();
        Log.d(MainTabbedActivity.TAG, "LoginActivity - handleLogin - event: " + event.getDbEventType());
        switch (event.getDbEventType()) {
            case LOGIN:
                Log.i(MainTabbedActivity.TAG, "LoginActivity - handleLogin - LOGIN");
                if (event.getDbObjectId().equals("True")) {
                    // login succesful
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("username",username);
                    setResult(RESULT_OK,returnIntent);
                    finish();
                } else if (event.getDbObjectId().equals("False")) {
                    // login failed
                    Toast.makeText(this, "User " + username + " not in database", Toast.LENGTH_SHORT).show();
                }
                break;

            case USER_CREATED:
                Log.i(MainTabbedActivity.TAG, "LoginActivity - handleLogin - USER_CREATED");
                // log in with new user
                Intent returnIntent = new Intent();
                returnIntent.putExtra("username",username);
                setResult(RESULT_OK,returnIntent);
                finish();
                break;
        }
    }


    @Override
    public void onDestroy() {
        MainController.onDestroy();
        realm.close();
        MainTabbedActivity.getBus().unregister(this);
        super.onDestroy();
    }
}
