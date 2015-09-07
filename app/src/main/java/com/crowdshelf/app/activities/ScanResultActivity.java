package com.crowdshelf.app.activities;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.crowdshelf.app.HelperMethods;
import com.crowdshelf.app.bookInfo.GoogleBooksMain;
import com.crowdshelf.app.bookInfo.GoogleBooksVolumeInfo;
import com.squareup.picasso.Picasso;

import ntnu.stud.markul.crowdshelf.R;

/**
 * Created by MortenAlver on 07.09.2015.
 */
public class ScanResultActivity extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String ISBN = intent.getStringExtra("ISBN");
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
    }
}
