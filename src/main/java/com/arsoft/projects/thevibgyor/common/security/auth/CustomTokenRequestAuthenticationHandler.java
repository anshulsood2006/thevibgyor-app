package com.arsoft.projects.thevibgyor.common.security.auth;

import com.arsoft.projects.thevibgyor.common.exception.Error;
import com.arsoft.projects.thevibgyor.common.exception.TheVibgyorException;
import com.arsoft.projects.thevibgyor.backend.model.Header;
import com.arsoft.projects.thevibgyor.common.util.HttpRequestUtil;
import com.arsoft.projects.thevibgyor.common.util.TimeUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.ZonedDateTime;

import static com.arsoft.projects.thevibgyor.common.util.ContentTypeUtil.APPLICATION_JSON;

@Slf4j
@Component
public class CustomTokenRequestAuthenticationHandler implements AuthenticationEntryPoint {

    @Autowired
    private ObjectMapper objectMapper;

    public CustomTokenRequestAuthenticationHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        log.info("Custom Token Request Authentication Handler is called due to exception " + authException.getMessage());
        ZonedDateTime startZonedDateTime = ZonedDateTime.now();
        String url = HttpRequestUtil.getFullURL(request);
        int statusCode = HttpStatus.UNAUTHORIZED.value();
        response.setStatus(statusCode);
        ZonedDateTime endZonedDateTime = ZonedDateTime.now();
        long elapsedTime = TimeUtil.getElapsedTimeInMillis(startZonedDateTime, endZonedDateTime);
        Header header = new Header(url, startZonedDateTime, endZonedDateTime, elapsedTime, statusCode);
        Error error = new Error(header, "You need to provide a valid username and password for basic authorization.");
        TheVibgyorException exception = new TheVibgyorException(error);
        response.setContentType(APPLICATION_JSON);
        response.getOutputStream().println(objectMapper.writeValueAsString(exception));
    }
}
