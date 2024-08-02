package com.arsoft.projects.thevibgyor.common.exception.handler;

import com.arsoft.projects.thevibgyor.common.exception.Error;
import com.arsoft.projects.thevibgyor.common.exception.GenericException;
import com.arsoft.projects.thevibgyor.common.exception.TheVibgyorException;
import com.arsoft.projects.thevibgyor.backend.model.Header;
import com.arsoft.projects.thevibgyor.common.util.HttpRequestUtil;
import com.arsoft.projects.thevibgyor.common.util.TimeUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.ZonedDateTime;
import java.util.Objects;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public TheVibgyorException handleMethodNotSupportedException(HttpServletRequest request, HttpServletResponse response, Exception ex) {
        ZonedDateTime startZonedDateTime = ZonedDateTime.now();
        String url = HttpRequestUtil.getFullURL(request);
        int statusCode = HttpStatus.FORBIDDEN.value();
        response.setStatus(statusCode);
        ZonedDateTime endZonedDateTime = ZonedDateTime.now();
        long elapsedTime = TimeUtil.getElapsedTimeInMillis(startZonedDateTime, endZonedDateTime);
        Header header = new Header(url, startZonedDateTime, endZonedDateTime, elapsedTime, statusCode);
        Error error = new Error(header, "You are using an unsupported HTTP method to call the endpoint.");
        log.error("Handling 405 method not supported exception");
        return new TheVibgyorException(error);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public TheVibgyorException handleNoHandlerFound(HttpServletRequest request, HttpServletResponse response, Exception ex) {
        ZonedDateTime startZonedDateTime = ZonedDateTime.now();
        String url = HttpRequestUtil.getFullURL(request);
        int statusCode = HttpStatus.FORBIDDEN.value();
        response.setStatus(statusCode);
        ZonedDateTime endZonedDateTime = ZonedDateTime.now();
        long elapsedTime = TimeUtil.getElapsedTimeInMillis(startZonedDateTime, endZonedDateTime);
        Header header = new Header(url, startZonedDateTime, endZonedDateTime, elapsedTime, statusCode);
        Error error = new Error(header, "You are trying to call a non existing endpoint.");
        log.error("Handling 404 page not found exception");
        return new TheVibgyorException(error);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public TheVibgyorException handleHttpMessageNotReadableException(HttpServletRequest request, HttpServletResponse response, HttpMessageNotReadableException ex) {
        ZonedDateTime startZonedDateTime = ZonedDateTime.now();
        String url = HttpRequestUtil.getFullURL(request);
        int statusCode = HttpStatus.BAD_REQUEST.value();
        response.setStatus(statusCode);
        ZonedDateTime endZonedDateTime = ZonedDateTime.now();
        long elapsedTime = TimeUtil.getElapsedTimeInMillis(startZonedDateTime, endZonedDateTime);
        Header header = new Header(url, startZonedDateTime, endZonedDateTime, elapsedTime, statusCode);
        Error error = new Error(header, String.format("Your request is not valid. %s.", ex.getMessage().substring(0, ex.getMessage().indexOf(":"))));
        log.error("Bad request: Cause " + ex.getMessage());
        return new TheVibgyorException(error);
    }

    @ExceptionHandler(GenericException.class)
    public TheVibgyorException handleGenericException(HttpServletRequest request, HttpServletResponse response, GenericException ex) {
        ZonedDateTime startZonedDateTime = Objects.requireNonNullElseGet(ex.getRequestTime(), ZonedDateTime::now);
        String url = HttpRequestUtil.getFullURL(request);
        int statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
        response.setStatus(statusCode);
        ZonedDateTime endZonedDateTime = ZonedDateTime.now();
        long elapsedTime = TimeUtil.getElapsedTimeInMillis(startZonedDateTime, endZonedDateTime);
        Header header = new Header(url, startZonedDateTime, endZonedDateTime, elapsedTime, statusCode);
        Error error = new Error(header, "There was an internal server error while calling the endpoint");
        return new TheVibgyorException(error);
    }

    @ExceptionHandler(BadRequestException.class)
    public TheVibgyorException handleBadRequestException(HttpServletRequest request, HttpServletResponse response, BadRequestException ex) {
        ZonedDateTime startZonedDateTime = ZonedDateTime.now();
        String url = HttpRequestUtil.getFullURL(request);
        int statusCode = HttpStatus.BAD_REQUEST.value();
        response.setStatus(statusCode);
        ZonedDateTime endZonedDateTime = ZonedDateTime.now();
        long elapsedTime = TimeUtil.getElapsedTimeInMillis(startZonedDateTime, endZonedDateTime);
        Header header = new Header(url, startZonedDateTime, endZonedDateTime, elapsedTime, statusCode);
        Error error = new Error(header, String.format("Your request is not valid. %s.", ex.getMessage() == null ? ex.getMessage() : ex.getMessage().indexOf(":") > 0 ? ex.getMessage().substring(0, ex.getMessage().indexOf(":")) : ex.getMessage()));
        log.error("Bad request: " + ex.getMessage());
        return new TheVibgyorException(error);
    }
}
