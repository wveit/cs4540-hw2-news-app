/*
    This class encapsulates all database and cursor functionality for the rest of the app.

    This class contains open() and close() methods which create or close a connection to the
    database. Only one connection will be opened at a time, even if open() is called several times.

    This class contains selectAll() method, which populates an internal cursor with the contents
    of the database.

    In order to access the internal cursor (and thus the contents of the database), the following
        methods are provided:

        * getNumCursorRows() -> returns the number of records that the internal cursor can point
            to . This information is necessary when iterating through all the records.

        * moveCursorToRow(int row) -> moves the cursor to a particular record, so that record's
            information can be extracted.

        * getAuthor(), getTitle(), getDescription(), getUrl(), getImageUrl(), getDate(), getId() ->
            these methods get the associated data from the record that is currently pointed to by
            the internal cursor.

    This class provides the following methods to update the contents of the databse:

        * addItem(NewsItem item) -> allows adding one news item to databse

        * addItems(ArrayList<NewsItem> itemlist) -> allows adding a bunch of news items to databse
            with one method call

        * deleteItem(int id) -> allows deleting one item from database. Id of item to be deleted
            must be known to use this method.

        * clearItems() -> clears all items records from the database but preserves table structure

 */

package com.example.androidu.newsapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.example.androidu.newsapp.NewsItem;

import java.util.ArrayList;

public class MyDatabase {

    private static final String TAG = "MyDatabase";
    private Context mContext;
    private DBHelper mDBHelper;
    private SQLiteDatabase mDatabase;
    private Cursor mCursor;
    private boolean mCursorPositionIsValid;

    public MyDatabase(Context context){
        mContext = context;
        mDBHelper = new DBHelper(context);
        mDatabase = null;
        mCursor = null;
        mCursorPositionIsValid = false;
    }

    public boolean open(){
        if(mDatabase != null){
            close();
        }

        mCursorPositionIsValid = false;

        try {
            mDatabase = mDBHelper.getWritableDatabase();
        }
        catch(SQLiteException e){
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean close(){
        if(mDatabase != null) {
            mDatabase.close();
            mDatabase = null;
        }

        if(mCursor != null){
            mCursor.close();
            mCursor = null;
        }

        mCursorPositionIsValid = false;
        return true;
    }

    public int selectAll(){
        mCursor = mDatabase.query(
                Contract.TABLE_NEWS.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                Contract.TABLE_NEWS.COLUMN_NAME_DATE
        );

        mCursorPositionIsValid = false;
        return mCursor.getCount();
    }

    public int getNumCursorRows(){
        if(mCursor == null){
            return 0;
        }

        return mCursor.getCount();
    }

    public boolean moveCursorToRow(int row){
        if(row < 0 || row >= mCursor.getCount()){
            return false;
        }

        mCursor.moveToPosition(row);
        mCursorPositionIsValid = true;
        return true;
    }

    public String getAuthor(){
        return mCursor.getString(mCursor.getColumnIndex(Contract.TABLE_NEWS.COLUMN_NAME_AUTHOR));
    }

    public String getTitle(){
        return mCursor.getString(mCursor.getColumnIndex(Contract.TABLE_NEWS.COLUMN_NAME_TITLE));
    }

    public String getDescription(){
        return mCursor.getString(mCursor.getColumnIndex(Contract.TABLE_NEWS.COLUMN_NAME_DESCRIPTION));
    }

    public String getUrl(){
        return mCursor.getString(mCursor.getColumnIndex(Contract.TABLE_NEWS.COLUMN_NAME_URL));
    }

    public String getImageUrl(){
        return mCursor.getString(mCursor.getColumnIndex(Contract.TABLE_NEWS.COLUMN_NAME_IMAGE_URL));
    }

    public String getDate(){
        return mCursor.getString(mCursor.getColumnIndex(Contract.TABLE_NEWS.COLUMN_NAME_DATE));
    }

    public int getId(){
        return mCursor.getInt(mCursor.getColumnIndex(Contract.TABLE_NEWS._ID));
    }

    public boolean addItem(NewsItem item){
        ContentValues cv = new ContentValues();
        cv.put(Contract.TABLE_NEWS.COLUMN_NAME_AUTHOR, item.getAuthor());
        cv.put(Contract.TABLE_NEWS.COLUMN_NAME_TITLE, item.getTitle());
        cv.put(Contract.TABLE_NEWS.COLUMN_NAME_DESCRIPTION, item.getDescription());
        cv.put(Contract.TABLE_NEWS.COLUMN_NAME_URL, item.getUrl());
        cv.put(Contract.TABLE_NEWS.COLUMN_NAME_IMAGE_URL, item.getImageUrl());
        cv.put(Contract.TABLE_NEWS.COLUMN_NAME_DATE, item.getDate());

        return mDatabase.insert(Contract.TABLE_NEWS.TABLE_NAME, null, cv) != -1;
    }

    public int addItems(ArrayList<NewsItem> newsItemList){
        for(NewsItem item : newsItemList){
            addItem(item);
        }

        return newsItemList.size();
    }

    public boolean deleteItem(int id){
        return mDatabase.delete(Contract.TABLE_NEWS.TABLE_NAME, Contract.TABLE_NEWS._ID + "=" + id, null) > 0;
    }

    public boolean clearItems(){

        mDatabase.execSQL("drop table if exists " + Contract.TABLE_NEWS.TABLE_NAME);

        String queryString = "CREATE TABLE " + Contract.TABLE_NEWS.TABLE_NAME + " ("+
                Contract.TABLE_NEWS._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                Contract.TABLE_NEWS.COLUMN_NAME_AUTHOR + " TEXT, " +
                Contract.TABLE_NEWS.COLUMN_NAME_TITLE + " TEXT, " +
                Contract.TABLE_NEWS.COLUMN_NAME_DESCRIPTION + " TEXT, " +
                Contract.TABLE_NEWS.COLUMN_NAME_URL + " TEXT, " +
                Contract.TABLE_NEWS.COLUMN_NAME_IMAGE_URL + " TEXT, " +
                Contract.TABLE_NEWS.COLUMN_NAME_DATE + " DATE ); ";

        mDatabase.execSQL(queryString);

        return true;
    }

}
