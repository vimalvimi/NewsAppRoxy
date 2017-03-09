package com.vimalroxy.news_roxy;

import android.net.Uri;
import android.text.format.DateUtils;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static android.text.format.DateUtils.MINUTE_IN_MILLIS;

public class Utils {

    private static final String TAG = "Utils";
    private static final String API_KEY = "390b643c-7a61-4fa3-92f6-0b2aa1e977d1";

    public Utils() {
    }

    static String createStringUrl() {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http")
                .authority("content.guardianapis.com")
                .appendPath("search")
                .appendQueryParameter("api-key", API_KEY)
                .appendQueryParameter("format", "json")
                .appendQueryParameter("order-by", "newest")
                .appendQueryParameter("show-tags", "contributor")
                .appendQueryParameter("show-references", "author");
        String url = builder.toString();
        return url;
    }

    static URL createUrl() {
        String Stringurl = createStringUrl();
        try {
            return new URL(Stringurl);
        } catch (MalformedURLException exception) {
            Log.e(TAG, "Error with creating URL", exception);
            return null;
        }
    }

    static String makeHttpRequest(URL url) throws IOException {

        int readTimeout = 10000;
        int connectionTimeout = 15000;

        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(readTimeout /* milliseconds */);
            urlConnection.setConnectTimeout(connectionTimeout /* milliseconds */);
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e("mainActivity", "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e("Queryutils", "Error making HTTP request: ", e);
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

    static long getDateInMillis(String srcDate) {
        String jsonDatePattern = "yyyy-MM-dd'T'HH:mm:ss'Z'";
        SimpleDateFormat desiredFormat = new SimpleDateFormat(jsonDatePattern, Locale.US);
        long dateInMillis = 0;
        try {
            Date date = desiredFormat.parse(srcDate);
            dateInMillis = date.getTime();
            return dateInMillis;
        } catch (ParseException e) {
            Log.d(TAG, "getDateInMillis: ERROR " + e);
            e.printStackTrace();
        }
        return 0;

    }

    public static ArrayList<News> extractNewsJSON(String newsJSONString) {

        //Creating New Array List To Fill Info From JSON STRING
        ArrayList<News> newsArrayList = new ArrayList<>();

        long JSONDate;
        long currentTimeinMS = System.currentTimeMillis();

        try {
            JSONObject root = new JSONObject(newsJSONString);
            JSONObject response = root.getJSONObject("response");
            JSONArray results = response.getJSONArray("results");

            for (int i = 0; i < results.length(); i++) {
                JSONObject news = results.getJSONObject(i);

                String sectionName = news.getString("sectionName");

                //DATE
                String date = news.getString("webPublicationDate");
                JSONDate = getDateInMillis(date);
                String dateFinal = String.valueOf(JSONDate);
                CharSequence ago = DateUtils.getRelativeTimeSpanString(JSONDate,currentTimeinMS,MINUTE_IN_MILLIS);
                final StringBuilder sb = new StringBuilder(ago.length());
                sb.append(ago);
                String agoTime = sb.toString();


                String title = news.getString("webTitle");
                String url = news.getString("webUrl");
                JSONArray tagsArray = news.getJSONArray("tags");
                String author = "";

                if (tagsArray.length() == 0) {
                    author = null;
                } else {
                    for (int j = 0; j < 1; j++) {
                        JSONObject firstObject = tagsArray.getJSONObject(j);
                        author += firstObject.getString("webTitle") + "";
                    }
                }
                newsArrayList.add(new News(title, sectionName, url, author , agoTime));
            }
        } catch (JSONException e) {
            Log.e(TAG, "extractNewsJSON: Problem parsing the NEWS JSON result", e);
        }
        return newsArrayList;
    }

}
