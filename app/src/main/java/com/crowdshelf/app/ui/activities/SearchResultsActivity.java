package com.crowdshelf.app.ui.activities;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.crowdshelf.app.MainController;
import com.crowdshelf.app.io.DbEvent;
import com.crowdshelf.app.io.DbEventType;
import com.crowdshelf.app.models.Book;
import com.crowdshelf.app.models.BookInfo;
import com.crowdshelf.app.ui.adapter.SearchResultAdapter;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.realm.Realm;
import ntnu.stud.markul.crowdshelf.R;

public class SearchResultsActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{
    private Realm realm;
    private String TAG = "SearchResultsActivity";
    private SearchResultAdapter listAdapter;
    private ArrayList<BookInfo> searchResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        MainTabbedActivity.getBus().register(this);
        realm = Realm.getDefaultInstance();
        searchResult = new ArrayList<>();
        listAdapter = new SearchResultAdapter(this, searchResult);
        ListView lv = (ListView) findViewById(R.id.searchResultListView);
        lv.setAdapter(listAdapter);
        lv.setOnItemClickListener(this);
        handleIntent(getIntent());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            TextView searchText = (TextView) findViewById(R.id.searchResultTextView);
            searchText.setText("Result for " + query);

            //use the query to search
            List<String> isbns = Arrays.asList("9780136042594", "9781847399304", "9783161484100", "9780670921607");
            for (String isbn : isbns) {
                MainController.getBookInfo(isbn, DbEventType.BOOKINFO_CHANGED);
            }
        }
    }

    @Subscribe
    public void handleDBEvents(DbEvent event) {
        realm.refresh();
        Log.i(TAG, "Handle DB Event: " + event.getDbEventType());
        switch (event.getDbEventType()){
                case BOOKINFO_CHANGED:
                BookInfo bookInfo = realm.where(BookInfo.class)
                    .equalTo("isbn", event.getDbObjectId())
                    .findFirst();
                searchResult.add(bookInfo);
                listAdapter.notifyDataSetChanged();
//                if (!searchResult.contains(bookInfo)) {
//                    searchResult.add(bookInfo);
//                    listAdapter.notifyDataSetChanged();
//                }
                break;

        }
    }

    @Override
    public void onItemClick(AdapterView<?> a, View v, final int position, long id) {
        BookInfo bookInfo = searchResult.get(position);
        Toast.makeText(SearchResultsActivity.this, bookInfo.getTitle(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, ViewBookActivity.class);
        intent.putExtra("isbn", bookInfo.getIsbn());
        startActivity(intent);
//        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
        MainTabbedActivity.getBus().unregister(this);
    }
}