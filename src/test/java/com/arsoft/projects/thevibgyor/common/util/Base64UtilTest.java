package com.arsoft.projects.thevibgyor.common.util;

import com.arsoft.projects.thevibgyor.backend.model.Role;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class Base64UtilTest {

    @Test
    public void decode() {
        String SECRET_KEY = "MYSECRETKEYWHICHISOFATLEASTBITINSIZE";
        byte[] b = Base64Util.getDecodedString(SECRET_KEY);
        assertNotNull(b);
    }

    @Test
    public void encode() {
        String SECRET_KEY = "MYSECRETKEYWHICHISOFATLEASTBITINSIZE";
        String b = Base64Util.getEncodedString(SECRET_KEY);
        assertNotNull(b);
    }
}
