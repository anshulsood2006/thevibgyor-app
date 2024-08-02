package com.arsoft.projects.thevibgyor.common.exception;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * This class is used to suppress "suppressed" and "stackTrace" in response when we throw TheVibgyorException
 * We need to register it with the object mapper as
 * ObjectMapper objectMapper = new ObjectMapper().addMixIn(Throwable.class, ThrowableMixing.class);
 */
public abstract class ThrowableMixing {
    @JsonIgnore
    public abstract String getSuppressed();

    @JsonIgnore
    public abstract StackTraceElement[] getStackTrace();
}
