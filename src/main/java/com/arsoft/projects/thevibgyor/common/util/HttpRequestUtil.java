package com.arsoft.projects.thevibgyor.common.util;

import com.arsoft.projects.thevibgyor.backend.model.User;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import java.time.ZonedDateTime;


@Slf4j
public class HttpRequestUtil {
    public static String getFullURL(HttpServletRequest request) {
        StringBuilder requestURL = new StringBuilder(request.getRequestURL().toString());
        String queryString = request.getQueryString();
        log.info("getFullURL is called.");
        if (queryString == null) {
            return requestURL.toString();
        } else {
            return requestURL.append('?').append(queryString).toString();
        }
    }

    public static String getAuthUser(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        String username = null;
        if (authHeader != null && authHeader.startsWith("Basic ")) {
            String base64Credentials = authHeader.substring(6);
            String credentials = new String(Base64Util.getDecodedString(base64Credentials));
            String[] values = credentials.split(":", 2);
            username = values[0];
        }
        return username;
    }

    public static String getAuthToken(HttpServletRequest request) {
        String authToken = request.getHeader("Authorization");
        if (authToken == null || !authToken.startsWith("Bearer ")) {
            return authToken;
        }
        return authToken.substring(7);
    }

    public static ZonedDateTime getRequestTime(HttpServletRequest request) {
        ZonedDateTime zonedDateTime = (ZonedDateTime) request.getAttribute("requestTime");
        if (zonedDateTime == null) {
            return ZonedDateTime.now();
        }
        return zonedDateTime;
    }

    public static User getRequestUser(HttpServletRequest request) {
        return (User) request.getAttribute("user");
    }
}
