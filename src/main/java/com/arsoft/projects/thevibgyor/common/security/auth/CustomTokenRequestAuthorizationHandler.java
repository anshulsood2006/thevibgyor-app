package com.arsoft.projects.thevibgyor.common.security.auth;

import com.arsoft.projects.thevibgyor.common.exception.Error;
import com.arsoft.projects.thevibgyor.common.exception.TheVibgyorException;
import com.arsoft.projects.thevibgyor.backend.model.Header;
import com.arsoft.projects.thevibgyor.common.util.HttpRequestUtil;
import com.arsoft.projects.thevibgyor.common.util.TimeUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.ZonedDateTime;

import static com.arsoft.projects.thevibgyor.common.util.ContentTypeUtil.APPLICATION_JSON;

@Slf4j
@Component
public class CustomTokenRequestAuthorizationHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    public CustomTokenRequestAuthorizationHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        log.info("Custom Token Request Authorization Handler is called due to exception " + accessDeniedException.getMessage());
        ZonedDateTime startZonedDateTime = ZonedDateTime.now();
        String url = HttpRequestUtil.getFullURL(request);
        int statusCode = HttpStatus.FORBIDDEN.value();
        response.setStatus(statusCode);
        ZonedDateTime endZonedDateTime = ZonedDateTime.now();
        long elapsedTime = TimeUtil.getElapsedTimeInMillis(startZonedDateTime, endZonedDateTime);
        Header header = new Header(url, startZonedDateTime, endZonedDateTime, elapsedTime, statusCode);
        Error error = new Error(header, "You are not authorized to call this endpoint.");
        TheVibgyorException exception = new TheVibgyorException(error);
        response.setContentType(APPLICATION_JSON);
        response.getOutputStream().println(objectMapper.writeValueAsString(exception));
    }
}
