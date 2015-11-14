package com.crowdshelf.app.ui.activities;

import com.crowdshelf.app.MainController;
import com.crowdshelf.app.io.DbEventType;
import com.crowdshelf.app.models.Book;
import com.crowdshelf.app.models.BookInfo;
import com.crowdshelf.app.models.User;
import com.crowdshelf.app.ui.adapter.UserListAdapter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import ntnu.stud.markul.crowdshelf.R;

/**
 * Created by MortenAlver on 07.09.2015.
 */
public class ViewBookActivity extends ActionBarActivity implements AdapterView.OnItemClickListener {
    private static final String TAG = "ViewBookActivity";
    private static final int BORROW_BOOK_ACTION = 0;
    private Realm realm;
    private BookInfo bookInfo;
    private String isbn;
    private UserListAdapter listAdapter;
    private UserListAdapter borrowedFromListAdapter;
    private ArrayList<User> renters = new ArrayList<>();
    private ArrayList<User> owners = new ArrayList<>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_result);

        Button removeBookButton = (Button) findViewById(R.id.removeButton);
        Button returnBookButton = (Button) findViewById(R.id.returnButton);
        TextView infoTextView = (TextView) findViewById(R.id.infoView);
        infoTextView.setMovementMethod(new ScrollingMovementMethod());

        realm = Realm.getDefaultInstance();
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        Intent intent = getIntent();
        isbn = intent.getStringExtra("isbn");

        listAdapter = new UserListAdapter(this, renters);
        ListView lv = (ListView)findViewById(R.id.rentedToListView);
        lv.setAdapter(listAdapter);

        borrowedFromListAdapter = new UserListAdapter(this, owners);
        ListView lv2 = (ListView)findViewById(R.id.borrowedFromListView);
        lv2.setAdapter(borrowedFromListAdapter);
        lv2.setOnItemClickListener(this);

        if (isbn != null) {
            if (isbn.equals("")) {
                Log.w(TAG, "Got called without isbn!");
            }
            returnBookButton.setVisibility(View.GONE);
            removeBookButton.setVisibility(View.GONE);

            // Check if mainUser borrows the book
            Book rentedToMainUser = realm.where(Book.class)
                    .equalTo("isbn", isbn)
                    .equalTo("rentedTo", MainTabbedActivity.getMainUserId())
                    .findFirst();


            // Check if mainUser owns the book
            Book ownedBook = realm.where(Book.class)
                    .equalTo("isbn", isbn)
                    .equalTo("owner", MainTabbedActivity.getMainUserId())
                    .findFirst();
            if (ownedBook != null){
                removeBookButton.setVisibility(View.VISIBLE);
            }

            // Get all users borrowing the book from mainUser
            List<Book> rentedBooks= realm.where(Book.class)
                    .equalTo("isbn", isbn)
                    .notEqualTo("rentedTo", "")
                    .findAll();
            if (!rentedBooks.isEmpty()){
                for (Book b : rentedBooks){
                    if (MainTabbedActivity.getMainUserId().equals(b.getOwner())){
                        User renter = realm.where(User.class)
                                .equalTo("id", b.getRentedTo())
                                .findFirst();
                        renters.add(renter);
                    }
                }
                if (!renters.isEmpty()){
                    if (renters.size() > 1){
                        TextView ownersText = (TextView) findViewById(R.id.rentersTextLink);
                        ownersText.setText("Borrowed to ");
                        Spannable blueName = new SpannableString(renters.get(0).getName());
                        blueName.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.linkColor)), 0, blueName.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        ownersText.append(blueName);
                        ownersText.append(" and ");
                        Spannable blueOthers = new SpannableString((renters.size()-1) + " others");
                        blueOthers.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.linkColor)), 0, blueOthers.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        ownersText.append(blueOthers);
                        lv.setVisibility(View.GONE);
                        findViewById(R.id.borrowedToText).setVisibility(View.GONE);
                        ownersText.setVisibility(View.VISIBLE);
                    }
                    else {
                        lv.setVisibility(View.VISIBLE);
                        findViewById(R.id.borrowedToText).setVisibility(View.VISIBLE);
                        listAdapter.notifyDataSetChanged();
                    }
                }
                else{
                    findViewById(R.id.borrowedToText).setVisibility(View.GONE);
                    lv.setVisibility(View.GONE);
                }
            }

            // Get all the mainUser borrows the book from
            if (!rentedBooks.isEmpty()){
                for (Book b : rentedBooks){
                    if (MainTabbedActivity.getMainUserId().equals(b.getRentedTo())){
                        User owner = realm.where(User.class)
                                .equalTo("id", b.getOwner())
                                .findFirst();
                        owners.add(owner);
                    }
                }
                if (!owners.isEmpty()){
                    if (owners.size() > 1){
                        TextView ownersText = (TextView) findViewById(R.id.ownersTextLink);
                        ownersText.setText("Borrowed from ");

                        Spannable blueName = new SpannableString(owners.get(0).getName());
                        blueName.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.linkColor)), 0, blueName.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        ownersText.append(blueName);
                        ownersText.append(" and ");
                        Spannable blueOthers = new SpannableString(owners.size()-1 + " others");
                        blueOthers.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.linkColor)), 0, blueOthers.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        ownersText.append(blueOthers);

                        lv2.setVisibility(View.GONE);
                        findViewById(R.id.borrowedFromText).setVisibility(View.GONE);
                        ownersText.setVisibility(View.VISIBLE);
                    }
                    else{
                        lv2.setVisibility(View.VISIBLE);
                        findViewById(R.id.borrowedFromText).setVisibility(View.VISIBLE);
                        borrowedFromListAdapter.notifyDataSetChanged();
                    }
                }
            }


            bookInfo = realm.where(BookInfo.class)
                    .equalTo("isbn", isbn)
                    .findFirst();
        }
        if (bookInfo != null) {
            drawBookInfoUI(bookInfo);
            setTitle(bookInfo.getTitle());
            if (bookInfo.getTitle().equals("not found")){
                findViewById(R.id.addButton).setVisibility(View.INVISIBLE);
                findViewById(R.id.borrowButton).setVisibility(View.INVISIBLE);
                removeBookButton.setVisibility(View.INVISIBLE);
            }
        }
        else{
            super.onBackPressed();
        }

    }



    private void drawBookInfoUI(BookInfo bookInfo) {
        TextView titleTextView = (TextView)findViewById(R.id.titleView);
        ImageView imageView = (ImageView)findViewById(R.id.imageView);
        TextView infoTextView = (TextView)findViewById(R.id.infoView);
        TextView authorView = (TextView)findViewById(R.id.authorView);
        titleTextView.setText(bookInfo.getTitle());
        infoTextView.setText(bookInfo.getDescription());
        Bitmap bitmap = BitmapFactory.decodeByteArray(bookInfo.getArtworkByteArray(), 0, bookInfo.getArtworkByteArray().length);
        imageView.setImageBitmap(bitmap);
        authorView.setText(bookInfo.getAuthor());
    }

    public void addButtonClick(View view) {
        Toast.makeText(ViewBookActivity.this, "Book added", Toast.LENGTH_SHORT).show();

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
        Book rentedBook = realm.where(Book.class)
                .equalTo("isbn", isbn)
                .equalTo("rentedTo", MainTabbedActivity.getMainUserId())
                .findFirst();
        MainController.removeRenter(rentedBook.getId(), MainTabbedActivity.getMainUserId(), DbEventType.USER_BOOKS_CHANGED);
        Intent returnIntent = new Intent();
        setResult(RESULT_OK, returnIntent);
        finish();
    }

    public void removeButtonClick(View view) {
        Toast.makeText(ViewBookActivity.this, "Book removed", Toast.LENGTH_SHORT).show();
        Book ownedBook = realm.where(Book.class)
                .equalTo("isbn",isbn)
                .equalTo("owner", MainTabbedActivity.getMainUserId())
                .findFirst();
        MainController.removeBook(ownedBook.getId(), DbEventType.USER_BOOKS_CHANGED);

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
    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), MainTabbedActivity.class);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivityForResult(myIntent, 0);
        finish();
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> a, View v, int position, long id) {
        User u = owners.get(position);
        Book rentedBook = realm.where(Book.class)
                .equalTo("isbn", isbn)
                .equalTo("rentedTo", MainTabbedActivity.getMainUserId())
                .equalTo("owner", u.getId())
                .findFirst();
        MainController.removeRenter(rentedBook.getId(), MainTabbedActivity.getMainUserId(), DbEventType.USER_BOOKS_CHANGED);
        Toast.makeText(ViewBookActivity.this, bookInfo.getTitle() + " was returned", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy: realm, super");
        realm.close();
        super.onDestroy();
    }

    public void seeAllOwnersClicked(View view) {
        Intent intent = new Intent(this, OwnerListActivity.class);
        intent.putExtra("isbn", bookInfo.getIsbn());
        startActivityForResult(intent, BORROW_BOOK_ACTION);
    }

    public void seeAllRentersClicked(View view) {
        Intent intent = new Intent(this, RenterListActivity.class);
        intent.putExtra("isbn", bookInfo.getIsbn());
        startActivityForResult(intent, BORROW_BOOK_ACTION);
    }
}
