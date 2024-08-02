package com.arsoft.projects.thevibgyor.common.util;

import com.arsoft.projects.thevibgyor.backend.model.Role;
import com.arsoft.projects.thevibgyor.common.security.auth.token.model.TokenRequest;
import com.arsoft.projects.thevibgyor.common.security.auth.token.model.TokenResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static com.arsoft.projects.thevibgyor.common.constant.Endpoint.GET_TOKEN_ENDPOINT;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Component
public class TestUtil {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TestRestTemplate restTemplate;

    private static final String TOKEN_ENDPOINT = GET_TOKEN_ENDPOINT.getValue();

    private static final String EXPIRED_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJha2hpbCIsImlzcyI6Ik1ZX0FVVEhfQVBQI" +
            "iwicm9sZXMiOlsiR1VFU1QiLCJVU0VSIl0sImlhdCI6MTcyMDk0NTY5MiwiZXhwIjoxNzIwOTQ1OTkyfQ.tBvFwRmNU0F" +
            "mCvtrU0mSad_zD2oESMtYAKDmI4XGkNs";

    public static HttpHeaders getTestHeadersForAdmin() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + Base64Util.getEncodedString("anshul@gmail.com:password"));
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    public static HttpHeaders getTestHeadersForUser() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + Base64Util.getEncodedString("akhil@gmail.com:password"));
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    public static HttpHeaders getTestHeadersForSuperAdmin() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + Base64Util.getEncodedString("super@gmail.com:password"));
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    public static HttpHeaders getTestHeadersForGuest() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + Base64Util.getEncodedString("guest:password1"));
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    public static HttpHeaders getTestHeadersWithExpiredJwt() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + EXPIRED_TOKEN);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    public String getJwtToken(Role role, int port) throws JsonProcessingException {
        HttpHeaders headers = null;
        if (role.getRoleName().equals("ADMIN")) {
            headers = TestUtil.getTestHeadersForAdmin();
        } else if (role.getRoleName().equals("USER")) {
            headers = TestUtil.getTestHeadersForUser();
        }else if (role.getRoleName().equals("SUPER_ADMIN")) {
            headers = TestUtil.getTestHeadersForSuperAdmin();
        }
        TokenRequest tokenRequest = new TokenRequest("3000");
        HttpEntity<String> entity = new HttpEntity<>(objectMapper.writeValueAsString(tokenRequest), headers);
        ResponseEntity<TokenResponse> response = restTemplate.exchange(
                "http://localhost:" + port + TOKEN_ENDPOINT,
                HttpMethod.POST,
                entity,
                TokenResponse.class
        );
        TokenResponse tokenResponse = response.getBody();
        assert tokenResponse != null;
        return tokenResponse.getToken();
    }

    public HttpHeaders getTestHeadersWithJwt(String jwtString) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + jwtString);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    public HttpEntity<String> getTestEntityWithJwt(String jwt) throws JsonProcessingException {
        HttpHeaders headers = getTestHeadersWithJwt(jwt);
        TokenRequest tokenRequest = new TokenRequest("3000");
        return new HttpEntity<>(objectMapper.writeValueAsString(tokenRequest), headers);
    }

    public HttpEntity<String> getTestEntityWithJwtAndString(String jwt, String string) throws JsonProcessingException {
        HttpHeaders headers = getTestHeadersWithJwt(jwt);
        return new HttpEntity<>(string, headers);
    }

    public void assertSuccessResponse(String response){
        assertTrue(Objects.requireNonNull(response).contains("data"));
        assertTrue(Objects.requireNonNull(response).contains("multi"));
        assertTrue(Objects.requireNonNull(response).contains("key"));
        assertTrue(Objects.requireNonNull(response).contains("count"));
    }

    public void assertHeadersInResponse(String response){
        assertTrue(Objects.requireNonNull(response).contains("header"));
        assertTrue(Objects.requireNonNull(response).contains("url"));
        assertTrue(Objects.requireNonNull(response).contains("requestTime"));
        assertTrue(Objects.requireNonNull(response).contains("responseTime"));
        assertTrue(Objects.requireNonNull(response).contains("elapsedTimeInMillis"));
        assertTrue(Objects.requireNonNull(response).contains("httpStatus"));
    }


    public void assertFailureResponse(String response) {
        assertTrue(Objects.requireNonNull(response).contains("message"));
    }
}
