package com.example.androidu.newsapp;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonUtils {
    public static boolean parse(ArrayList<NewsItem> newsItemList, String jsonString){

        try {
            JSONObject topLevelObject = new JSONObject(jsonString);
            JSONArray articleArray = topLevelObject.getJSONArray("articles");
            System.out.println("JsonArray size: " + articleArray.length());
            for(int i = 0; i < articleArray.length(); i++){
                System.out.println("looping");
                JSONObject articleObject = articleArray.getJSONObject(i);
                String title = articleObject.getString("title");
                String description = articleObject.getString("description");
                String url = articleObject.getString("url");
                newsItemList.add(new NewsItem(title, description, url));
            }

            return true;
        }
        catch(org.json.JSONException e){
            e.printStackTrace();
        }

        return false;

    }
}
