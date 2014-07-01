package com.jamesdev.penguinphoto.cache;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 14-6-8.
 */
public class DbHelperSingleton extends SQLiteOpenHelper{
    private static DbHelperSingleton mInstance = null;

    public static final String TABLE_PHOTOS = "images";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_CREATE_TIME = "createTime";
    public static final String COLUMN_LOCATION_DESCRIPTION = "locationDescription";
    public static final String COLUMN_GEOGRAPHY_LATITUDE= "geographyLatitude";
    public static final String COLUMN_GEOGRAPHY_LONGITUDE= "geographyLongitude";
    public static final String COLUMN_IS_UPLOADED = "isUploaded";

    private static final String DATABASE_NAME = "penguin_photo.db";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_PHOTOS + "("
            + COLUMN_ID  + " integer primary key autoincrement, "
            + COLUMN_TYPE + " text not null, "
            + COLUMN_NAME + " string not null, "
            + COLUMN_CREATE_TIME + " long not null, "
            + COLUMN_LOCATION_DESCRIPTION + " string, "
            + COLUMN_GEOGRAPHY_LATITUDE + " double not null, "
            + COLUMN_GEOGRAPHY_LONGITUDE + " double not null, "
            + COLUMN_IS_UPLOADED + " int default 0"
            + ");";

    private DbHelperSingleton(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static DbHelperSingleton getInstance(Context context) {
        /**
         * Use the application context as suggested by CommonsWare.
         * this will ensure that you don't accidentally leak an Activity's
         * context (see this article for more information:
         * http://developer.android.com/resources/articles/avoiding-memory-leaks.html)
         */
        if (mInstance == null) {
            mInstance = new DbHelperSingleton(context.getApplicationContext());
        }
        return mInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PHOTOS);
        onCreate(db);
    }
}
