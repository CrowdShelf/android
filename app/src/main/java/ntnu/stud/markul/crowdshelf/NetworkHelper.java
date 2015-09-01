package ntnu.stud.markul.crowdshelf;
import java.io.BufferedReader;
import java.io.IOException;
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
    public static void sendPostRequest(String jsonData) {
        try {
            // instantiate the URL object with the target URL of the resource to
            // request
            URL url = new URL("https://something.herokuapp.com");

            // instantiate the HttpURLConnection with the URL object - A new
            // connection is opened every time by calling the openConnection
            // method of the protocol handler for this URL.
            // 1. This is the point where the connection is opened.
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            // set connection output to true
            connection.setDoOutput(true);
            // instead of a GET, we're going to send using method="POST"
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");

            // instantiate OutputStreamWriter using the output stream, returned
            // from getOutputStream, that writes to this connection.
            // 2. This is the point where you'll know if the connection was
            // successfully established. If an I/O error occurs while creating
            // the output stream, you'll see an IOException.
            OutputStreamWriter writer = new OutputStreamWriter(
                    connection.getOutputStream());

            // write data to the connection. This is data that you are sending to the server
            writer.write("message=" + jsonData);

            // Closes this output stream and releases any system resources
            // associated with this stream. At this point, we've sent all the
            // data. Only the outputStream is closed at this point, not the
            // actual connection
            writer.close();
            // if there is a response code AND that response code is 200 OK, do
            // stuff in the first if block
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {


            } else {
                // Server returned HTTP error code.
            }
        } catch (java.net.MalformedURLException e) {
            // ...
        } catch (IOException e) {
            // ...
        }
    }

    public static void sendGetRequest(String jsonData) {
        try {
            URL url = new URL("https://something.herokuapp.com");

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setDoOutput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");

            InputStreamReader reader = new InputStreamReader(
                    connection.getInputStream());


            // Closes this output stream and releases any system resources
            // associated with this stream. At this point, we've sent all the
            // data. Only the outputStream is closed at this point, not the
            // actual connection

            // if there is a response code AND that response code is 200 OK, do
            // stuff in the first if block
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
            } else {
                // Server returned HTTP error code.
            }
            reader.close();
        } catch (java.net.MalformedURLException e) {
            // ...
        } catch (IOException e) {
            // ...
        }
    }

    private static void printRequest() {
    }

}
