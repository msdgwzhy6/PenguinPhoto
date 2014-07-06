package com.jamesdev.penguinphoto.data;

import com.jamesdev.penguinphoto.model.Photo;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.jsoup.Connection;
import org.jsoup.nodes.Document;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.util.*;

/**
 * Created by Administrator on 14-6-29.
 */
public class LoginManager {
    private static final String LOGIN_URL_EXTENSION = "/accounts/login";
    private static final String CSRF_TOKEN = "csrfmiddlewaretoken";
    private static final String COOKIE_CSRF_TOKEN = "csrftoken";
    private static final String HEADER_CSRF_TOKEN = "X-CSRFToken";
    private static final String SESSION_ID = "sessionid";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";

    /**
     * Attempts to log the user into http://news.ycombinator.com. If successful, returns a user authentication cookie.
     * Else it returns null.
     **/
    public static String login(String username, String password) {
        try {
            Connection.Response loginResponse = ConnectionManager.anonConnect(LOGIN_URL_EXTENSION)
                    .method(Connection.Method.GET)
                    .execute();

/*            Connection.Response response = ConnectionManager.anonConnect(LOGIN_URL_EXTENSION + "/")
                    .data(USERNAME, username)
                    .data(PASSWORD, password)
                    .header("Origin", ConnectionManager.BASE_URL)
                    .followRedirects(true)
                    .referrer(ConnectionManager.BASE_URL + LOGIN_URL_EXTENSION)
                    .method(Connection.Method.POST)
                    .execute();*/

            dopost(username, password, loginResponse);

/*            String cookie = response.cookie("user");
            if (StringUtils.isNotBlank(cookie)) {
                return cookie;
            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void dopost(String username, String password, Connection.Response loginResponse){
        String csrfToken = loginResponse.cookie(COOKIE_CSRF_TOKEN);
        String sessionId = loginResponse.cookie(SESSION_ID);
        //封装数据
        Map<String, String> parmas = new HashMap<String, String>();
        parmas.put("username", username);
        parmas.put("password", password);
        parmas.put(CSRF_TOKEN, csrfToken);

        DefaultHttpClient client = new DefaultHttpClient();//http客户端
        HttpPost httpPost = new HttpPost(ConnectionManager.BASE_URL + LOGIN_URL_EXTENSION + "/");

        ArrayList<BasicNameValuePair> pairs = new ArrayList<BasicNameValuePair>();
        if(parmas != null){
            Set<String> keys = parmas.keySet();
            for(Iterator<String> i = keys.iterator(); i.hasNext();) {
                String key = (String)i.next();
                pairs.add(new BasicNameValuePair(key, parmas.get(key)));
            }
        }

        try {
            UrlEncodedFormEntity p_entity = new UrlEncodedFormEntity(pairs, "utf-8");
         /*
          *  将POST数据放入HTTP请求
          */
            httpPost.setEntity(p_entity);
          //  httpPost.addHeader("User-Agent", ConnectionManager.USER_AGENT);
      //      httpPost.addHeader("Set-Cookie", COOKIE_CSRF_TOKEN + "=" + csrfToken + ";" + SESSION_ID + "=" + sessionId);

         /*
          *  发出实际的HTTP POST请求
           */
            client.getParams().setParameter(CoreProtocolPNames.USER_AGENT,  ConnectionManager.USER_AGENT);
            HttpResponse response = client.execute(httpPost);
            HttpEntity entity = response.getEntity();
            InputStream content = entity.getContent();
            String returnConnection = convertStreamToString(content);
            System.out.print(returnConnection);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

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
}
