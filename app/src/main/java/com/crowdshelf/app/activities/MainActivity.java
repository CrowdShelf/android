package com.crowdshelf.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.crowdshelf.app.bookInfo.GoogleBooksVolumeInfo;
import com.crowdshelf.app.emailService.GMailSender;
import com.crowdshelf.app.GridViewAdapter;
import com.crowdshelf.app.HelperMethods;
import com.crowdshelf.app.bookInfo.GoogleBooksMain;

import java.util.ArrayList;

import ntnu.stud.markul.crowdshelf.R;

public class MainActivity extends AppCompatActivity {
    private final String MIXPANEL_TOKEN = "93ef1952b96d0faa696176aadc2fbed4";

    private GMailSender gMailSender;
//    private MixpanelAPI mixpanel;

    private static GridViewAdapter gridViewAdapter;
    private static ArrayList<GoogleBooksVolumeInfo> booksAddedArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        booksAddedArrayList = new ArrayList<>();

        gridViewAdapter = new GridViewAdapter(MainActivity.this, R.layout.book_single, booksAddedArrayList);
        GridView gridView = (GridView) findViewById(R.id.gridView);
        gridView.setAdapter(gridViewAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(MainActivity.this, "You removed " + booksAddedArrayList.get(position).getTitle(), Toast.LENGTH_SHORT).show();
                booksAddedArrayList.remove(position);
                gridViewAdapter.notifyDataSetChanged();



            /*
            @TODO
            On startup do:
            User mainUser = MainController.getUser("username");
            mainUser.getCrowds(); // To download all the crowds of the main user, the members of these crowds, and their books
             */

            }
        });


//        //Setup Mixpanel
//        if (mixpanel == null) {
//            mixpanel = MixpanelAPI.getInstance(this, MIXPANEL_TOKEN);
//        }

        //Must be set to allow HTTPRequests
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    public void openScannerButtonPressed(View view) {
        Intent intent = new Intent(this, ScannerActivity.class);
        startActivity(intent);
    }

    public void sendMailButtonPressed(View view) {
        bookScanned("9780670921607");
//        sendMail("This is a subject", "This is the main text of the email", "kongen@slottet.no", "crowdshelfmail@gmail.com");
//        mixpanel.track("SendMailButtonPressed");
    }

    //Example of how mail can be sent
    public void sendMail(String subject, String body, String senderEmail, String recipients) {
        try {
            if (gMailSender == null)
                setUpGMailSender("crowdshelfmail@gmail.com", "kundestyrt");

            gMailSender.sendMail(subject, body, senderEmail, recipients);
//            mixpanel.track("MailSentSuccessfully");
        } catch (Exception e) {
            Log.e("SendMail", e.getMessage(), e);
        }
    }

    private void setUpGMailSender(String user, String password) {
        gMailSender = new GMailSender(user, password);
    }

    public static void bookScanned(String isbn) {
        String jsonAsStringFromISBN = HelperMethods.getJsonFromGoogleBooksApiUsingISBN(isbn);

        GoogleBooksMain googleBooksMain = HelperMethods.convertGoogleBooksJsonStringToObject(jsonAsStringFromISBN);

        assert googleBooksMain != null;
        if (googleBooksMain.getTotalItems() > 0){
            GoogleBooksVolumeInfo a = googleBooksMain.getItems().get(0).getVolumeInfo();

            booksAddedArrayList.add(a);
            gridViewAdapter.notifyDataSetChanged();
        }

    }
}
