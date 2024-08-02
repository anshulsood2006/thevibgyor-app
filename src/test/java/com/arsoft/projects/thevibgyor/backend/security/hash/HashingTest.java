package com.arsoft.projects.thevibgyor.backend.security.hash;

import com.arsoft.projects.thevibgyor.common.security.hash.Hashing;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HashingTest {
    @Test
    void hash(){
        Hashing hashing = new Hashing();
        String hash = hashing.getSha256Hash("anshulsood");
        assertEquals("e4df3bc80794cc7c467f08d562a23f8ddc0f73f809d3b23b76d9df6b81e2f494", hash);
    }
}
