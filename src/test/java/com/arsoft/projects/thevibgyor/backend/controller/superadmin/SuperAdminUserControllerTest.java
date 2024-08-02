package com.arsoft.projects.thevibgyor.backend.controller.superadmin;

import com.arsoft.projects.thevibgyor.common.constant.ApiUri;
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
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;

import static com.arsoft.projects.thevibgyor.common.constant.Endpoint.SUPER_ADMIN_ENDPOINTS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ComponentScan(basePackages = "com.arsoft.projects.thevibgyor")
public class SuperAdminUserControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    public TestUtil testUtil;

    @LocalServerPort
    private int port;

    private static final String ENDPOINT = SUPER_ADMIN_ENDPOINTS.getValue() + ApiUri.GET_USERS;

    @Test
    public void testUserApiFailure() throws Exception {
        HttpHeaders headers = TestUtil.getTestHeadersForAdmin();
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ResourceAccessException ex = assertThrows(ResourceAccessException.class, () -> restTemplate.exchange(
                "http://localhost:" + port + ENDPOINT,
                HttpMethod.POST,
                entity,
                String.class
        ));
        assertNotNull(ex);
    }

    @Test
    public void testJwtLoginFailure() throws Exception {
        HttpHeaders headers = TestUtil.getTestHeadersWithExpiredJwt();
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

    @Test
    public void testLoginFailureForAdminToken() throws Exception {
        String jwt = testUtil.getJwtToken(new Role(1, "ADMIN"), port);
        HttpEntity<String> entity = testUtil.getTestEntityWithJwt(jwt);
        RestClientException ex = assertThrows(RestClientException.class, () -> restTemplate.exchange(
                "http://localhost:" + port + ENDPOINT,
                HttpMethod.GET,
                entity,
                TokenResponse.class
        ));
        assertNotNull(ex);
    }

    @Test
    public void testLoginFailureForUserToken() throws Exception {
        String jwt = testUtil.getJwtToken(new Role(1, "USER"), port);
        HttpEntity<String> entity = testUtil.getTestEntityWithJwt(jwt);
        RestClientException ex = assertThrows(RestClientException.class, () -> restTemplate.exchange(
                "http://localhost:" + port + ENDPOINT,
                HttpMethod.GET,
                entity,
                TokenResponse.class
        ));
        assertNotNull(ex);
        assertNotNull(ex);
    }

    @Test
    public void testLoginSuccessForSuperAdminToken() throws Exception {
        String jwt = testUtil.getJwtToken(new Role(0, "SUPER_ADMIN"), port);
        HttpEntity<String> entity = testUtil.getTestEntityWithJwt(jwt);
        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:" + port + ENDPOINT,
                HttpMethod.GET,
                entity,
                String.class
        );
        assertThat(response.getStatusCode().isSameCodeAs(HttpStatusCode.valueOf(200)));
        testUtil.assertSuccessResponse(response.getBody());
        testUtil.assertHeadersInResponse(response.getBody());
    }
}
