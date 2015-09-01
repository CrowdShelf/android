package ntnu.stud.markul.crowdshelf;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.HttpURLConnection.*;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Torstein on 01.09.2015.
 */
public class NetworkHelper {
    private static String host = "https://something.herokuapp.com";
    public static void sendPostRequest(final String route   ) {
        new AsyncTask<Void,Void,Void>(){
            @Override
            protected Void doInBackground(Void... params){
                try {
                    // instantiate the URL object with the target URL of the resource to
                    // request
                    URL url = new URL(host + "/api" + route);
                    // instantiate the HttpURLConnection with the URL object - A new
                    // connection is opened every time by calling the openConnection
                    // method of the protocol handler for this URL.
                    // This is the point where the connection is opened.
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    // set connection output to true
                    connection.setDoOutput(true);
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-Type", "application/json");

                    OutputStreamWriter writer = new OutputStreamWriter(
                            connection.getOutputStream());

                    // if there is a response code AND that response code is 200 OK, do
                    // stuff in the first if block
                    if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        // TODO: do GSON stuff here
                    } else {
                        // Server returned HTTP error code.
                    }
                } catch (java.net.MalformedURLException e) {
                    // ...
                } catch (IOException e) {
                    //..
                }
                return null;
            }
        }.execute();
    }

    public static void sendPutRequest(final String route, final String jsonData) {
        new AsyncTask<Void,Void,Void>(){
            @Override
            protected Void doInBackground(Void... params){
                try {
                    // instantiate the URL object with the target URL of the resource to
                    // request
                    URL url = new URL(host + "/api" + route);
                    // instantiate the HttpURLConnection with the URL object - A new
                    // connection is opened every time by calling the openConnection
                    // method of the protocol handler for this URL.
                    // This is the point where the connection is opened.
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    // set connection output to true
                    connection.setDoOutput(true);
                    connection.setRequestMethod("PUT");
                    connection.setRequestProperty("Content-Type", "application/json");

                    OutputStreamWriter writer = new OutputStreamWriter(
                            connection.getOutputStream());

                    // write data to the connection. This is data that you are sending to the server
                    writer.write(jsonData);

                    // Closes this output stream and releases any system resources
                    // associated with this stream.
                    writer.close();
                    // if there is a response code AND that response code is 200 OK, do
                    // stuff in the first if block
                    if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        // TODO: do GSON stuff here
                    } else {
                        // Server returned HTTP error code.
                    }
                } catch (java.net.MalformedURLException e) {
                    // ...
                } catch (IOException e) {
                    //..
                }
                return null;
            }
        }.execute();
    }

    public static void sendGetRequest(final String route) {
        new AsyncTask<Void,Void,Void>(){
            @Override
            protected Void doInBackground(Void... params){
                InputStream inputStream = null;
                try {
                    // instantiate the URL object with the target URL of the resource to
                    // request
                    URL url = new URL(host + "/api" + route);

                    // instantiate the HttpURLConnection with the URL object - A new
                    // connection is opened every time by calling the openConnection
                    // method of the protocol handler for this URL.
                    // This is the point where the connection is opened.
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    // set connection output to true
                    connection.setDoOutput(true);
                    connection.setRequestMethod("GET");
                    connection.setRequestProperty("Content-Type", "application/json");

                    connection.connect();

                    inputStream = connection.getInputStream();
                    // if there is a response code AND that response code is 200 OK, do
                    // stuff in the first if block
                    if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        // TODO: do GSON stuff here
                    } else {
                        // Server returned HTTP error code.
                    }
                } catch (java.net.MalformedURLException e) {
                    // ...
                } catch (IOException e) {
                    //..
                } finally {
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            return null;
            }
        }.execute();
    }

    private static void printRequest() {
    }

}
