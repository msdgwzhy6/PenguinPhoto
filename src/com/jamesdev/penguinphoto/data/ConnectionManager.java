package com.jamesdev.penguinphoto.data;

import com.jamesdev.penguinphoto.app.framework.BaseApplication;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

/**
 * Created by Administrator on 14-6-29.
 */
public class ConnectionManager {
    private static final String TAG = ConnectionManager.class.getSimpleName();

    public static final String BASE_URL = "http://127.0.0.1";
    public static final String ITEMS_URL = "/item?id=";
    public static final String THREADS_URL = "/threads?id=";
    public static final String SUBMISSIONS_URL = "/submitted?id=";
    public static final String SUBMIT_URL = "/submit";
    public static final String USER_AGENT = System.getProperty("http.agent");
    public static final int TIMEOUT_MILLIS = 40 * 1000;

    /** Connects to server using the cookie you've provided **/
    public static Connection authConnect(String baseUrlExtension, String userCookie) {
        return anonConnect(baseUrlExtension).cookie("user", userCookie);
    }

    /** Connects to server with no user cookie authentication **/
    public static Connection anonConnect(String baseUrlExtension) {
        Connection conn = Jsoup.connect(ConnectionManager.BASE_URL + baseUrlExtension)
                .timeout(TIMEOUT_MILLIS)
                .userAgent(ConnectionManager.USER_AGENT);

        UserPrefs prefs = new UserPrefs(BaseApplication.getApp().getApplicationContext());

        boolean compress = prefs.getCompressData();
        if (compress) {
            conn.header("Accept-Encoding", "gzip");
        }

        return conn;
    }


    /** Converts an id into a string containing the extension (everything that goes after .com) of the URL **/
    public static String itemIdToUrlExtension(long id) {
        return ITEMS_URL + Long.toString(id);
    }

    /** Converts an id into a string the full URL for that id. **/
    public static String itemIdToUrl(long id) {
        return BASE_URL + itemIdToUrlExtension(id);
    }
}
