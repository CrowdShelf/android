package com.crowdshelf.app.ui.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.crowdshelf.app.MainController;
import com.crowdshelf.app.io.DbEvent;
import com.crowdshelf.app.io.DbEventType;
import com.crowdshelf.app.io.network.NetworkController;
import com.crowdshelf.app.models.User;
import com.squareup.otto.Subscribe;

import ntnu.stud.markul.crowdshelf.R;

public class LoginActivity extends AppCompatActivity implements View.OnKeyListener {
    private static final String TAG = "LoginActivity";
    private String username;
    private String email;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        MainTabbedActivity.getBus().register(this);
        EditText usernameTextField = (EditText) findViewById(R.id.usernameTextfield);
        usernameTextField.setOnKeyListener(this);
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

        MainController.createUser(user, DbEventType.USER_CREATED);
    }

    public void login(View view) {
        EditText usernameTextfield = (EditText) findViewById(R.id.usernameTextfield);
        username = usernameTextfield.getText().toString();
        MainController.login(username, DbEventType.LOGIN);
    }

    @Subscribe
    public void handleLogin(DbEvent event) {
        Intent returnIntent;
        switch (event.getDbEventType()) {
            // @todo: Handle unsuccessful login attempts (username not found)
            case LOGIN:
                Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
                returnIntent = new Intent();
                Log.i(TAG, "DbEvent Login with username: " + username);
                returnIntent.putExtra("username",username);
                setResult(RESULT_OK,returnIntent);
                finish();
                break;
            case USER_CREATED:
                /*
                HACK: Put all new users in a default crowd:
                 */
                Log.i(TAG, "User created, id:" + event.getDbObjectId());
                NetworkController.addCrowdMember("561190113d92611100e5c6a1", event.getDbObjectId(), DbEventType.NONE);
                MainController.login(username, DbEventType.LOGIN);

                Toast.makeText(this, "User created", Toast.LENGTH_SHORT).show();
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

    @Override
    public boolean onKey(View view, int keyCode, KeyEvent event) {
        if (keyCode == EditorInfo.IME_ACTION_SEARCH ||
        keyCode == EditorInfo.IME_ACTION_DONE ||
        event.getAction() == KeyEvent.ACTION_DOWN &&
        event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
            if (!event.isShiftPressed()) {
                Log.v("AndroidEnterKeyActivity","Enter Key Pressed!");
                switch (view.getId()) {
                    case R.id.usernameTextfield:
                        login(view);
                        break;
                    case R.id.nameTextfield:
                        register(view);
                        break;
                    }
                return true;
                }
            }
        return false; // pass on to other listeners.
        }
}
