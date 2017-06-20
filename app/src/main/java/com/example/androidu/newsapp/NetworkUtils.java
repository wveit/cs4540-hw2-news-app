package com.example.androidu.newsapp;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;


public class NetworkUtils {
    public static final String BASE_URL = "https://newsapi.org/v1/articles";
    public static final String SOURCE_PARAM = "source";
    public static final String SORT_BY_PARAM = "sortBy";
    public static final String API_KEY_PARAM = "apiKey";
    public static final String API_KEY = "99e8e4488d1248f49b14bbb22c759eed";

    public static URL buildUrl(String source, String sortBy){
        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(SOURCE_PARAM, source)
                .appendQueryParameter(SORT_BY_PARAM, sortBy)
                .appendQueryParameter(API_KEY_PARAM, API_KEY).build();

        URL url = null;

        try {
            url = new URL(uri.toString());
        }
        catch(MalformedURLException e){
            e.printStackTrace();
        }

        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try{
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if(hasInput) {
                return scanner.next();
            } else {
                return null;
            }

        } finally {
            urlConnection.disconnect();
        }
    }
}
