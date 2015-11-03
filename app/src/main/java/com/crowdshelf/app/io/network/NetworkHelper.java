package com.crowdshelf.app.io.network;

import android.os.AsyncTask;
import android.util.Log;

import com.crowdshelf.app.MainController;
import com.crowdshelf.app.io.DbEventType;
import com.crowdshelf.app.io.network.responseHandlers.ResponseHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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
    private final static String TAG = "NetworkHelper";
    private static String host = "http://crowdshelf-dev.herokuapp.com";
    // For converting json into java objects using GSON and custom deserializers for each class
    private static Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    public static void sendRequest(final HttpRequestMethod requestMethod, final String route, final String jsonData,
                                   final ResponseHandler responseHandler, final DbEventType dbEventType)
    {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                try {
                    URL url = new URL(host + "/api" + route);
                    Log.i(TAG, "Send request: " + requestMethod.toString() + " URL: " + url.toString());
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoOutput(false); // must be false for GET. WHY?
                    connection.setDoInput(true);
                    // todo: Maybe it will become necessary to use other values for setDoOutput and setDoInput
                    if (jsonData != null) {
                    }
                    if (responseHandler != null) {
                    }
                    connection.setRequestMethod(requestMethod.toString());
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.setConnectTimeout(12000);
                    connection.setReadTimeout(12000);
                    connection.connect();
                    //Log.i(TAG, "NetworkHelper DoOutput: " + connection.getDoOutput() + "DoInput: " + connection.getDoInput());
                    if (jsonData != null) {
                        Log.i(TAG, "Sending JsonData: " + jsonData);
                        OutputStreamWriter writer = new OutputStreamWriter(
                                connection.getOutputStream());
                        writer.write(jsonData);
                        writer.close();
                    }

                    if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        InputStreamReader iReader = new InputStreamReader(connection.getInputStream());
                        BufferedReader bReader = new BufferedReader(iReader);
                        StringBuilder builder = new StringBuilder();
                        String line = null;
                        while ((line = bReader.readLine()) != null) {
                            builder.append(line).append("\n");
                        }
                        String jsonString = builder.toString();
                        iReader.close();
                        bReader.close();
                        return jsonString;
                    } else if (connection.getResponseCode() == 401) {
                        // Token timed out. Login again.
                        MainController.loginWithSavedCredentials();
                        // Do the request again. @todo: this may run before the above signInButtonClicked has finished!
                        sendRequest(requestMethod, route, jsonData, responseHandler, dbEventType);
                    } else {
                        Log.i(TAG, "ResponseCode: " + String.valueOf(connection.getResponseCode()) +
                                " ResponseMessage: " + connection.getResponseMessage());
                    }
                } catch (java.net.MalformedURLException e) {
                    Log.w(TAG, "SendRequest MalformedURLException" + e.toString());
                } catch (IOException e) {
                    Log.w(TAG, "SendRequest IOException" + e.toString());
                }
                return "";
            }

            protected void onPostExecute(String jsonString) {
                handleResponse(jsonString, responseHandler, dbEventType);
            }
        }.execute();
    }

    public static void handleResponse(String jsonString, ResponseHandler responseHandler, DbEventType dbEventType) {
        try {
            if (jsonString.length() > 0) {
                Log.i(TAG, "Received JSON: " + jsonString);
                if (responseHandler != null) {
                    responseHandler.handleJsonResponse(jsonString, dbEventType);
                }
            } else {
                Log.d(TAG, "Did not receive data from server");
            }
        } catch (NullPointerException e) {
            Log.i(TAG, "HandleResponse NullPointerException" + e.toString());
        }
    }
}