package ntnu.stud.markul.crowdshelf;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.mixpanel.android.mpmetrics.MixpanelAPI;

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

        //Checks if this activity was created with an intent containing a message/extra with name ISBN.
        Intent intent = getIntent();
        String ISBN = intent.getStringExtra("ISBN");
        if (ISBN != null){
            Log.i("MainActivity", "ISBN not null");
            sendMail("Book scanned", "ISBN: " + ISBN, "no-reply@crowdshelf.com", "crowdshelfmail@gmail.com");
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

}
