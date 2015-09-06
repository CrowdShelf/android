package com.crowdshelf.app;

import android.util.Log;

import com.crowdshelf.app.jsonModels.GoogleBooksMain;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HelperMethods {

    public static GoogleBooksMain convertGoogleBooksJsonStringToObject(String bookInformationJsonAsString) {
        Gson gson = new GsonBuilder().create();
        try {
            return gson.fromJson(bookInformationJsonAsString, GoogleBooksMain.class);
        } catch (Exception e) {
            Log.e("gson.FromJSON", e.getMessage());
        }
        return null;
    }

    public static String getJsonFromGoogleBooksApiUsingISBN(String isbn) {
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

    public static boolean isJSONValid(String test) {
        try {
            new JSONObject(test);
        } catch (JSONException ex) {
            // edited, to include @Arthur's comment
            // e.g. in case JSONArray is valid as well...
            try {
                new JSONArray(test);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }
}
