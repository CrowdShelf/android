package ntnu.stud.markul.crowdshelf;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import ntnu.stud.markul.crowdshelf.gsonHelpers.BookDeserializer;
import ntnu.stud.markul.crowdshelf.gsonHelpers.CrowdDeserializer;
import ntnu.stud.markul.crowdshelf.gsonHelpers.UserDeserializer;
import ntnu.stud.markul.crowdshelf.models.Book;
import ntnu.stud.markul.crowdshelf.models.Crowd;
import ntnu.stud.markul.crowdshelf.models.User;

/**
 * Created by Torstein on 01.09.2015.
 */
public class NetworkHelper {
    private static String host = "https://crowdshelf.herokuapp.com";
    // For converting json into java objects using GSON and custom deserializers for each class
    private static Gson gson = new GsonBuilder()
            .registerTypeAdapter(Book.class, new BookDeserializer())
            .registerTypeAdapter(User.class, new UserDeserializer())
            .registerTypeAdapter(Crowd.class, new CrowdDeserializer())
            .create();

    public static void sendPostRequest(final String route, final String jsonData) {
        new AsyncTask<Void,Void,JsonReader>(){
            @Override
            protected JsonReader doInBackground(Void... params){
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

                    writer.write(jsonData);
                    writer.close();
                    // if there is a response code AND that response code is 200 OK, do
                    // stuff in the first if block
                    if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        return new JsonReader(
                                new InputStreamReader(connection.getInputStream()));
                        // So far, no POST request gets a response
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
            @Override
            protected void onPostExecute(JsonReader reader) {
                handleJsonResponse(reader);
            }
        }.execute();
    }

    public static void sendPutRequest(final String route, final String jsonData) {
        new AsyncTask<Void,Void,Void>(){
            @Override
            protected Void doInBackground(Void... params){
                try {
                    URL url = new URL(host + "/api" + route);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoOutput(true);
                    connection.setRequestMethod("PUT");
                    connection.setRequestProperty("Content-Type", "application/json");

                    OutputStreamWriter writer = new OutputStreamWriter(
                            connection.getOutputStream());
                    writer.write(jsonData);
                    writer.close();
                    if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        // TODO: handle reponse codes?
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
        new AsyncTask<Void,Void,JsonReader>(){
            @Override
            protected JsonReader doInBackground(Void... params){
                InputStream inputStream = null;
                try {
                   URL url = new URL(host + "/api" + route);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoOutput(true);
                    connection.setRequestMethod("GET");
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.connect();
                    if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        return new JsonReader(
                                new InputStreamReader(connection.getInputStream()));
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

            @Override
            protected void onPostExecute(JsonReader reader) {
                handleJsonResponse(reader);
            }
        }.execute();
    }

    public static void handleJsonResponse(JsonReader reader) {
        try {
            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();
                // todo this needs checking and restructuring
                if (name.equals("isbn")) {
                    // Retrieved book
                    Book book = new Gson().fromJson(reader, Book.class);
                    MainController.retrieveBook(book);
                } else if (name.equals("memberOf")) {
                    // Retrieved User
                    User user = new Gson().fromJson(reader, User.class);
                    MainController.retrieveUser(user);
                } else if (name.equals("creator")) {
                    // Retrieved crowd
                    Crowd crowd = new Gson().fromJson(reader, Crowd.class);
                    MainController.retrieveCrowd(crowd);
                }  else {
                    reader.skipValue();
                }
            }
            reader.endObject();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
