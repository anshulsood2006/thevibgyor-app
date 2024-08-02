package com.arsoft.projects.thevibgyor.backend.controller.guest;

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
import org.springframework.web.client.ResourceAccessException;

import static com.arsoft.projects.thevibgyor.common.constant.Endpoint.GET_TOKEN_ENDPOINT;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ComponentScan(basePackages = "com.arsoft.projects.thevibgyor")
public class LoginControllerTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String ENDPOINT = GET_TOKEN_ENDPOINT.getValue();


    @Test
    public void testLoginAdminSuccess() throws Exception {
        HttpHeaders headers = TestUtil.getTestHeadersForAdmin();
        TokenRequest tokenRequest = new TokenRequest();
        HttpEntity<String> entity = new HttpEntity<>(objectMapper.writeValueAsString(tokenRequest), headers);
        ResponseEntity<TokenResponse> response = restTemplate.exchange(
                "http://localhost:" + port + ENDPOINT,
                HttpMethod.POST,
                entity,
                TokenResponse.class
        );
        TokenResponse user = response.getBody();
        assertNotNull(user);
    }

    @Test
    public void testLoginUserSuccess() throws Exception {
        HttpHeaders headers = TestUtil.getTestHeadersForUser();
        TokenRequest tokenRequest = new TokenRequest();
        HttpEntity<String> entity = new HttpEntity<>(objectMapper.writeValueAsString(tokenRequest), headers);
        ResponseEntity<TokenResponse> response = restTemplate.exchange(
                "http://localhost:" + port + ENDPOINT,
                HttpMethod.POST,
                entity,
                TokenResponse.class
        );
        TokenResponse user = response.getBody();
        assertNotNull(user);
    }

    @Test
    public void testLoginFailure() throws Exception {
        HttpHeaders headers = TestUtil.getTestHeadersForGuest();
        TokenRequest tokenRequest = new TokenRequest("3000");
        HttpEntity<String> entity = new HttpEntity<>(objectMapper.writeValueAsString(tokenRequest), headers);
        ResourceAccessException ex = assertThrows(ResourceAccessException.class, () -> restTemplate.exchange(
                "http://localhost:" + port + ENDPOINT,
                HttpMethod.POST,
                entity,
                TokenResponse.class
        ));
        assertNotNull(ex);
    }

}
