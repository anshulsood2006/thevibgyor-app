package com.arsoft.projects.thevibgyor.backend.controller.common;

import com.arsoft.projects.thevibgyor.backend.model.Role;
import com.arsoft.projects.thevibgyor.common.security.auth.token.model.TokenRequest;
import com.arsoft.projects.thevibgyor.common.security.auth.token.model.TokenResponse;
import com.arsoft.projects.thevibgyor.common.util.TestUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;

import java.util.Objects;

import static com.arsoft.projects.thevibgyor.common.constant.Endpoint.COMMON_ENDPOINTS;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ComponentScan(basePackages = "com.arsoft.projects.thevibgyor")
public class MenuControllerTests {
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    public TestUtil testUtil;

    @LocalServerPort
    private int port;

    private static final String ENDPOINT = COMMON_ENDPOINTS.getValue() + "/header-menus";

    @Test
    public void testJwtLoginFailure() throws Exception {
        HttpHeaders headers = TestUtil.getTestHeadersWithExpiredJwt();
        TokenRequest tokenRequest = new TokenRequest("3000");
        HttpEntity<String> entity = new HttpEntity<>(objectMapper.writeValueAsString(tokenRequest), headers);
        RestClientException ex = assertThrows(RestClientException.class, () -> restTemplate.exchange(
                "http://localhost:" + port + ENDPOINT,
                HttpMethod.GET,
                entity,
                TokenResponse.class
        ));
        assertNotNull(ex);
    }

    @Test
    public void testLoginSuccessForAdminToken() throws Exception {
        String jwt = testUtil.getJwtToken(new Role(1, "ADMIN"), port);
        HttpEntity<String> entity = testUtil.getTestEntityWithJwt(jwt);
        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:" + port + ENDPOINT,
                HttpMethod.GET,
                entity,
                String.class
        );
        assertTrue(Objects.requireNonNull(response.getBody()).contains("data"));
    }

    @Test
    public void testLoginSuccessForUserToken() throws Exception {
        String jwt = testUtil.getJwtToken(new Role(2, "USER"), port);
        HttpEntity<String> entity = testUtil.getTestEntityWithJwt(jwt);
        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:" + port + ENDPOINT,
                HttpMethod.GET,
                entity,
                String.class
        );
        assertTrue(Objects.requireNonNull(response.getBody()).contains("data"));
    }
}
