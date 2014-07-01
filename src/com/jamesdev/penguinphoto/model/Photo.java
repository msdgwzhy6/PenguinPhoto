package com.jamesdev.penguinphoto.model;

import android.content.ContentValues;
import com.jamesdev.penguinphoto.cache.DbHelperSingleton;

/**
 * Created by Administrator on 14-6-8.
 */
public class Photo {
    private long id;
    private String type;
    private String name;
    private long createTime;
    private String locationDescription;
    private double geographyLatitude;
    private double geographyLongitude;
    private int uploaded;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getLocationDescription() {
        return locationDescription;
    }

    public void setLocationDescription(String locationDescription) {
        this.locationDescription = locationDescription;
    }

    public double getGeographyLatitude() {
        return geographyLatitude;
    }

    public double getGeographyLongitude() {
        return geographyLongitude;
    }

    public void setGeographyLongitude(double geographyLongitude) {
        this.geographyLongitude = geographyLongitude;
    }

    public void setGeographyLatitude(double geographyLatitude) {
        this.geographyLatitude = geographyLatitude;
    }

    public int isUploaded() {
        return uploaded;
    }

    public void setUploaded(int uploaded) {
        this.uploaded = uploaded;
    }

    public ContentValues buildContentValues() {
        ContentValues values = new ContentValues();
        values.put(DbHelperSingleton.COLUMN_ID, id);
        values.put(DbHelperSingleton.COLUMN_TYPE, type);
        values.put(DbHelperSingleton.COLUMN_NAME, name);
        values.put(DbHelperSingleton.COLUMN_CREATE_TIME, createTime);
        values.put(DbHelperSingleton.COLUMN_LOCATION_DESCRIPTION, locationDescription);
        values.put(DbHelperSingleton.COLUMN_GEOGRAPHY_LATITUDE, geographyLatitude);
        values.put(DbHelperSingleton.COLUMN_GEOGRAPHY_LONGITUDE, geographyLongitude);
        values.put(DbHelperSingleton.COLUMN_IS_UPLOADED, isUploaded());
        return values;
    }

    @Override
    public String toString() {
        return "Photo{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", createTime=" + createTime +
                ", locationDescription='" + locationDescription + '\'' +
                ", geographyLatitude=" + geographyLatitude +
                ", geographyLongitude=" + geographyLongitude +
                ", uploaded=" + uploaded +
                '}';
    }
}
