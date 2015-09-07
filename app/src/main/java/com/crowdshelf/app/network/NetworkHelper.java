package com.crowdshelf.app.network;

import android.os.AsyncTask;

import com.crowdshelf.app.network.gsonHelpers.JsonHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Torstein on 01.09.2015.
 */
public class NetworkHelper {
    private static String host = "https://crowdshelf.herokuapp.com";
    // For converting json into java objects using GSON and custom deserializers for each class
    private static Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    public static void sendPostRequest(final String route, final String jsonData, final JsonHandler jsonHandler) {
        new AsyncTask<Void, Void, InputStreamReader>() {
            @Override
            protected InputStreamReader doInBackground(Void... params) {
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
                handleResponse(reader, jsonHandler);
            }
        }.execute();
    }

    public static void sendPutRequest(final String route, final String jsonData, final JsonHandler jsonHandler) {
        new AsyncTask<Void, Void, InputStreamReader>() {
            @Override
            protected InputStreamReader doInBackground(Void... params) {
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
                handleResponse(reader, jsonHandler);
            }
        }.execute();
    }

    public static void sendGetRequest(final String route, final JsonHandler jsonHandler) {
        new AsyncTask<Void, Void, InputStreamReader>() {
            @Override
            protected InputStreamReader doInBackground(Void... params) {
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
                handleResponse(reader, jsonHandler);
            }
        }.execute();
    }

    public static void handleResponse(InputStreamReader iReader, JsonHandler jsonHandler) {
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
            for (String line = null; (line = bReader.readLine()) != null; ) {
                builder.append(line).append("\n");
            }
            String jsonString = builder.toString();
            JsonElement jsonElement = new JsonParser().parse(jsonString);

            System.out.print("Received JSON-data: \n");
            System.out.print(gson.toJson(jsonElement));
            if (jsonHandler != null) {
                jsonHandler.handleJsonResponse(jsonString);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}