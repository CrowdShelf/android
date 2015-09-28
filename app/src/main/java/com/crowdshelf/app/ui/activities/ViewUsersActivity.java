package com.crowdshelf.app.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

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
    etc.

    In other words, this screen should let you choose a user from a suitable list and
    return the selected user

     */

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.activity_scan_result);
    }
}
