package ntnu.stud.markul.crowdshelf;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private final String MIXPANEL_TOKEN = "93ef1952b96d0faa696176aadc2fbed4";

    private GMailSender gMailSender;
    private MixpanelAPI mixpanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Setup Mixpanel
        if (mixpanel == null){
            mixpanel = MixpanelAPI.getInstance(this, MIXPANEL_TOKEN);
        }

        //Must be set to allow HTTPRequest
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //Checks if this activity was created with an intent containing a message/extra with name ISBN.
        Intent intent = getIntent();

        String ISBN = intent.getStringExtra("ISBN");

        if (ISBN != null){
            String bookInformationJsonAsString = getJsonAsStringFromISBN(ISBN);

            Map<String, Object> allBookInformationJson =
                    new Gson().fromJson(bookInformationJsonAsString, new TypeToken<HashMap<String, Object>>() {
                    }.getType());





            sendMail("Book scanned",
                    "ISBN: " + ISBN + "\n"
                            + bookInformationJson.get(""), + "\n"
                    "no-reply@crowdshelf.com",
                    "crowdshelfmail@gmail.com");
        }

        setContentView(R.layout.activity_main);
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
                setUpGmailSender("crowdshelfmail@gmail.com", "kundestyrt");

            gMailSender.sendMail(subject, body, senderEmail, recipients);
            mixpanel.track("MailSentSuccessfully");
        }
        catch (Exception e){
            Log.e("SendMail", e.getMessage(), e);
        }
    }

    private void setUpGmailSender(String user, String password) {
        gMailSender = new GMailSender(user, password);
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

}
