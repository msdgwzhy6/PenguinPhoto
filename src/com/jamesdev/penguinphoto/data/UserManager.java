package com.jamesdev.penguinphoto.data;

import org.apache.http.HttpResponse;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 14-6-29.
 */
public class UserManager {
    private static final String LOGIN_URL_EXTENSION = "/accounts/login/";
    private static final String REGISTER_URL_EXTENSION = "/accounts/register/";
    private static final String CSRF_TOKEN = "csrfmiddlewaretoken";
    private static final String COOKIE_CSRF_TOKEN = "csrftoken";
    private static final String HEADER_CSRF_TOKEN = "X-CSRFToken";
    private static final String SESSION_ID = "sessionid";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String EMAIL = "email";

    public static final String RESPONSE_MSG = "msg";
    public static final String RESPONSE_CODE = "code";
    public static final int REQUEST_ERROR = 0;
    public static final int LOGINT_SUCCESS = REQUEST_ERROR + 1;
    public static final int REGISTER_SUCCESS = LOGINT_SUCCESS + 1;

    /**
     * Attempts to log the user into http://news.ycombinator.com. If successful, returns a user authentication cookie.
     * Else it returns null.
     **/
    public static String login(String username, String password) {
        try {
            Map<String, String> parmas = new HashMap<String, String>();
            parmas.put(USERNAME, username);
            parmas.put(PASSWORD, password);

            HttpResponse response = ConnectionManager.doPost(LOGIN_URL_EXTENSION, parmas);
            if (null == response) {
                return null;
            }

            int state = getResponseCode(response);
            return state == LOGINT_SUCCESS ? username : null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String register(String username, String email, String password){
        try {
            Map<String, String> parmas = new HashMap<String, String>();
            parmas.put(USERNAME, username);
            parmas.put(EMAIL, email);
            parmas.put(PASSWORD, password);
            HttpResponse response = ConnectionManager.doPost(REGISTER_URL_EXTENSION, parmas);
            if (null == response) {
                return null;
            }

            int state = getResponseCode(response);
            return state == REGISTER_SUCCESS ? username : null;
        } catch (Exception e) {

        }
        return null;
    }

    private static int getResponseCode(HttpResponse httpResponse) {
        int state = 0;
        String responseString = ConnectionManager.getResponseString(httpResponse);
        if (null == responseString) {
            return state;
        }
        try {
            JSONObject jsonObject = new JSONObject(responseString);
            state = (Integer)jsonObject.get(RESPONSE_CODE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return state;
    }
}
