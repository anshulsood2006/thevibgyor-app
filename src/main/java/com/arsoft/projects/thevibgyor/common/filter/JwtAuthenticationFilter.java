package com.arsoft.projects.thevibgyor.common.filter;

import com.arsoft.projects.thevibgyor.common.exception.Error;
import com.arsoft.projects.thevibgyor.common.exception.TheVibgyorException;
import com.arsoft.projects.thevibgyor.backend.model.Header;
import com.arsoft.projects.thevibgyor.backend.model.Role;
import com.arsoft.projects.thevibgyor.common.security.auth.token.service.JwtService;
import com.arsoft.projects.thevibgyor.backend.service.UserService;
import com.arsoft.projects.thevibgyor.common.util.HttpRequestUtil;
import com.arsoft.projects.thevibgyor.common.util.TimeUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.List;

import static com.arsoft.projects.thevibgyor.common.util.ContentTypeUtil.APPLICATION_JSON;

@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserService userService;
    @Autowired
    private ObjectMapper objectMapper;

    public JwtAuthenticationFilter(JwtService jwtService, UserService userService, ObjectMapper objectMapper) {
        this.jwtService = jwtService;
        this.userService = userService;
        this.objectMapper = objectMapper;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("JWT Authentication Filter is called to add user info");
        String authToken = HttpRequestUtil.getAuthToken(request);
        if (authToken == null || !jwtService.isValidToken(authToken)) {
            log.info("Handling case when JWT is not provided or is invalid");
            handleInvalidToken(request, response);
            return;
        }
        Claims claims = jwtService.extractAllClaims(authToken);
        UserDetails userDetails = this.userService.loadUserByUsername(claims.getSubject());
        List<Role> roles = jwtService.getRoles(authToken);
        List<SimpleGrantedAuthority> authorities = roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getRoleName()))
                .toList();
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails, authToken, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.info(String.format("Authenticated user is '%s' with roles: '%s'", userDetails.getUsername(), roles));
        filterChain.doFilter(request, response);
    }

    private void handleInvalidToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ZonedDateTime startZonedDateTime = HttpRequestUtil.getRequestTime(request);
        String url = HttpRequestUtil.getFullURL(request);
        int statusCode = HttpStatus.UNAUTHORIZED.value();
        response.setStatus(statusCode);
        ZonedDateTime endZonedDateTime = ZonedDateTime.now();
        long elapsedTime = TimeUtil.getElapsedTimeInMillis(startZonedDateTime, endZonedDateTime);
        Header header = new Header(url, startZonedDateTime, endZonedDateTime, elapsedTime, statusCode);
        Error error = new Error(header, "You need to provide a valid JWT token for authentication.");
        TheVibgyorException exception = new TheVibgyorException(error);
        response.setContentType(APPLICATION_JSON);
        response.getOutputStream().println(objectMapper.writeValueAsString(exception));
    }
}
