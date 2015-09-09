package com.crowdshelf.app.activities;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.crowdshelf.app.HelperMethods;
import com.crowdshelf.app.MainController;
import com.crowdshelf.app.bookInfo.GoogleBooksMain;
import com.crowdshelf.app.bookInfo.GoogleBooksVolumeInfo;
import com.crowdshelf.app.models.Book;
import com.crowdshelf.app.models.User;
import com.squareup.picasso.Picasso;

import java.util.List;

import ntnu.stud.markul.crowdshelf.R;

/**
 * Created by MortenAlver on 07.09.2015.
 */
public class ScanResultActivity extends Activity {

    private String ISBN = "";
    //private User mainUser = new User("Morten"); // TODO: Only for testing

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        ISBN = intent.getStringExtra("ISBN");
        setContentView(R.layout.activity_scan_result);

        Log.i("ScanResult", ISBN);
        String jsonAsStringFromISBN = HelperMethods.getJsonFromGoogleBooksApiUsingISBN(ISBN);

        GoogleBooksMain googleBooksMain = HelperMethods.convertGoogleBooksJsonStringToObject(jsonAsStringFromISBN);

        assert googleBooksMain != null;
        if (googleBooksMain.getTotalItems() > 0){
            GoogleBooksVolumeInfo a = googleBooksMain.getItems().get(0).getVolumeInfo();

            String bookTitle = a.getTitle();
            TextView titleText = (TextView)findViewById(R.id.titleView);
            titleText.setText(bookTitle);

            String bookThumbnail = a.getImageLinks().getThumbnail();
            ImageView imageView = (ImageView)findViewById(R.id.imageView);
            Picasso.with(this)
                    .load(bookThumbnail)
                    .resize(250, 400)
                    .into(imageView);

            String bookInfo = a.getDescription();
            TextView infoText = (TextView)findViewById(R.id.infoView);
            infoText.setText(bookInfo);
        }
        /*
        Buttons to show and when to show them:
        Borrow (borrow book from another user) - always
        Take in return - if you own the book and another user borrows a copy book
        Lend out - if you own the book and have available copies
        Add to shelf - if you don't own the book
         */

        // Check if you own this book
        if (true) {
            ((Button) findViewById(R.id.removeButton)).setVisibility(View.INVISIBLE);
        }
    }

    public void addButtonClick(View view) {
        // Add book to my shelf
        Toast.makeText(ScanResultActivity.this, "Add a book: " + ISBN, Toast.LENGTH_SHORT).show();
        MainController.createBook(ISBN, 1, 1);
    }

    public void borrowButtonClick(View view) {
        // Borrow book from another user
        Toast.makeText(ScanResultActivity.this, "Borrow book: " + ISBN, Toast.LENGTH_SHORT).show();
        List<Book> books = MainController.getBooksByIsbnOwnedByYourCrowds(ISBN);
    }

    public void returnButtonClick(View view) {
        // Return a book you borrow to its owner
        Toast.makeText(ScanResultActivity.this, "Return book: " + ISBN, Toast.LENGTH_SHORT).show();
    }

    public void removeButtonClick(View view) {
        // "remove book" not implemented in API yet, wait with implementing this method
        Toast.makeText(ScanResultActivity.this, "Remove book: " + ISBN, Toast.LENGTH_SHORT).show();
        MainController.getBookByIsbnOwner(ISBN, "Morten"); // TODO: change to mainUser.getUsername());
    }

    public void takeInReturn(View view) {
        // Take a book someone else borrows from you in return
    }
}
