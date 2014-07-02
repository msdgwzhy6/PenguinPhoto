package com.jamesdev.penguinphoto.data;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Connection;
import org.jsoup.nodes.Document;

/**
 * Created by Administrator on 14-6-29.
 */
public class LoginManager {
    private static final String LOGIN_URL_EXTENSION = "/login";

    /**
     * Attempts to log the user into http://news.ycombinator.com. If successful, returns a user authentication cookie.
     * Else it returns null.
     **/
    public static String login(String username, String password) {
        try {
            Connection.Response loginResponse = ConnectionManager.anonConnect(LOGIN_URL_EXTENSION)
                    .method(Connection.Method.GET)
                    .execute();
            Document loginPage = loginResponse.parse();
            String fnid = loginPage.select("input[name=fnid]")
                    .attr("value");

            Connection.Response response = ConnectionManager.anonConnect("/y")
                    .data("fnid", fnid)
                    .data("u", username)
                    .data("p", password)
                    .header("Origin", ConnectionManager.BASE_URL)
                    .followRedirects(true)
                    .referrer(ConnectionManager.BASE_URL + LOGIN_URL_EXTENSION)
                    .method(Connection.Method.POST)
                    .execute();

            String cookie = response.cookie("user");
            if (StringUtils.isNotBlank(cookie)) {
                return cookie;
            }
        } catch (Exception e) {
            // connection error
        }
        return null;
    }
}
