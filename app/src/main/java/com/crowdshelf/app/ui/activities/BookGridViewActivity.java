package com.crowdshelf.app.ui.activities;

import com.crowdshelf.app.MainController;
import com.crowdshelf.app.io.DbEvent;
import com.crowdshelf.app.io.DbEventType;
import com.crowdshelf.app.models.Book;
import com.crowdshelf.app.models.Crowd;
import com.crowdshelf.app.models.MemberId;
import com.crowdshelf.app.models.User;
import com.crowdshelf.app.ui.fragments.BookGridViewFragment;
import com.squareup.otto.Subscribe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.realm.Realm;
import ntnu.stud.markul.crowdshelf.R;

public class BookGridViewActivity extends AppCompatActivity implements BookGridViewFragment.OnBookGridViewFragmentInteractionListener {

    private Realm realm;
    private String TAG = "BookGridViewActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_grid_view);
        Intent intent = getIntent();
        String shelf = intent.getStringExtra("shelf");
        String userID = intent.getStringExtra("userID");
        realm = Realm.getDefaultInstance();
        BookGridViewFragment gridViewFragment = (BookGridViewFragment) getSupportFragmentManager().findFragmentById(R.id.bookGridViewActivityFragment);

        Set<Book> books = new HashSet<>();
        switch (shelf) {
            case "RentedOut":
                setTitle("Rented out Books");
                books.addAll(realm.where(Book.class)
                        .equalTo("owner", MainTabbedActivity.getMainUserId())
                        .notEqualTo("rentedTo", "")
                        .findAll());
                break;
            case "Borrowed":
                setTitle("Borrowed Books");
                books.addAll(realm.where(Book.class)
                        .equalTo("rentedTo", MainTabbedActivity.getMainUserId())
                        .findAll());

                break;
            case "Owned":
                setTitle("Owned Books");
                books.addAll(realm.where(Book.class)
                        .equalTo("owner", MainTabbedActivity.getMainUserId())
                        .findAll());

                break;
            default:
                if (userID == null) {
                    setTitle("Crowd Books");
                    String crowdID = shelf;
                    Crowd crowd = realm.where(Crowd.class)
                            .equalTo("id", crowdID)
                            .findFirst();
                    for (MemberId u : crowd.getMembers()) {
                        books.addAll(realm.where(Book.class)
                                .equalTo("owner", u.getId())
                                .findAll());
                    }
                }
                else {
                    books.addAll(realm.where(Book.class)
                            .equalTo("owner", userID)
                            .findAll());
                }
        }

        gridViewFragment.setmItems(books);
    }

    @Override
    public void itemInBookGridViewClicked(String bookID) {
        Log.i(TAG, "bookID clicked: " + bookID);
        Book b = realm.where(Book.class).equalTo("id", bookID).findFirst();

        Intent intent = new Intent(this, ViewBookActivity.class);
        intent.putExtra("isbn", b.getIsbn());
        intent.putExtra("bookID", bookID);
        intent.putExtra("bookOwnerID", b.getOwner());
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}
