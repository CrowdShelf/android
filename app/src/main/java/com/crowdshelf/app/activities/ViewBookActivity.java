package com.crowdshelf.app.activities;

import android.app.Activity;
import android.content.Intent;
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
public class ViewBookActivity extends Activity {

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

        // For testing, todo replace
        User mainUser = new User();

        /*
        Hide buttons that are not applicable based on whether the main user owns this book,
        rents it out, or borrows it from another user.
         */


        if (!mainUser.ownsBook(ISBN)) {
            // If you don't own the book then you can't remove it
            ((Button) findViewById(R.id.removeButton)).setVisibility(View.INVISIBLE);

        }
        // If you don't borrow the book then you can't return it
        if (!mainUser.rentsBook(ISBN)) {
            ((Button) findViewById(R.id.removeButton)).setVisibility(View.INVISIBLE);
        }
    }

    public void addButtonClick(View view) {
        // Add book to my shelf
        Toast.makeText(ViewBookActivity.this, "Add a book: " + ISBN, Toast.LENGTH_SHORT).show();
        MainController.createBook(ISBN, 1, 1);
        Intent intent = new Intent(this, ShelfActivity.class);
        intent.putExtra("ISBN", ISBN);
        startActivity(intent);
    }

    public void borrowButtonClick(View view) {
        // Borrow book from another user
        Toast.makeText(ViewBookActivity.this, "Borrow book: " + ISBN, Toast.LENGTH_SHORT).show();

        // todo switch to ViewUsersActivity
    }

    public void returnButtonClick(View view) {
        // Return a book you borrow to its owner
        Toast.makeText(ViewBookActivity.this, "Return book: " + ISBN, Toast.LENGTH_SHORT).show();
        // todo switch to ViewUsersActivity

    }

    public void removeButtonClick(View view) {
        // "remove book" not implemented in API yet, wait with implementing this method
        Toast.makeText(ViewBookActivity.this, "Remove book: " + ISBN, Toast.LENGTH_SHORT).show();
        // don't do anything here
    }

    // todo show the buttons below in GUI

    public void takeInReturn(View view) {
        // Take a book someone else borrows from you in return
        Toast.makeText(ViewBookActivity.this, "Take book in return: " + ISBN, Toast.LENGTH_SHORT).show();
        // todo switch to ViewUsersActivity
    }

    public void lendOut(View view) {
        // Lend out a book you own
        Toast.makeText(ViewBookActivity.this, "Lend book out: " + ISBN, Toast.LENGTH_SHORT).show();
        // todo switch to ViewUsersActivity
    }
}
