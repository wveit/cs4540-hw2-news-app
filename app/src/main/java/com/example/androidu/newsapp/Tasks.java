/*

    This class provides 3 static methods that do miscelaneous tasks that are required by other
        classes:

    syncDatabase(...) performs the entire process of updating the databse from newsapi.org. It is
        meant to be used through the DatabaseUpdateService class which will call this method in a
        new thread or set up a recurring job that will call this in a new thread.

    appIsInitialized(...) checks to see if this app has been run before in order to determine if
        the database should be populated with newsItems (or if there should already be newsItems
        present)

    markAppInitialized(...) allows the app to be marked as Initialized, so that subsequent runs
        of this app will not cause the database to be updated upon startup (instead the database
        will be updated as part of a scheduled job every one minute).


 */

package com.example.androidu.newsapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.androidu.newsapp.database.MyDatabase;
import com.example.androidu.newsapp.network.ApiKeyHolder;
import com.example.androidu.newsapp.network.NetworkUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class Tasks {

    private static final String TAG = "Tasks";
    private static final String SHARED_PREF_FILE_ID = "com.example.androidu.newsapp";
    private static final String INITIALIZED_KEY = "initialized";

    public static boolean syncDatabase(MyDatabase db){

        String sourceString = "the-next-web";
        String sortByString = "latest";
        String apiKeyString = ApiKeyHolder.API_KEY;

        URL url = NetworkUtils.buildUrl(sourceString, sortByString, apiKeyString);
        Log.d(TAG, "Built url: " + url);

        try{
            String json = NetworkUtils.getResponseFromHttpUrl(url);
            Log.d(TAG, "Received http response (JSON string)");

            ArrayList<NewsItem> newsItemList = new ArrayList<NewsItem>();
            NetworkUtils.parseJson(newsItemList, json);
            Log.d(TAG, "Parsed JSON into " + newsItemList.size() + " items");
            Log.d(TAG, "Date: " + newsItemList.get(0).getDate());

            db.clearItems();

            db.addItems(newsItemList);
            Log.d(TAG, "Added items to database");
        }
        catch(IOException e){
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public static boolean appIsInitialized(Context context){
        SharedPreferences prefs = context.getSharedPreferences("SHARED_PREF_FILE_ID", Context.MODE_PRIVATE);
        return prefs.contains(INITIALIZED_KEY);
    }

    public static void markAppInitialized(Context context){
        SharedPreferences prefs = context.getSharedPreferences("SHARED_PREF_FILE_ID", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(INITIALIZED_KEY, true);
        editor.commit();
    }

}
