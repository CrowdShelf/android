package ntnu.stud.markul.crowdshelf;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    public final static String EXTRA_MESSAGE = "com.mycompany.myfirstapp.MESSAGE";
    private GMailSender gMailSender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    /** Called when the user clicks the Send button */
    public void sendMessage(View view) {
        Intent intent = new Intent(this, ProfileActivity.class);
        EditText editText = (EditText) findViewById(R.id.edit_message);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }
    public void openScanner(View view) {
        Intent intent = new Intent(this, ScannerActivity.class);
        startActivity(intent);
    }

    public void sendMailButtonPressed(View view){
        sendMail("This is a subject", "This is the main text of the email", "kongen@slottet.no", "markuslund92@hotmail.com");
    }

    //Example of how mail can be sent
    public void sendMail(String subject, String body, String senderEmail, String recipients) {
        try{

            if (gMailSender == null)
                setUpGmailSender("kundestyrt", "crowdshelfmail@gmail.com");

            gMailSender.sendMail(subject, body, senderEmail, recipients);
        }catch (Exception e){
            Log.e("SendMail", e.getMessage(), e);
        }
    }

    private void setUpGmailSender(String password, String user) {
        gMailSender = new GMailSender(user, password);
    }
}
