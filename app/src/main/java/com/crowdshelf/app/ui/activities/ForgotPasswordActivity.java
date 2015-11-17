package com.crowdshelf.app.ui.activities;

import com.crowdshelf.app.MainController;
import com.crowdshelf.app.io.DbEventType;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import ntnu.stud.markul.crowdshelf.R;

public class ForgotPasswordActivity extends AppCompatActivity implements TextView.OnEditorActionListener {

    private TextView resetTextView;
    private EditText resetUsernameEditText;
    private Button resetPasswordButton;
    private TextView confirmTextView;
    private EditText confirmCodeEditText;
    private EditText confirmPasswordEditText;
    private Button confirmPasswordButton;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Reset password");
        setContentView(R.layout.activity_forgot_password);

        resetTextView = (TextView) findViewById(R.id.typeInUsernameTextView);
        resetUsernameEditText = (EditText) findViewById(R.id.resetPasswordUsernameTextField);
        resetPasswordButton = (Button) findViewById(R.id.resetPasswordButton);

        resetUsernameEditText.setOnEditorActionListener(this);

        confirmTextView = (TextView) findViewById(R.id.confirmPasswordTextView);
        confirmCodeEditText = (EditText) findViewById(R.id.confirmCodeTextField);
        confirmPasswordEditText = (EditText) findViewById(R.id.confirmNewPasswordTextField);
        confirmPasswordButton = (Button) findViewById(R.id.confirmPasswordButton);

        confirmPasswordEditText.setOnEditorActionListener(this);



        hideResetPasswordElements(false);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }
    public void confirmResetPasswordButtonClicked(View v){
        String code = confirmCodeEditText.getText().toString();
        String newPassword = confirmPasswordEditText.getText().toString();

        if (code.isEmpty() || !isValidCode(code)){
            Toast.makeText(this, "Input valid code", Toast.LENGTH_SHORT).show();
        }else if (newPassword.isEmpty()){
            Toast.makeText(this, "Input valid password", Toast.LENGTH_SHORT).show();
        }else{
            //TODO: Send code and new password to backend
            MainController.resetPassword(username, newPassword, Integer.parseInt(code), DbEventType.NONE);
            finish();
        }
    }



    public void resetPasswordButtonClicked(View v){
        username = resetUsernameEditText.getText().toString();
        if (!username.isEmpty()){

            //TODO: Send username to backend to receive email
            MainController.forgotPassword(username, DbEventType.NONE);

            hideResetPasswordElements(true);

        }else {
            Toast.makeText(this, "Input valid username", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        finish();
        return true;
    }



    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        switch (v.getId()){
            case R.id.resetPasswordUsernameTextField:
                if (actionId == EditorInfo.IME_ACTION_DONE){
                    resetPasswordButtonClicked(null);
                }
                break;

            case R.id.confirmNewPasswordTextField:
                if (actionId == EditorInfo.IME_ACTION_DONE){
                    confirmResetPasswordButtonClicked(null);
                }
                break;
        }
        return true;
    }

    private void hideResetPasswordElements(boolean hide){
        if (hide){
            resetTextView.setVisibility(View.INVISIBLE);
            resetUsernameEditText.setVisibility(View.INVISIBLE);
            resetPasswordButton.setVisibility(View.INVISIBLE);

            resetUsernameEditText.setText("");

            confirmTextView.setVisibility(View.VISIBLE);
            confirmCodeEditText.setVisibility(View.VISIBLE);
            confirmPasswordEditText.setVisibility(View.VISIBLE);
            confirmPasswordButton.setVisibility(View.VISIBLE);
        }
        else{
            resetTextView.setVisibility(View.VISIBLE);
            resetUsernameEditText.setVisibility(View.VISIBLE);
            resetPasswordButton.setVisibility(View.VISIBLE);

            confirmTextView.setVisibility(View.INVISIBLE);
            confirmCodeEditText.setVisibility(View.INVISIBLE);
            confirmPasswordEditText.setVisibility(View.INVISIBLE);
            confirmPasswordButton.setVisibility(View.INVISIBLE);

            confirmPasswordEditText.setText("");
            confirmCodeEditText.setText("");
        }
    }

    private boolean isValidCode(String code) {
        if (5 < code.length() || 5 > code.length()){
            return false;
        }
        try{
            Integer.parseInt(code);
            return true;
        }catch (Exception e){
            return false;
        }
    }
}
