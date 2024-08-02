package com.arsoft.projects.thevibgyor.common.util;

import lombok.extern.slf4j.Slf4j;

import java.util.Base64;

@Slf4j
public class Base64Util {

    public static String getEncodedString(String string) {
        return Base64.getEncoder().encodeToString(string.getBytes());
    }

    public static byte[] getDecodedString(String string) {
        return Base64.getDecoder().decode(string);
    }
}
