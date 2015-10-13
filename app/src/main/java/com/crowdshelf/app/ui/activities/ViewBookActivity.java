package com.crowdshelf.app.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.crowdshelf.app.MainController;
import com.crowdshelf.app.ScannedBookActions;
import com.crowdshelf.app.io.DbEventType;
import com.crowdshelf.app.models.Book;
import com.crowdshelf.app.models.BookInfo;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import java.util.List;

import io.realm.Realm;
import ntnu.stud.markul.crowdshelf.R;

/**
 * Created by MortenAlver on 07.09.2015.
 */
public class ViewBookActivity extends Activity {
    private static final String TAG = "ViewBookActivity";
    private static final int BORROW_BOOK_ACTION = 0;
    private Realm realm;
    private BookInfo bookInfo;
    private List<Book> books;
    private String isbn;
    private String bookID;
    private Book book;
    private String bookOwnerID;
    private Button removeBookButton;
    private Button returnBookButton;
    private TextView infoTextView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_result);

        removeBookButton = (Button)findViewById(R.id.removeButton);
        returnBookButton = (Button)findViewById(R.id.returnButton);
        infoTextView = (TextView)findViewById(R.id.infoView);
        infoTextView.setMovementMethod(new ScrollingMovementMethod());

        realm = Realm.getDefaultInstance();

        Intent intent = getIntent();
        isbn = intent.getStringExtra("isbn");
        bookID = intent.getStringExtra("bookID");
        bookOwnerID = intent.getStringExtra("bookOwnerID");

        if (isbn != null) {
            if (isbn.equals("")) {
                Log.w(TAG, "Got called without isbn!");
            }
            returnBookButton.setVisibility(View.GONE);
            removeBookButton.setVisibility(View.GONE);

            bookInfo = realm.where(BookInfo.class)
                    .equalTo("isbn", isbn)
                    .findFirst();
        } else if (bookID != null) {
            book = realm.where(Book.class)
                    .equalTo("id", bookID)
                    .findFirst();
            bookInfo = realm.where(BookInfo.class)
                    .equalTo("isbn", book.getIsbn())
                    .findFirst();

            if (bookOwnerID.equals(MainTabbedActivity.getMainUserId())){
                returnBookButton.setVisibility(View.GONE);
                removeBookButton.setVisibility(View.VISIBLE);
            }else{
                returnBookButton.setVisibility(View.VISIBLE);
                removeBookButton.setVisibility(View.GONE);
            }

        }
        drawBookInfoUI(bookInfo);


        /*
        Buttons to show and when to show them:
        - Borrow (borrow book from another user): always
        - Take in return: if you own the book and another user borrows a copy
        - Lend out: if you own the book and have available copies
        - Add to shelf: if you don't own the book
        - Return: you borrow the book from someone else
        - Remove book (from your own shelf - how do we select specific copy if the owner has many?): WAIT WITH IMPLEMENTATION
         */
    }

    private void drawBookInfoUI(BookInfo bookInfo) {
        TextView titleTextView = (TextView)findViewById(R.id.titleView);
        ImageView imageView = (ImageView)findViewById(R.id.imageView);
        TextView infoTextView = (TextView)findViewById(R.id.infoView);
        titleTextView.setText(bookInfo.getTitle());
        infoTextView.setText(bookInfo.getDescription());
        Bitmap bitmap = BitmapFactory.decodeByteArray(bookInfo.getArtworkByteArray(), 0, bookInfo.getArtworkByteArray().length);
        imageView.setImageBitmap(bitmap);
    }

    public void addButtonClick(View view) {
        // Create new book object
        // Add book to my shelf
        Toast.makeText(ViewBookActivity.this, "Book added", Toast.LENGTH_SHORT).show();

        MixpanelAPI mixpanel = MixpanelAPI.getInstance(this, MainTabbedActivity.getProjectToken());
        mixpanel.track("BookAdded");

        Intent returnIntent = new Intent();
        returnIntent.putExtra("result", ScannedBookActions.ADD_BUTTON_CLICKED.value);
        setResult(RESULT_OK, returnIntent);
        finish();
    }

    public void borrowButtonClick(View view) {
        // Borrow book from another user
        // Check if someone in your crowd owns this book, get that book object

        Toast.makeText(ViewBookActivity.this, "Borrow book: " + bookInfo.getIsbn(), Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, UserListActivity.class);
        intent.putExtra("isbn", bookInfo.getIsbn());
        startActivityForResult(intent, BORROW_BOOK_ACTION);

//        Intent returnIntent = new Intent();
//        returnIntent.putExtra("result", ScannedBookActions.BORROW_BUTTON_CLICKED.value);
//        setResult(RESULT_OK, returnIntent);
//        finish();

        // todo switch to ViewUsersActivity
    }

    public void returnButtonClick(View view) {
        // Return a book you borrow to its owner
        // Get the book object WHERE rentedTo = mainUser AND isbn = isbn
        Toast.makeText(ViewBookActivity.this, "Returned book: " + bookInfo.getTitle(), Toast.LENGTH_SHORT).show();
        MainController.removeRenter(bookID, MainTabbedActivity.getMainUserId(), DbEventType.USER_BOOKS_CHANGED);
        Intent returnIntent = new Intent();
        setResult(RESULT_OK, returnIntent);
        finish();
        // todo switch to ViewUsersActivity

    }

    public void removeButtonClick(View view) {
        // "remove book" not implemented in API yet, wait with implementing this method
        Toast.makeText(ViewBookActivity.this, "Book removed", Toast.LENGTH_SHORT).show();
        // don't do anything here

        /*
        todo: Here we should figure out which Book (e.g.) bookId to delete. There may be
        many options.
         */

        Intent returnIntent = new Intent();
        returnIntent.putExtra("result", ScannedBookActions.REMOVE_BUTTON_CLICKED.value);
        if (book != null){
            returnIntent.putExtra("bookID", book.getId());
        }
        setResult(RESULT_OK, returnIntent);
        finish();
    }

    // todo show the buttons below in GUI

    public void takeInReturn(View view) {
        // Take a book someone else borrows from you in return
        // get book object where Owner = mainUser AND rentedTo != null
        Toast.makeText(ViewBookActivity.this, "Take book in return: " + bookInfo.getIsbn(), Toast.LENGTH_SHORT).show();
        // todo switch to ViewUsersActivity
    }

    public void lendOut(View view) {
        // Lend out a book you own
        // get book object where Owner = mainUser AND rentedTo == null
        Toast.makeText(ViewBookActivity.this, "Lend book out: " + bookInfo.getIsbn(), Toast.LENGTH_SHORT).show();
        // todo switch to ViewUsersActivity
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(TAG, "onActivityResult");
        switch (requestCode) {
            case BORROW_BOOK_ACTION:
                if (resultCode == RESULT_OK) {
                    String userName = data.getStringExtra("userName");
                    Toast.makeText(getBaseContext(), "Rented book from: " + userName, Toast.LENGTH_SHORT).show();
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("result", ScannedBookActions.BOOK_BORROWED.value);
                    setResult(RESULT_OK, returnIntent);
                    finish();
                }

                break;
        }
    }



    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy: realm, super");
        realm.close();
        super.onDestroy();
    }
}
