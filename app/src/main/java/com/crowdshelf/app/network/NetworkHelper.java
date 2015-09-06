package com.crowdshelf.app.network;

import android.os.AsyncTask;
import android.util.Log;

import com.crowdshelf.app.MainController;
import com.crowdshelf.app.gsonHelpers.UserDeserializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import com.crowdshelf.app.models.Book;
import com.crowdshelf.app.models.Crowd;
import com.crowdshelf.app.models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

/**
 * Created by Torstein on 01.09.2015.
 */
public class NetworkHelper {
    private static String host = "https://crowdshelf.herokuapp.com";
    // For converting json into java objects using GSON and custom deserializers for each class
    private static Gson gson = new GsonBuilder()
            //.registerTypeAdapter(Book.class, new BookDeserializer())
            .registerTypeAdapter(User.class, new UserDeserializer())
            //.registerTypeAdapter(Crowd.class, new CrowdDeserializer())
            // .serializeNulls() // json nulls for null fields
            .setPrettyPrinting()
            .create();

    public static void sendPostRequest(final String route, final String jsonData) {
        new AsyncTask<Void,Void,InputStreamReader>(){
            @Override
            protected InputStreamReader doInBackground(Void... params){
                try {
                    // instantiate the URL object with the target URL of the resource to
                    // request
                    URL url = new URL(host + "/api" + route);
                    // instantiate the HttpURLConnection with the URL object - A new
                    // connection is opened every time by calling the openConnection
                    // method of the protocol handler for this URL.
                    // This is the point where the connection is opened.
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoOutput(true);
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-Type", "application/json");
                    OutputStreamWriter writer = new OutputStreamWriter(
                            connection.getOutputStream());
                    writer.write(jsonData);
                    writer.close();
                    if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        return new InputStreamReader(connection.getInputStream());
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
            protected void onPostExecute(InputStreamReader reader) {
                handleJsonResponse(reader);
            }
        }.execute();
    }

    public static void sendPutRequest(final String route, final String jsonData) {
        new AsyncTask<Void,Void,InputStreamReader>(){
            @Override
            protected InputStreamReader doInBackground(Void... params){
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
                        new InputStreamReader(connection.getInputStream());
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
            protected void onPostExecute(InputStreamReader reader) {
                handleJsonResponse(reader);
            }
        }.execute();
    }

    public static void sendGetRequest(final String route) {
        new AsyncTask<Void,Void,InputStreamReader>(){
            @Override
            protected InputStreamReader doInBackground(Void... params){
                try {
                   URL url = new URL(host + "/api" + route);
                    System.out.print(url + "\n");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoOutput(true);
                    connection.setRequestMethod("GET");
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.connect();
                    if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        return new InputStreamReader(connection.getInputStream());
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
            protected void onPostExecute(InputStreamReader reader) {
                handleJsonResponse(reader);
            }
        }.execute();
    }

    public static void handleJsonResponse(InputStreamReader iReader) {
        try {
            /*
            Possible reponses:
            user object
            book object
            crowd object
            array of book objects
            array of crowd objects
             */

            BufferedReader bReader = new BufferedReader(iReader);
            StringBuilder builder = new StringBuilder();
            for (String line = null; (line = bReader.readLine()) != null;) {
                builder.append(line).append("\n");
            }
            String jsonString = builder.toString();
            JsonParser jsonParser = new JsonParser();
            JsonElement jsonElement = jsonParser.parse(jsonString);

            System.out.print("Received JSON-data: \n");
            System.out.print(gson.toJson(jsonElement));

            // TODO this also needs to handle arrays of obects!
            
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            if (jsonObject.has("isbn")) {
                // Retrieved book
                Book book = gson.fromJson(jsonString, Book.class);
                MainController.retrieveBook(book);
            } else if (jsonObject.has("username")) {
                // Retrieved User
                User user = gson.fromJson(jsonString, User.class);
                MainController.retrieveUser(user);
            } else if (jsonObject.has("owner")) {
                // Retrieved crowd
                Crowd crowd = gson.fromJson(jsonString, Crowd.class);
                MainController.retrieveCrowd(crowd);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
