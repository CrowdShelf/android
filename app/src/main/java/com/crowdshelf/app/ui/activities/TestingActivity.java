package com.crowdshelf.app.ui.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.crowdshelf.app.io.DBEvent;
import com.crowdshelf.app.io.DBEventType;
import com.crowdshelf.app.io.network.NetworkController;
import com.crowdshelf.app.models.Book;
import com.crowdshelf.app.models.BookInfo;
import com.crowdshelf.app.models.Crowd;
import com.crowdshelf.app.models.User;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.squareup.otto.ThreadEnforcer;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import ntnu.stud.markul.crowdshelf.R;

public class TestingActivity extends AppCompatActivity {

    public TextView outputTextView;
    public EditText inputEditText;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing);
        inputEditText = (EditText) findViewById(R.id.inputTextView);
        outputTextView = (TextView) findViewById(R.id.resultTextView);

        MainTabbedActivity.getBus().register(this);
        realm = Realm.getDefaultInstance();
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

    @Subscribe
    public void handleTestResult(DBEvent event) {
        realm.refresh();
        switch (event.getDbEventType()) {
            case CROWD_CHANGED:
                String crowdId = event.getDbObjectId();
                Crowd crowd = realm.where(Crowd.class)
                        .equalTo("id", crowdId)
                        .findFirst();

                outputTextView.setText(crowd.getName());
                outputTextView.setText(crowd.getMembers().get(0).getId());
                break;
            case BOOK_CHANGED:
                String bookId = event.getDbObjectId();
                // Determine if you own the book you just scanned:
                Book book = realm.where(Book.class)
                        .equalTo("id", bookId)
                        .findFirst();
                outputTextView.setText(book.getIsbn());
                break;
            case USER_CHANGED:
                String userId = event.getDbObjectId();
                // Determine if you own the book you just scanned:
                User user = realm.where(User.class)
                        .equalTo("id", userId)
                        .findFirst();
                outputTextView.setText(user.getName());
                break;
        }
    }

    public void getCrowdOnClick(View v){
        NetworkController.getCrowd("55fee5bab379431100423434", DBEventType.CROWD_CHANGED);
    }

    public void getUserOnClick(View v){
        NetworkController.getUser("5603b4a4e4c6851100a24381", DBEventType.USER_CHANGED);
    }

    public void getBookOnClick(View v){
        NetworkController.getBook("56046eca62cc8e11003a7865", DBEventType.BOOK_CHANGED);
    }

    public void createCrowdOnClick(View v){
        Crowd crowd = new Crowd();
        crowd.setName("kekass");
        crowd.setOwner("5603b4a4e4c6851100a24381");
        NetworkController.createCrowd(crowd, DBEventType.CROWD_CHANGED);
    }

    public void createUserOnClick(View v){
        User user = new User();
        user.setName("jayson gason");
        user.setUsername("jayson");
        user.setEmail("jayson@gmail.com");
        NetworkController.createUser(user, DBEventType.USER_CHANGED);
    }

    public void createBookOnClick(View v){
        Book book = new Book();
        book.setOwner("5603b4a4e4c6851100a24381");
        book.setIsbn("9780552128484");
        NetworkController.createBook(book, DBEventType.BOOK_CHANGED);
    }

    @Override
    public void onDestroy() {
        realm.close();
        super.onDestroy();
    }
}
