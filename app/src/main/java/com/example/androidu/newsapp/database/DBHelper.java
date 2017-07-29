/*
    This DBHelper class is created to make it easy to create the database upon first use, re-create
    the database upon version upgrade, and open new connections to this app's database.

    This class contains constants to indicate the current version of the database (schema version)
    and the name of the databse file where the databse will be written to and read from.

    The onCreate(...) method was overridden in order to specify the query that will create the
    one database table when the database is first made or upgraded.

    The onUpgrage(...) method was overridden in order to specify how to handle a database upgrade.
    In this case it simply drops and re-creates the table. All existing data will be lost in this
    case.

    This class inherits the getWritableDatabase() method from SQLiteOpenHelper, which opens a new
    connection to the database and returns a database object.
 */

package com.example.androidu.newsapp.database;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "newsitems.db";

    public DBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String queryString = "CREATE TABLE " + Contract.TABLE_NEWS.TABLE_NAME + " ("+
                Contract.TABLE_NEWS._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                Contract.TABLE_NEWS.COLUMN_NAME_AUTHOR + " TEXT, " +
                Contract.TABLE_NEWS.COLUMN_NAME_TITLE + " TEXT, " +
                Contract.TABLE_NEWS.COLUMN_NAME_DESCRIPTION + " TEXT, " +
                Contract.TABLE_NEWS.COLUMN_NAME_URL + " TEXT, " +
                Contract.TABLE_NEWS.COLUMN_NAME_IMAGE_URL + " TEXT, " +
                Contract.TABLE_NEWS.COLUMN_NAME_DATE + " DATE ); ";

        db.execSQL(queryString);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + Contract.TABLE_NEWS.TABLE_NAME);
        onCreate(db);
    }


}
