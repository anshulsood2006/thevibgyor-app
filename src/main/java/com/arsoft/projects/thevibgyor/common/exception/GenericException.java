package com.arsoft.projects.thevibgyor.common.exception;

import lombok.Getter;

import java.time.ZonedDateTime;

@Getter
public class GenericException extends RuntimeException {

    private final Throwable throwable;
    private final ZonedDateTime requestTime;

    public GenericException(Throwable throwable, ZonedDateTime requestTime) {
        this.throwable = throwable;
        this.requestTime = requestTime;
    }
}
