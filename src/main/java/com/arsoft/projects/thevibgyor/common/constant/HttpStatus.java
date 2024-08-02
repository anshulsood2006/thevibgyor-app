package com.arsoft.projects.thevibgyor.common.constant;

public enum HttpStatus {
    OK(200),

    INTERNAL_SERVER_ERROR(500);
    private final int value;

    HttpStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
