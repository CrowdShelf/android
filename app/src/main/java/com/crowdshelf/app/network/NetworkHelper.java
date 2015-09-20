package com.crowdshelf.app.network;

import android.os.AsyncTask;
import android.util.Log;

import com.crowdshelf.app.network.responseHandlers.ResponseHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Torstein on 01.09.2015.
 */
public class NetworkHelper {
    private static String host = "http://crowdshelf-dev.herokuapp.com";
    // For converting json into java objects using GSON and custom deserializers for each class
    private static Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    public static void sendRequest(final HTTPRequestMethod requestMethod, final String route, final String jsonData, final ResponseHandler responseHandler) {
        new AsyncTask<Void, Void, InputStreamReader>() {
            @Override
            protected InputStreamReader doInBackground(Void... params) {
                try {
                    URL url = new URL(host + "/api" + route);
                    Log.d("NETDBTEST", "NetworkHelper request: " + requestMethod.toString() + " URL: " + url.toString());
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoOutput(false); // must be false for GET. WHY?
                    connection.setDoInput(true);
                    if (jsonData != null) {
                    }
                    if (responseHandler != null) {
                    }
                    connection.setRequestMethod(requestMethod.toString());
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.setConnectTimeout(12000);
                    connection.setReadTimeout(12000);
                    connection.connect();
                    Log.d("NETDBTEST", "NetworkHelper DoOutput: " + connection.getDoOutput());
                    Log.d("NETDBTEST", "NetworkHelper DoInput: " + connection.getDoInput());
                    if (jsonData != null) {
                        Log.d("NETDBTEST", "NetworkHelper Sending JsonData");
                        OutputStreamWriter writer = new OutputStreamWriter(
                                connection.getOutputStream());
                        writer.write(jsonData);
                        writer.close();
                    }
                    Log.d("NETDBTEST", "NetworkHelper ResponseCode: " + String.valueOf(connection.getResponseCode()));
                    Log.d("NETDBTEST", "NetworkHelper ResponseMessage: " + connection.getResponseMessage());

                    if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        return new InputStreamReader(connection.getInputStream());
                    } else {

                    }
                } catch (java.net.MalformedURLException e) {
                    Log.d("NETDBTEST", "NetworkHelper SendRequest MalformedURLException" + e.toString());
                } catch (IOException e) {
                    Log.d("NETDBTEST", "NetworkHelper SendRequest IOException" + e.toString());
                }
                return null;
            }

            protected void onPostExecute(InputStreamReader reader) {
                if (reader != null) {
                    handleResponse(reader, responseHandler);
                }
            }
        }.execute();
    }

    public static void handleResponse(InputStreamReader iReader, ResponseHandler responseHandler) {
        try {
            BufferedReader bReader = new BufferedReader(iReader);
            StringBuilder builder = new StringBuilder();
            String line = null;
            while ((line = bReader.readLine()) != null)
            {
                builder.append(line).append("\n");
            }
            String jsonString = builder.toString();
            JsonElement jsonElement = new JsonParser().parse(jsonString);

            //System.out.print("Received JSON-data in NetworkHelper: \n");
            Log.d("NETDBTEST", "Received JSON: " + gson.toJson(jsonElement));
            if (responseHandler != null) {
                responseHandler.handleJsonResponse(jsonString);
            }
            iReader.close();
            bReader.close();
        } catch (IOException e) {
            Log.d("NETDBTEST", "NetworkHelper HandleResponse IOException" + e.toString());;
        } catch (NullPointerException e) {
            Log.d("NETDBTEST", "NetworkHelper HandleResponse NullPointerException" + e.toString());
        }
    }
}