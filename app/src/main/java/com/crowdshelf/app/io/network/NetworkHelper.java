package com.crowdshelf.app.io.network;

import android.os.AsyncTask;
import android.util.Log;

import com.crowdshelf.app.MainController;
import com.crowdshelf.app.io.DbEventFailure;
import com.crowdshelf.app.io.DbEventOk;
import com.crowdshelf.app.io.DbEventType;
import com.crowdshelf.app.io.network.responseHandlers.ResponseHandler;
import com.crowdshelf.app.ui.activities.MainTabbedActivity;
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
        new AsyncTask<Void, Void, Response>() {
            @Override
            protected Response doInBackground(Void... params) {
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
                        return new Response(jsonString, connection.getResponseCode(), connection.getResponseMessage());
                    } else if (connection.getResponseCode() == 401) {
                        if (!route.contains("/login")) {
                            // Token timed out. Login again.
                            MainController.loginWithSavedCredentials();
                            // Do the request again. @todo: this may run before the above login has finished!
                            sendRequest(requestMethod, route, jsonData, responseHandler, dbEventType);
                        } else {
                            return new Response("",connection.getResponseCode(), connection.getResponseMessage());
                        }

                    } else {
                        Log.i(TAG, "ResponseCode: " + String.valueOf(connection.getResponseCode()) +
                                " ResponseMessage: " + connection.getResponseMessage());
                        return new Response("", connection.getResponseCode(), connection.getResponseMessage());
                    }
                } catch (java.net.MalformedURLException e) {
                    Log.w(TAG, "SendRequest MalformedURLException" + e.toString());
                } catch (IOException e) {
                    Log.w(TAG, "SendRequest IOException" + e.toString());
                }
                return new Response("", 0, "");
            }

            protected void onPostExecute(Response response) {
                handleResponse(response, responseHandler, dbEventType);
            }
        }.execute();
    }

    public static void handleResponse(Response response, ResponseHandler responseHandler, DbEventType dbEventType) {
        try {
            Log.i(TAG, "data " + response.getJsonData() + " msg " + response.getResponseMessage() + " code " + response.getResponseCode());
            String jsonData = response.getJsonData();
            if (jsonData.length() > 0) {
                Log.i(TAG, "Received JSON: " + jsonData);
                if (responseHandler != null) {
                    responseHandler.handleJsonResponse(jsonData, dbEventType);
                }
            } else if (response.getResponseCode() != 200){
                MainTabbedActivity.getBus().post(new DbEventFailure(dbEventType, response.getResponseCode()));
            }
        } catch (NullPointerException e) {
            Log.i(TAG, "HandleResponse NullPointerException" + e.toString());
        }
    }
}