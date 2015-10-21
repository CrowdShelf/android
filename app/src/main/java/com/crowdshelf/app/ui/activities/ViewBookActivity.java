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
    private String isbn;
    private String bookID;
    private Book book;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_result);

        Button removeBookButton = (Button) findViewById(R.id.removeButton);
        Button returnBookButton = (Button) findViewById(R.id.returnButton);
        TextView infoTextView = (TextView) findViewById(R.id.infoView);
        infoTextView.setMovementMethod(new ScrollingMovementMethod());

        realm = Realm.getDefaultInstance();

        Intent intent = getIntent();
        isbn = intent.getStringExtra("isbn");
        bookID = intent.getStringExtra("bookID");
        String bookOwnerID = intent.getStringExtra("bookOwnerID");

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
        Toast.makeText(ViewBookActivity.this, "Book added", Toast.LENGTH_SHORT).show();

        MixpanelAPI mixpanel = MixpanelAPI.getInstance(this, MainTabbedActivity.getProjectToken());
        mixpanel.track("BookAdded");

        Book b1 = new Book();
        b1.setIsbn(isbn);
        b1.setOwner(MainTabbedActivity.getMainUserId());
        b1.setAvailableForRent(true);
        MainController.createBook(b1, DbEventType.USER_BOOKS_CHANGED);

        finish();
    }

    public void borrowButtonClick(View view) {
//        Toast.makeText(ViewBookActivity.this, "Borrow book: " + bookInfo.getIsbn(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, UserListActivity.class);
        intent.putExtra("isbn", bookInfo.getIsbn());
        startActivityForResult(intent, BORROW_BOOK_ACTION);

    }

    public void returnButtonClick(View view) {
        Toast.makeText(ViewBookActivity.this, "Returned book: " + bookInfo.getTitle(), Toast.LENGTH_SHORT).show();
        MainController.removeRenter(bookID, MainTabbedActivity.getMainUserId(), DbEventType.USER_BOOKS_CHANGED);
        Intent returnIntent = new Intent();
        setResult(RESULT_OK, returnIntent);
        finish();
    }

    public void removeButtonClick(View view) {
        Toast.makeText(ViewBookActivity.this, "Book removed", Toast.LENGTH_SHORT).show();

        MainController.removeBook(book.getId(), DbEventType.USER_BOOKS_CHANGED);

        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(TAG, "onActivityResult");
        switch (requestCode) {
            case BORROW_BOOK_ACTION:
                if (resultCode == RESULT_OK) {
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
