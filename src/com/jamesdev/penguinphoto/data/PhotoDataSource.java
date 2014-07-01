package com.jamesdev.penguinphoto.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.jamesdev.penguinphoto.cache.DbHelperSingleton;
import com.jamesdev.penguinphoto.model.Photo;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 14-6-8.
 */
public class PhotoDataSource {
    private SQLiteDatabase mDataBase;
    private DbHelperSingleton mDbHelper;
    private String[] mAllColumns = {DbHelperSingleton.COLUMN_ID,
        DbHelperSingleton.COLUMN_TYPE,
        DbHelperSingleton.COLUMN_NAME,
        DbHelperSingleton.COLUMN_CREATE_TIME,
        DbHelperSingleton.COLUMN_LOCATION_DESCRIPTION,
        DbHelperSingleton.COLUMN_GEOGRAPHY_LATITUDE,
        DbHelperSingleton.COLUMN_GEOGRAPHY_LONGITUDE,
        DbHelperSingleton.COLUMN_IS_UPLOADED};

    public PhotoDataSource(Context context) {
        mDbHelper = DbHelperSingleton.getInstance(context);
    }

    public void open(){
        mDataBase = mDbHelper.getWritableDatabase();
    }

    public void close() {
        mDbHelper.close();
    }

    public Photo createPhoto(String type, String name, long createTime, String locationDescription,
                             double latitude, double longitude) {
        ContentValues values = new ContentValues();
        values.put(DbHelperSingleton.COLUMN_TYPE, type);
        values.put(DbHelperSingleton.COLUMN_NAME, name);
        values.put(DbHelperSingleton.COLUMN_CREATE_TIME, createTime);
        values.put(DbHelperSingleton.COLUMN_LOCATION_DESCRIPTION, locationDescription);
        values.put(DbHelperSingleton.COLUMN_GEOGRAPHY_LATITUDE, latitude);
        values.put(DbHelperSingleton.COLUMN_GEOGRAPHY_LONGITUDE, longitude);
        long insertId = mDataBase.insert(DbHelperSingleton.TABLE_PHOTOS, null, values);
        Cursor cursor = mDataBase.query(DbHelperSingleton.TABLE_PHOTOS,
                mAllColumns, DbHelperSingleton.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Photo newPhoto = cursorToPhoto(cursor);
        cursor.close();
        return newPhoto;
    }

    public void setUploaded(Photo photo) {
        long id = photo.getId();
        photo.setUploaded(1);
        ContentValues values = photo.buildContentValues();

        mDataBase.update(DbHelperSingleton.TABLE_PHOTOS, values, DbHelperSingleton.COLUMN_ID + " = " + id, null);
    }

    public void deletePhoto(Photo photo) {
        long id = photo.getId();
        mDataBase.delete(DbHelperSingleton.TABLE_PHOTOS, DbHelperSingleton.COLUMN_ID + " = " + id, null);
    }

    public void deletePhoto(String photoName) {
        mDataBase.delete(DbHelperSingleton.TABLE_PHOTOS, DbHelperSingleton.COLUMN_NAME + " = " + photoName, null);
    }

    public List<Photo> getAllPhotos() {
        List<Photo> photos = new ArrayList<Photo>();
        Cursor cursor = mDataBase.query(DbHelperSingleton.TABLE_PHOTOS,
                mAllColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Photo photo = cursorToPhoto(cursor);
            photos.add(photo);
            cursor.moveToNext();
        }
        cursor.close();
        return photos;
    }

    private Photo cursorToPhoto(Cursor cursor) {
        Photo photo = new Photo();
        photo.setId(cursor.getLong(0));
        photo.setType(cursor.getString(1));
        photo.setName(cursor.getString(2));
        photo.setCreateTime(cursor.getLong(3));
        photo.setLocationDescription(cursor.getString(4));
        photo.setGeographyLatitude(cursor.getDouble(5));
        photo.setGeographyLongitude(cursor.getDouble(6));
        photo.setUploaded(cursor.getInt(7));
        return photo;
    }
}
