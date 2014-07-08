package com.jamesdev.penguinphoto.data;

import org.apache.http.HttpResponse;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 14-6-29.
 */
public class LoginManager {
    private static final String LOGIN_URL_EXTENSION = "/accounts/login/";
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
            Map<String, String> parmas = new HashMap<String, String>();
            parmas.put("username", username);
            parmas.put("password", password);

            HttpResponse response = ConnectionManager.doPost(LOGIN_URL_EXTENSION, parmas);
            if (null == response) {
                return null;
            }

            String cookie = ConnectionManager.getCookieValue(response, USERNAME);
            return cookie;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
