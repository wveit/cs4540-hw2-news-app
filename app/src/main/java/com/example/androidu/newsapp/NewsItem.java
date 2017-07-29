/*

    This class is a simple model class to represent a NewsItem. It has getters and setters for
        each NewsItem's:

        * Author
        * Title
        * Description
        * Url
        * ImageUrl
        * Date
 */

package com.example.androidu.newsapp;

public class NewsItem {
    private String mAuthor;
    private String mTitle;
    private String mDescription;
    private String mUrl;
    private String mImageUrl;
    private String mDate;

    public NewsItem(){
        mAuthor = "";
        mTitle = "";
        mDescription = "";
        mUrl = "";
        mImageUrl = "";
        mDate = "";
    }

    public NewsItem(String author, String title, String description, String url, String imageUrl, String date){
        mAuthor = author;
        mTitle = title;
        mDescription = description;
        mUrl = url;
        mImageUrl = imageUrl;
        mDate = date;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public void setAuthor(String author) {
        mAuthor = author;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }
}
