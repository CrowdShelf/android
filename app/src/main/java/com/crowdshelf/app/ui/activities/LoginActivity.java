package com.crowdshelf.app.ui.activities;

import android.content.Intent;
import android.os.Handler;
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
import com.crowdshelf.app.io.network.NetworkController;
import com.crowdshelf.app.models.User;
import com.squareup.otto.Subscribe;

import io.realm.Realm;
import ntnu.stud.markul.crowdshelf.R;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private String username;
    private String email;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        MainTabbedActivity.getBus().register(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_login, menu);
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
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
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

        MainController.createUser(user, DBEventType.USER_CREATED);
    }

    public void login(View view) {
        EditText usernameTextfield = (EditText) findViewById(R.id.usernameTextfield);
        username = usernameTextfield.getText().toString();
        MainController.login(username, DBEventType.LOGIN);
    }

    @Subscribe
    public void handleLogin(DBEvent event) {
        Intent returnIntent;
        Log.i(TAG, "handleLogin - event: " + event.getDbEventType());
        switch (event.getDbEventType()) {
            case LOGIN:
                Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
                returnIntent = new Intent();
                returnIntent.putExtra("username",username);
                setResult(RESULT_OK,returnIntent);
                finish();
                break;
                /*
                if (event.getDbObjectId().equals("True")) {
                    // login succesful

                } else if (event.getDbObjectId().equals("False")) {
                    // login failed
                    Toast.makeText(this, "User " + username + " not registrated", Toast.LENGTH_SHORT).show();
                }
                break;
                */
            case USER_CREATED:
                /*
                HACK: Put all new users in a default crowd:
                 */
                Log.i(TAG, "User created, id:" + event.getDbObjectId());
                NetworkController.addCrowdMember("561190113d92611100e5c6a1", event.getDbObjectId(), DBEventType.NONE);

                // log in with new user
                Toast.makeText(this, "User created", Toast.LENGTH_SHORT).show();
                returnIntent = new Intent();
                returnIntent.putExtra("username",username);
                setResult(RESULT_OK,returnIntent);
                finish();
                break;
        }
    }


    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy: realm, bus, super");
        MainTabbedActivity.getBus().unregister(this);
        super.onDestroy();
    }

/*    @Override
    public void onBackPressed() {}*/

    public void registerLayout(View view) {
        findViewById(R.id.mailTextfield).setVisibility(View.VISIBLE);
        findViewById(R.id.nameTextfield).setVisibility(View.VISIBLE);
        findViewById(R.id.registationButton).setVisibility(View.VISIBLE);
        findViewById(R.id.loginButton).setVisibility(View.INVISIBLE);
        findViewById(R.id.registerLayout).setVisibility(View.INVISIBLE);
        findViewById(R.id.cancelLayout).setVisibility(View.VISIBLE);
    }

    public void cancelLayout(View view) {
        findViewById(R.id.mailTextfield).setVisibility(View.INVISIBLE);
        findViewById(R.id.nameTextfield).setVisibility(View.INVISIBLE);
        findViewById(R.id.registationButton).setVisibility(View.INVISIBLE);
        findViewById(R.id.loginButton).setVisibility(View.VISIBLE);
        findViewById(R.id.registerLayout).setVisibility(View.VISIBLE);
        findViewById(R.id.cancelLayout).setVisibility(View.INVISIBLE);
    }
}
