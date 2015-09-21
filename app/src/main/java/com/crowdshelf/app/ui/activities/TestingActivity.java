package com.crowdshelf.app.ui.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import ntnu.stud.markul.crowdshelf.R;

public class TestingActivity extends AppCompatActivity {

    public TextView outputTextView;
    public EditText inputEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing);
        inputEditText = (EditText) findViewById(R.id.inputTextView);
        outputTextView = (TextView) findViewById(R.id.resultTextView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_crowd_testing, menu);
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

    public void crowdOnClick(View v){
        String input = String.valueOf(inputEditText.getText());

        String result = "crowd" + input;

        outputTextView.setText(result);


    }

    public void userOnClick(View v){
        String input = String.valueOf(inputEditText.getText());

        String result = "user" + input;

        outputTextView.setText(result);
    }

    public void bookOnClick(View v){

        String input = String.valueOf(inputEditText.getText());

        String result = "book" + input;

        outputTextView.setText(result);

    }
}
