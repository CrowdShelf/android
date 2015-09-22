package com.crowdshelf.app.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.crowdshelf.app.ScannedBookActions;
import com.crowdshelf.app.models.Book;
import com.crowdshelf.app.models.BookInfo;

import io.realm.Realm;
import ntnu.stud.markul.crowdshelf.R;

/**
 * Created by MortenAlver on 07.09.2015.
 */
public class ViewBookActivity extends Activity {
    private Realm realm;
    private BookInfo bookInfo;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_result);

        realm = Realm.getDefaultInstance();

        Intent intent = getIntent();
        String ISBN = intent.getStringExtra("ISBN");
        ScannedBookActions scannedBookAction = ScannedBookActions.fromValue(intent.getIntExtra("SCANNEDBOOKACTION", ScannedBookActions.UNKNOWN.value));
        String bookId = intent.getStringExtra("BOOKID");

        bookInfo = realm.where(BookInfo.class)
                .equalTo("isbn", ISBN)
                .findFirst();

        drawBookInfoUI(bookInfo);

        if (!bookId.equals("")) {
            Book b = realm.where(Book.class)
                    .equalTo("id", bookId)
                    .findFirst();
        }

        //TODO: Hide useless buttons
        switch (scannedBookAction) {
            case IS_OWNER:
                break;
            case IS_RENTING_BOOK:
                break;
            case NOT_OWNING_OR_RENTING:
                break;
        }
    }

    private void drawBookInfoUI(BookInfo bookInfo) {
        TextView titleTextView = (TextView)findViewById(R.id.titleView);

        ImageView imageView = (ImageView)findViewById(R.id.imageView);

        TextView infoTextView = (TextView)findViewById(R.id.infoView);

        titleTextView.setText(bookInfo.getTitle());
        Bitmap bitmap = BitmapFactory.decodeByteArray(bookInfo.getArtworkByteArray(), 0, bookInfo.getArtworkByteArray().length);
        imageView.setImageBitmap(bitmap);
        infoTextView.setText(bookInfo.getDescription());
    }

    public void setBook(String ISBN) {

        Log.i("ScanResult", ISBN);



        /*        // todo: Display a list of people who you can rent this book from
        List<Book> books = MainController.getBooksByIsbnOwnedByYourCrowds(ISBN);
        for (Book b : books) {
            b.getOwner();
        }
        /*

        /*
        Buttons to show and when to show them:
        -Borrow (borrow book from another user) - always
        -Take in return  - if you own the book and another user borrows a copy
        Lend out - if you own the book and have available copies
        -Add to shelf - if you don't own the book
        -Return - you borrow the book from someone else
        -Remove book (from your own shelf - how do we select specific copy if the owner has many?) - WAIT WITH IMPLEMENTATION
         */

    }

    public void addButtonClick(View view) {
        // Create new book object

        // Add book to my shelf
        Toast.makeText(ViewBookActivity.this, "Add a book: " + bookInfo.getIsbn(), Toast.LENGTH_SHORT).show();

        Intent returnIntent = new Intent();
        returnIntent.putExtra("result", ScannedBookActions.ADD_BUTTON_CLICKED.value);
        setResult(RESULT_OK, returnIntent);
        finish();
    }

    public void borrowButtonClick(View view) {
        // Borrow book from another user
        // Check if someone in your crowd owns this book, get that book object

        Toast.makeText(ViewBookActivity.this, "Borrow book: " + bookInfo.getIsbn(), Toast.LENGTH_SHORT).show();
        Intent returnIntent = new Intent();
        returnIntent.putExtra("result", ScannedBookActions.BORROW_BUTTON_CLICKED.value);
        setResult(RESULT_OK, returnIntent);
        finish();

        // todo switch to ViewUsersActivity
    }

    public void returnButtonClick(View view) {
        // Return a book you borrow to its owner
        // Get the book object WHERE rentedTo = mainUser AND isbn = ISBN
        Toast.makeText(ViewBookActivity.this, "Return book: " + bookInfo.getIsbn(), Toast.LENGTH_SHORT).show();
        Intent returnIntent = new Intent();
        returnIntent.putExtra("result", ScannedBookActions.RETURN_BUTTON_CLICKED.value);
        setResult(RESULT_OK, returnIntent);
        finish();
        // todo switch to ViewUsersActivity

    }

    public void removeButtonClick(View view) {
        // "remove book" not implemented in API yet, wait with implementing this method
        Toast.makeText(ViewBookActivity.this, "Remove book: " + bookInfo.getIsbn(), Toast.LENGTH_SHORT).show();
        // don't do anything here
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
}
