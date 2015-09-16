package com.crowdshelf.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.crowdshelf.app.GridViewAdapter;
import com.crowdshelf.app.HelperMethods;
import com.crowdshelf.app.bookInfo.GoogleBooksMain;
import com.crowdshelf.app.bookInfo.GoogleBooksVolumeInfo;
import com.crowdshelf.app.models.Book;
import com.crowdshelf.app.models.BookOwner;

import java.util.ArrayList;
import java.util.List;

import ntnu.stud.markul.crowdshelf.R;

public class ShelfActivity extends AppCompatActivity {

    private static GridViewAdapter gridViewAdapter;
    private static GridView gridView;
    private static ArrayList<GoogleBooksVolumeInfo> booksAddedArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initiate();
        displayBooksInShelf(MainActivity.getMainUser()); // Todo: Move to another activity
    }

    public static void displayBooksInShelf(BookOwner bookOwner) {
        List<Book> books = bookOwner.getBooks();
        addBookToShelf("9780670921607");    // Todo: Remove. For testing while user have no books
        for (Book book : books) {
            addBookToShelf(book.getIsbn());
            //Toast.makeText(ShelfActivity.this, "You tried to add " + book.getIsbn(), Toast.LENGTH_SHORT).show();
        }
    }

    private void initiate(){
        Intent intent = getIntent();
        String ISBN = intent.getStringExtra("ISBN");
        setContentView(R.layout.activity_shelf);

        booksAddedArrayList = new ArrayList<>();
//        gridViewAdapter = new GridViewAdapter(ShelfActivity.this, R.layout.book_single, booksAddedArrayList);
        gridView = (GridView) findViewById(R.id.gridViewShelf);
        gridView.setAdapter(gridViewAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(ShelfActivity.this, "You removed " + booksAddedArrayList.get(position).getTitle(), Toast.LENGTH_SHORT).show();
                booksAddedArrayList.remove(position);
                gridViewAdapter.notifyDataSetChanged();
            }
        });
    }

     public static void addBookToShelf(String isbn) {
         String jsonAsStringFromISBN = HelperMethods.getJsonFromGoogleBooksApiUsingISBN(isbn);
         GoogleBooksMain googleBooksMain = HelperMethods.convertGoogleBooksJsonStringToObject(jsonAsStringFromISBN);

         assert googleBooksMain != null;
         if (googleBooksMain.getTotalItems() > 0) {
             GoogleBooksVolumeInfo a = googleBooksMain.getItems().get(0).getVolumeInfo();

             booksAddedArrayList.add(a);
             gridViewAdapter.notifyDataSetChanged();
         }

     }
 }
