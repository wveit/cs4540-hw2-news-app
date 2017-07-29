/*
    Created this contract class to contain constants that will represent table name and
    column names
 */

package com.example.androidu.newsapp.database;

import android.provider.BaseColumns;

public class Contract {

    public static class TABLE_NEWS implements BaseColumns{
        public static final String TABLE_NAME = "newsitems";

        public static final String COLUMN_NAME_AUTHOR = "author";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_URL = "url";
        public static final String COLUMN_NAME_IMAGE_URL = "imageurl";
        public static final String COLUMN_NAME_DATE = "date";
    }
}
