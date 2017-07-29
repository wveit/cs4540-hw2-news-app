/*

    This class provides 3 static methods that do work related to getting newsItems from newsapi.org

    buildUrl(...) takes in the three required query parameters for accessing the newsapi.org api and
        builds the Url that will be used to query the api.

    getResponseFromHttpUrl(...) takes in the Url used to access the api and returns the Json response
        from newsApi.org

    parseJson(...) takes in an empty ArrayList<NewsItem> and a Json string that contains NewsItems.
        It then parses the Json, extracts the NewsItems and adds the NewsItems to the arrayList.
        This arrayList of NewsItems can be used to add the newsitems to a database
 */

package com.example.androidu.newsapp.network;

import android.net.Uri;

import com.example.androidu.newsapp.NewsItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;


public class NetworkUtils {
    public static final String BASE_URL = "https://newsapi.org/v1/articles";
    public static final String SOURCE_PARAM = "source";
    public static final String SORT_BY_PARAM = "sortBy";
    public static final String API_KEY_PARAM = "apiKey";

    public static URL buildUrl(String source, String sortBy, String apiKey){
        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(SOURCE_PARAM, source)
                .appendQueryParameter(SORT_BY_PARAM, sortBy)
                .appendQueryParameter(API_KEY_PARAM, apiKey).build();

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

    public static boolean parseJson(ArrayList<NewsItem> newsItemList, String jsonString){

        try {
            JSONObject topLevelObject = new JSONObject(jsonString);
            JSONArray articleArray = topLevelObject.getJSONArray("articles");
            for(int i = 0; i < articleArray.length(); i++){
                JSONObject articleObject = articleArray.getJSONObject(i);
                String author = articleObject.getString("author");
                String title = articleObject.getString("title");
                String description = articleObject.getString("description");
                String url = articleObject.getString("url");
                String imageUrl = articleObject.getString("urlToImage");
                String date = articleObject.getString("publishedAt").substring(0, 10);

                newsItemList.add(new NewsItem(author, title, description, url, imageUrl, date));
            }

            return true;
        }
        catch(org.json.JSONException e){
            e.printStackTrace();
        }

        return false;

    }
}
