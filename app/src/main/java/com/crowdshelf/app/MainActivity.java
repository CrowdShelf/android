package com.crowdshelf.app;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import ntnu.stud.markul.crowdshelf.R;
import com.crowdshelf.app.jsonModels.GoogleBooksJSON;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {
    private final String MIXPANEL_TOKEN = "93ef1952b96d0faa696176aadc2fbed4";

    private GMailSender gMailSender;
    private MixpanelAPI mixpanel;
    private String hardcodedJsonExample = "{\"kind\":\"books#volumes\",\"totalItems\":1,\"items\":[{\"kind\":\"books#volume\",\"id\":\"e554PwAACAAJ\",\"etag\":\"O1S0xxNxRkw\",\"selfLink\":\"https://www.googleapis.com/books/v1/volumes/e554PwAACAAJ\",\"volumeInfo\":{\"title\":\"The Bro Code\",\"authors\":[\"Barney Stinson\",\"Niel Patrick Harris\",\"Matt Kuhn\"],\"publisher\":\"Pocket Books\",\"publishedDate\":\"2009-10-01\",\"description\":\"THE BRO CODE provides men with all the rules they need to know in order to become a \\\"bro\\\" and behave properly among other bros. THE BRO CODE has never been published before. Few know of its existence, and the code, until now, has been verbally communicated between those in the 'bro'. Containing approximately 150 \\\"unspoken\\\" rules, this code of conduct ranges from the simple (bros before hos) to the complex (the hot-to-crazy ratio, complete with bar graphs and charts). With helpful sidebros THE BRO CODE will help any ordinary guy become the best bro he can be. Let ultimate bro and co-author Barney Stinson and his book, THE BRO CODE share their wisdom, lest you be caught making eye contact in a devil's three-way (two dudes, duh.) Sample Articles from THE BRO CODE: Article 1: Regardless of veracity, a Bro never admits familiarity with a Broadway show or musical. Article 53: A Bro will, whenever possible, provide his Bro with prophylactic protection.Article 57: A Bro may not speculate on the expected Bro / chick ratio of a party or venue without first disclosing the present-time observed ratio.\",\"industryIdentifiers\":[{\"type\":\"ISBN_10\",\"identifier\":\"1847399304\"},{\"type\":\"ISBN_13\",\"identifier\":\"9781847399304\"}],\"readingModes\":{\"text\":false,\"image\":false},\"pageCount\":192,\"printType\":\"BOOK\",\"categories\":[\"Humor\"],\"averageRating\":3.5,\"ratingsCount\":374,\"maturityRating\":\"NOT_MATURE\",\"allowAnonLogging\":false,\"contentVersion\":\"preview-1.0.0\",\"imageLinks\":{\"smallThumbnail\":\"http://books.google.no/books/content?id=e554PwAACAAJ&printsec=frontcover&img=1&zoom=5&source=gbs_api\",\"thumbnail\":\"http://books.google.no/books/content?id=e554PwAACAAJ&printsec=frontcover&img=1&zoom=1&source=gbs_api\"},\"language\":\"en\",\"previewLink\":\"http://books.google.no/books?id=e554PwAACAAJ&dq=isbn:9781847399304&hl=&cd=1&source=gbs_api\",\"infoLink\":\"http://books.google.no/books?id=e554PwAACAAJ&dq=isbn:9781847399304&hl=&source=gbs_api\",\"canonicalVolumeLink\":\"http://books.google.no/books/about/The_Bro_Code.html?hl=&id=e554PwAACAAJ\"},\"saleInfo\":{\"country\":\"NO\",\"saleability\":\"NOT_FOR_SALE\",\"isEbook\":false},\"accessInfo\":{\"country\":\"NO\",\"viewability\":\"NO_PAGES\",\"embeddable\":false,\"publicDomain\":false,\"textToSpeechPermission\":\"ALLOWED\",\"epub\":{\"isAvailable\":false},\"pdf\":{\"isAvailable\":false},\"webReaderLink\":\"http://books.google.no/books/reader?id=e554PwAACAAJ&hl=&printsec=frontcover&output=reader&source=gbs_api\",\"accessViewStatus\":\"NONE\",\"quoteSharingAllowed\":false},\"searchInfo\":{\"textSnippet\":\"THE BRO CODE provides men with all the rules they need to know in order to become a &quot;bro&quot; and behave properly among other bros. THE BRO CODE has never been published before.\"}}]}";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Setup Mixpanel
        if (mixpanel == null){
            mixpanel = MixpanelAPI.getInstance(this, MIXPANEL_TOKEN);
        }

        //Must be set to allow HTTPRequests
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //Checks if this activity was created with an intent containing a message/extra with name ISBN.
        Intent intent = getIntent();
        String ISBN = intent.getStringExtra("ISBN");

        if (ISBN != null){

            GoogleBooksJSON googleBooksJson = convertGoogleBooksJsonStringToObject(getJsonAsStringFromISBN(ISBN));

            sendMail("Book scanned",
                    "ISBN: " + ISBN + "\n",
                    "no-reply@crowdshelf.com",
                    "crowdshelfmail@gmail.com");
            setContentView(R.layout.activity_scanner);

            assert googleBooksJson != null;
            String bookTitle = googleBooksJson.getItems().get(0).getVolumeInfo().getTitle();
            String bookThumbnail = googleBooksJson.getItems().get(0).getVolumeInfo().getImageLinks().getThumbnail();
            String bookInfo = googleBooksJson.getItems().get(0).getVolumeInfo().getDescription();

            ImageView imageView = (ImageView)findViewById(R.id.imageView);
            Picasso.with(this)
                    .load(bookThumbnail)
                    .into(imageView);

            TextView titleText = (TextView)findViewById(R.id.titleView);
            titleText.setText(bookTitle);

            TextView infoText = (TextView)findViewById(R.id.infoView);
            infoText.setText(bookInfo);

        }
    }

    private void getInfoFromISBN(String bookInformationJsonAsString) {
            Gson gson = new GsonBuilder().create();
            try{
                BookInfoGetter p = gson.fromJson(bookInformationJsonAsString, BookInfoGetter.class);
                Log.i("getInfoFromISBN", p.toString());

            }catch (Exception e) {
                Log.e("gson.FromJSON", e.getMessage());
            }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public void openScannerButtonPressed(View view) {
        Intent intent = new Intent(this, ScannerActivity.class);
        startActivity(intent);
    }

    public void sendMailButtonPressed(View view){
        sendMail("This is a subject", "This is the main text of the email", "kongen@slottet.no", "crowdshelfmail@hotmail.com");
        mixpanel.track("SendMailButtonPressed");
    }

    //Example of how mail can be sent
    public void sendMail(String subject, String body, String senderEmail, String recipients) {
        try{
            if (gMailSender == null)
                setUpGMailSender("crowdshelfmail@gmail.com", "kundestyrt");

            gMailSender.sendMail(subject, body, senderEmail, recipients);
            mixpanel.track("MailSentSuccessfully");
        }
        catch (Exception e){
            Log.e("SendMail", e.getMessage(), e);
        }
    }

    private String getJsonAsStringFromISBN(String isbn) {
        URL url;
        HttpURLConnection conn;
        BufferedReader rd;
        String line;
        StringBuilder result = new StringBuilder();
        try {
            url = new URL("https://www.googleapis.com/books/v1/volumes?q=isbn:" + isbn);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            rd.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result.toString();
    }



    private void setUpGMailSender(String user, String password) {
        gMailSender = new GMailSender(user, password);
    }

    private GoogleBooksJSON convertGoogleBooksJsonStringToObject(String bookInformationJsonAsString) {
        Gson gson = new GsonBuilder().create();
        try{
            GoogleBooksJSON p = gson.fromJson(bookInformationJsonAsString, GoogleBooksJSON.class);
            return p;
        }catch (Exception e){
            Log.e("gson.FromJSON", e.getMessage());
        }
        return null;
    }

}
