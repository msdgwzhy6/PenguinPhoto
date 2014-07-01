package com.jamesdev.penguinphoto.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 14-6-7.
 */
public class TimeUtil {

    public static long getCurrentTimeMillis() {
        return  System.currentTimeMillis();
    }

    public static String getCurrentTimeString() {
        return convertToTime(getCurrentTimeMillis());
    }

    /**
     * long类型时间格式化
     */
    public static String convertToTime(long time) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(time);
        return df.format(date);
    }
}
