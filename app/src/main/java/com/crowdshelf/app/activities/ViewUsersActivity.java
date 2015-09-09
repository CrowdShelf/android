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
 * Created by Torstein on 07.09.2015.
 */
public class ViewUsersActivity extends Activity {
    /*
    This screen should show a list of users.
    You should be sent to this screen when:
    -you want to borrow a book and you have to choose who to borrow it from
    -you want to lend a book out and you have to choose who to lend it to
    -you want to take a book in return and you have to choose who returned it
    etc
     */

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.activity_scan_result);
    }
}
