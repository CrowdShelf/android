package com.crowdshelf.app.ui.activities;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.crowdshelf.app.MainController;
import com.crowdshelf.app.io.DbEvent;
import com.crowdshelf.app.io.DbEventType;
import com.crowdshelf.app.models.User;
import com.squareup.otto.Subscribe;

import io.realm.Realm;
import ntnu.stud.markul.crowdshelf.R;

public class LoginActivity extends AppCompatActivity implements TextView.OnEditorActionListener {
    private static final String TAG = "LoginActivity";
    private String username;
    private String email;
    private String name;
    private String password;
    private EditText usernameTextField;
    private EditText passwordTextField;
    private ProgressBar loginSpinner;
    private ImageView logoImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Login");
        setContentView(R.layout.activity_login);
        MainTabbedActivity.getBus().register(this);
        usernameTextField = (EditText) findViewById(R.id.usernameTextfield);
        passwordTextField = (EditText) findViewById(R.id.passwordTextField);
        logoImageView = (ImageView) findViewById(R.id.crowdshelfLogoImageView);
        loginSpinner = (ProgressBar) findViewById(R.id.loginSpinner);
        loginSpinner.setVisibility(View.INVISIBLE);
        passwordTextField.setOnEditorActionListener(this);
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
        username = usernameTextField.getText().toString();

        EditText emailTextfield = (EditText) findViewById(R.id.mailTextfield);
        email = emailTextfield.getText().toString();

        EditText nameTextfield = (EditText) findViewById(R.id.nameTextfield);
        name = nameTextfield.getText().toString();

        password = passwordTextField.getText().toString();

        User user=new User();
        user.setUsername(username);
        user.setName(name);
        user.setEmail(email);

        MainController.createUser(username, name, email, password, DbEventType.USER_CREATED);
    }

    public void signInButtonClicked(View view) {
        username = usernameTextField.getText().toString();
        password = passwordTextField.getText().toString();
        if (username.isEmpty()){
            Toast.makeText(this, "Username required", Toast.LENGTH_LONG).show();
        }
        else if (password.isEmpty()){
            Toast.makeText(this, "Password required", Toast.LENGTH_LONG).show();
        }
        else{
            MainController.login(username, password, DbEventType.LOGIN);
            logoImageView.setVisibility(View.INVISIBLE);
            loginSpinner.setVisibility(View.VISIBLE);
        }
    }

    @Subscribe
    public void handleLogin(DbEvent event) {
        Intent returnIntent;
        switch (event.getDbEventType()) {
            // @todo: Handle unsuccessful signInButtonClicked attempts (username not found)
            case LOGIN:
                Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
                returnIntent = new Intent();
                Log.i(TAG, "DbEvent Login with username: " + username);
                returnIntent.putExtra("username", username);
                setResult(RESULT_OK,returnIntent);
                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                User u = realm.where(User.class)
                        .equalTo("username", username)
                        .findFirst();
                u.setPassword(password);
                realm.commitTransaction();
                realm.close();
                loginSpinner.setVisibility(View.INVISIBLE);

                finish();
                break;
            case USER_CREATED:

                Log.i(TAG, "User created, id:" + event.getDbObjectId());
                MainController.login(username, password, DbEventType.LOGIN);

                Toast.makeText(this, "User created", Toast.LENGTH_SHORT).show();
                break;
        }
    }


    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy: bus, super");
        MainTabbedActivity.getBus().unregister(this);
        super.onDestroy();
    }

/*    @Override
    public void onBackPressed() {}*/

    public void changeToCreateUserViewButtonClicked(View view) {
        findViewById(R.id.mailTextfield).setVisibility(View.VISIBLE);
        findViewById(R.id.nameTextfield).setVisibility(View.VISIBLE);
        findViewById(R.id.registationButton).setVisibility(View.VISIBLE);
        findViewById(R.id.cancelLayout).setVisibility(View.VISIBLE);

        findViewById(R.id.loginButton).setVisibility(View.INVISIBLE);
        findViewById(R.id.createNewUserButton).setVisibility(View.INVISIBLE);
        findViewById(R.id.forgotPasswordButton).setVisibility(View.INVISIBLE);

        passwordTextField.setNextFocusForwardId(R.id.mailTextfield);
        passwordTextField.setImeOptions(EditorInfo.IME_ACTION_NEXT);

    }

    public void cancelCreateNewUserButtonClicked(View view) {
        findViewById(R.id.mailTextfield).setVisibility(View.INVISIBLE);
        findViewById(R.id.nameTextfield).setVisibility(View.INVISIBLE);
        findViewById(R.id.registationButton).setVisibility(View.INVISIBLE);
        findViewById(R.id.cancelLayout).setVisibility(View.INVISIBLE);

        findViewById(R.id.forgotPasswordButton).setVisibility(View.VISIBLE);
        findViewById(R.id.loginButton).setVisibility(View.VISIBLE);
        findViewById(R.id.createNewUserButton).setVisibility(View.VISIBLE);

        passwordTextField.setImeOptions(EditorInfo.IME_ACTION_DONE);

    }


    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        Log.i(TAG, "onEditorAction");
        switch (actionId){
            case EditorInfo.IME_ACTION_DONE:
                signInButtonClicked(null);
                break;
        }
        return false;
    }
}
