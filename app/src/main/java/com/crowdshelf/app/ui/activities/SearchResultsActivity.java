package com.crowdshelf.app.ui.activities;
import com.crowdshelf.app.io.DbEventOk;
import com.crowdshelf.app.io.DbEventType;
import com.crowdshelf.app.io.network.GetBookInfosBySearch;
import com.crowdshelf.app.models.Book;
import com.crowdshelf.app.models.BookInfo;
import com.crowdshelf.app.ui.adapter.SearchResultAdapter;
import com.squareup.otto.Subscribe;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.internal.view.menu.ActionMenuItemView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import io.realm.Realm;
import ntnu.stud.markul.crowdshelf.R;

public class SearchResultsActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{
    private Realm realm;
    private String TAG = "SearchResultsActivity";
    private SearchResultAdapter listAdapter;
    private ArrayList<BookInfo> searchResult;
    private boolean isLocalSearchActive;
    private String lastQuery;
    private ProgressBar pb;
    private MenuItem toggleSearchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        pb = (ProgressBar)findViewById(R.id.searchActivityProgressBar);
        toggleSearchButton = (MenuItem) findViewById(R.id.search_menu_toggle);
        MainTabbedActivity.getBus().register(this);
        realm = Realm.getDefaultInstance();
        searchResult = new ArrayList<>();
        listAdapter = new SearchResultAdapter(this, searchResult);
        isLocalSearchActive = true;
        ListView lv = (ListView) findViewById(R.id.searchResultListView);
        lv.setAdapter(listAdapter);
        lv.setOnItemClickListener(this);
        handleIntent(getIntent());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.search_menu_toggle:
                MainTabbedActivity.getMixpanel().track("SwitchedFilter");
                if (isLocalSearchActive){
                    changeSearchToInternet(lastQuery);
                    isLocalSearchActive = false;
                    ((ActionMenuItemView) findViewById(R.id.search_menu_toggle)).setTitle("Search Crowdshelf");
                }
                else{
                    changeSearchToLocal(lastQuery);
                    isLocalSearchActive = true;
                    ((ActionMenuItemView) findViewById(R.id.search_menu_toggle)).setTitle("Search online");

                }
            }
            return true;
    }

    private void changeSearchToLocal(String query) {
        pb.setVisibility(View.VISIBLE);
        Set<BookInfo> allBookInfos = new HashSet<>();
        Set<BookInfo> bookInfos = new HashSet<>();

        allBookInfos.addAll(realm.where(BookInfo.class)
                .contains("title", query, false)
                .or()
                .contains("author", query, false)
                .findAll());

        for (BookInfo bookInfo: allBookInfos) {
            for (Book book: MainTabbedActivity.userCrowdBooks){
                if (bookInfo.getIsbn().equals(book.getIsbn())){
                    bookInfos.add(bookInfo);
                    break;
                }
            }
        }

        searchResult.clear();
        searchResult.addAll(bookInfos);
        setTitle((String.valueOf(bookInfos.size()) + " results for \"" + query +"\""));

        pb.setVisibility(View.GONE);
        listAdapter.notifyDataSetChanged();
    }

    private void changeSearchToInternet(String query) {
        pb.setVisibility(View.VISIBLE);
        setTitle(("loading results for \"" + query + "\"..."));
        searchResult.clear();
        listAdapter.notifyDataSetChanged();
        GetBookInfosBySearch.getBookInfos(query, DbEventType.BOOKINFO_CHANGED);
    }


    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        pb.setVisibility(View.VISIBLE);
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            try {
                MainTabbedActivity.getMixpanel().track("BookSearch", new JSONObject().put("Search", query));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            lastQuery = query;
            setTitle(("loading results for \"" + query + "\"..."));
            if (isLocalSearchActive){
                changeSearchToLocal(query);
            }else {
                changeSearchToInternet(query);
            }
        }
    }

    @Subscribe
    public void handleDBEvents(DbEventOk event) {
        realm.refresh();
        Log.i(TAG, "Handle DB Event: " + event.getDbEventType());
        switch (event.getDbEventType()){
                case BOOKINFO_CHANGED:
                BookInfo bookInfo = realm.where(BookInfo.class)
                    .equalTo("isbn", event.getDbObjectId())
                    .findFirst();
                searchResult.add(bookInfo);
                pb.setVisibility(View.GONE);
                setTitle((String.valueOf(searchResult.size()) + " results for \"" + lastQuery + "\""));
                listAdapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> a, View v, final int position, long id) {
        BookInfo bookInfo = searchResult.get(position);
//        Toast.makeText(SearchResultsActivity.this, bookInfo.getTitle(), Toast.LENGTH_SHORT).show();
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