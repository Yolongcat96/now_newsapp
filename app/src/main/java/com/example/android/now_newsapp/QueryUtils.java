package com.example.android.now_newsapp;

import android.text.TextUtils;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

// Helper methods related to requesting and receiving news dta from theguardian site
public class QueryUtils {

    public static final String LOG_TAG = QueryUtils.class.getSimpleName();
    public static final int READ_TIMEOUT = 10000;
    public static final int CONNECT_TIMEOUT = 15000;

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    // Create URL
    private static URL createUrl (String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem on building the URL:", e);
        }
        return url;
    }

    // create Http request signal
    private static String makeHttpRequest(URL url) throws IOException {

        String jsonResponse = ""; // Initialization

        // If the url is null, then return early
        if (url == null) {
            return jsonResponse;
        }

        // Create the request signal
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(READ_TIMEOUT/*miliseconds*/);
            urlConnection.setConnectTimeout(CONNECT_TIMEOUT);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request wasd successful, get the code (response code is 200)
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) { // Success
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }

        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }

        return jsonResponse;

    }

    private static String readFromStream(InputStream inputStream) throws IOException {

        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    public static List<Article> fetchArticleData (String requestUrl) {

        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link Articles}s
        List<Article> articles = extractFeatureFromJson(jsonResponse);

        // Return the list of {@link article}s
        return articles;
    }

    public static List<Article> extractFeatureFromJson(String articleJSON) {

        if (TextUtils.isEmpty(articleJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding earthquakes to
        List<Article> articles = new ArrayList<>();

        try {

            JSONObject root = new JSONObject(articleJSON);
            JSONObject contents = root.getJSONObject("response");
            JSONArray articleArray = contents.getJSONArray("results");
            for (int i = 0 ; i < articleArray.length() ; i++) {

                JSONObject currentArticle = articleArray.getJSONObject(i);

                // Article Id
                String articleId = currentArticle.getString("id");

                // Article Section Id
                String sectionId = currentArticle.getString("sectionId");

                // Article Section
                String sectionName = currentArticle.getString("sectionName");

                // Article Title
                String title = currentArticle.getString("webTitle");

                // Article web url
                String webUrl = currentArticle.getString("webUrl");

                // Article publication date
                String _publicationDate = currentArticle.getString("webPublicationDate");
                String publicationDate = reformatDate(_publicationDate);

                // Get data from the fields
                JSONObject fields = currentArticle.getJSONObject("fields");
                String imageUrl = null;
                if (fields != null) {
                    imageUrl = fields.getString("thumbnail");
                }

                // Get data from the tags
                JSONArray tags = currentArticle.getJSONArray("tags");
                String author_firstName = "Unknown";
                String author_lastName = "";
                if (tags.length() > 0) {
                    JSONObject author = tags.getJSONObject(0);
                    author_firstName = author.getString("firstName");
                    author_lastName = author.getString("lastName");
                }

                String name = "Written by "+ author_firstName + " " + author_lastName;

                Article _article = new Article(articleId, sectionId, sectionName, title, webUrl, imageUrl, publicationDate, name);
                articles.add(_article);

            }

        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the article results", e);
        }

        return articles;
    }

    // Format the received data format
    private static String reformatDate(String stringDate) {

        Date date1 = new Date();
        try {
            date1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(stringDate);
        } catch (ParseException e) {
            Log.e(LOG_TAG,"Formatted date error" + e);
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd LLL yyyy");

        return dateFormat.format(date1).toString();
    }

}
