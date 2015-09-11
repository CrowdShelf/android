package com.crowdshelf.app.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.crowdshelf.app.GridViewAdapter;
import com.crowdshelf.app.HelperMethods;
import com.crowdshelf.app.bookInfo.GoogleBooksMain;
import com.crowdshelf.app.bookInfo.GoogleBooksVolumeInfo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ntnu.stud.markul.crowdshelf.R;

public class ShelfActivity extends AppCompatActivity {

    private static GridViewAdapter gridViewAdapter;
    private static ArrayList<GoogleBooksVolumeInfo> booksAddedArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String ISBN = intent.getStringExtra("ISBN");
        setContentView(R.layout.activity_shelf);

        booksAddedArrayList = new ArrayList<>();
        gridViewAdapter = new GridViewAdapter(ShelfActivity.this, R.layout.book_single, booksAddedArrayList);
        GridView gridView = (GridView) findViewById(R.id.gridViewShelf);
        gridView.setAdapter(gridViewAdapter);
        setContentView(R.layout.activity_shelf);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(ShelfActivity.this, "You removed " + booksAddedArrayList.get(position).getTitle(), Toast.LENGTH_SHORT).show();
                booksAddedArrayList.remove(position);
                gridViewAdapter.notifyDataSetChanged();
            }
        });

        // Get the isbn of the books in the shelf
        List<String> isbns = Arrays.asList("9780670921607");
        for (String isbn : isbns) {
            addBookToShelf(isbn);
        }
    }

             @Override
             public boolean onCreateOptionsMenu(Menu menu) {
                 // Inflate the menu; this adds items to the action bar if it is present.
                 getMenuInflater().inflate(R.menu.menu_shelf, menu);
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
