package com.jamesdev.penguinphoto.data;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Created by Administrator on 14-6-29.
 */
public class ConnectionManager {
    private static final String TAG = ConnectionManager.class.getSimpleName();
    private static final String COOKIE = "Set-Cookie";



    public static final String BASE_URL = "http://192.168.1.103:8000";
    public static final String ITEMS_URL = "/item?id=";
    public static final String THREADS_URL = "/threads?id=";
    public static final String SUBMISSIONS_URL = "/submitted?id=";
    public static final String SUBMIT_URL = "/submit";
    public static final String USER_AGENT = System.getProperty("http.agent");
    public static final int TIMEOUT_MILLIS = 40 * 1000;


    /**
     * Converts an id into a string containing the extension (everything that goes after .com) of the URL *
     */
    public static String itemIdToUrlExtension(long id) {
        return ITEMS_URL + Long.toString(id);
    }

    /**
     * Converts an id into a string the full URL for that id. *
     */
    public static String itemIdToUrl(long id) {
        return BASE_URL + itemIdToUrlExtension(id);
    }

    public static HttpResponse  doPost(String urlExtension, Map<String, String> parmas) {
        HttpResponse httpResponse = null;
        ArrayList<BasicNameValuePair> pairs = convertMapToNameValuePair(parmas);

        try {
            DefaultHttpClient client = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(BASE_URL + urlExtension);
            UrlEncodedFormEntity p_entity = new UrlEncodedFormEntity(pairs, "utf-8");
            httpPost.setEntity(p_entity);
            httpResponse = client.execute(httpPost);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return httpResponse;
    }

    public static String getCookieValue(HttpResponse httpResponse, String key) {
        HashMap<String, String> cookie = getCookie(httpResponse);
        return cookie.get(key);
    }

    public static String getResponseString(HttpResponse httpResponse) {
        HttpEntity entity = httpResponse.getEntity();
        InputStream content = null;
        try {
            content = entity.getContent();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String returnConnection = convertStreamToString(content);
        return returnConnection;
    }

    private static ArrayList<BasicNameValuePair> convertMapToNameValuePair(Map<String, String> parmas) {
        ArrayList<BasicNameValuePair> pairs = new ArrayList<BasicNameValuePair>();
        if (parmas != null) {
            Set<String> keys = parmas.keySet();
            for (Iterator<String> i = keys.iterator(); i.hasNext(); ) {
                String key = i.next();
                pairs.add(new BasicNameValuePair(key, parmas.get(key)));
            }
        }
        return pairs;
    }

    private static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    public static HashMap<String, String> getCookie(HttpResponse httpResponse) {
        Header[] headers = httpResponse.getHeaders(COOKIE);
        if (null == headers) {
            return null;
        }

        HashMap<String, String> cookieContiner = new HashMap<String, String>();
        for (int i = 0; i < headers.length; i++) {
            String cookie = headers[i].getValue();
            String[] cookieValues = cookie.split(";");
            for (int j = 0; j < cookieValues.length; j++) {
                String[] keyPair = cookieValues[j].split("=");
                String key = keyPair[0].trim();
                String value = keyPair.length > 1 ? keyPair[1].trim() : "";
                cookieContiner.put(key, value);
            }
        }
        return cookieContiner;
    }

    public static void addCookies(HttpPost request, HashMap<String, String> cookieContiner)
    {
        StringBuilder sb = new StringBuilder();
        Iterator iter = cookieContiner.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            String key = entry.getKey().toString();
            String val = entry.getValue().toString();
            sb.append(key);
            sb.append("=");
            sb.append(val);
            sb.append(";");
        }
        request.addHeader(COOKIE, sb.toString());
    }
}
