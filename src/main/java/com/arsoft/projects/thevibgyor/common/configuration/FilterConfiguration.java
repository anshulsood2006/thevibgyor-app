package com.arsoft.projects.thevibgyor.common.configuration;

import com.arsoft.projects.thevibgyor.common.constant.Endpoint;
import com.arsoft.projects.thevibgyor.common.filter.JwtAuthenticationFilter;
import com.arsoft.projects.thevibgyor.common.filter.TokenRequestFilter;
import com.arsoft.projects.thevibgyor.common.security.auth.token.service.JwtService;
import com.arsoft.projects.thevibgyor.backend.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.arsoft.projects.thevibgyor.common.constant.Endpoint.ALL_ENDPOINTS;

@Configuration
public class FilterConfiguration {

    @Autowired
    private JwtService jwtService;
    @Autowired
    private ObjectMapper objectMapper;

    @Bean
    public FilterRegistrationBean<TokenRequestFilter> basicAuthenticationFilter(UserService userService) {
        FilterRegistrationBean<TokenRequestFilter> registrationBean = new FilterRegistrationBean<>();
        TokenRequestFilter tokenRequestFilter = new TokenRequestFilter(userService);
        registrationBean.setFilter(tokenRequestFilter);
        registrationBean.addUrlPatterns(Endpoint.GET_TOKEN_ENDPOINT.getValue());
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<JwtAuthenticationFilter> jwtAuthenticationFilter(UserService userService) {
        FilterRegistrationBean<JwtAuthenticationFilter> registrationBean = new FilterRegistrationBean<>();
        JwtAuthenticationFilter tokenRequestFilter = new JwtAuthenticationFilter(jwtService, userService, objectMapper);
        registrationBean.setFilter(tokenRequestFilter);
        registrationBean.addUrlPatterns(Endpoint.ADMIN_ENDPOINTS.getValue() + ALL_ENDPOINTS.getValue(), Endpoint.LOGGED_USER_ENDPOINTS.getValue() + ALL_ENDPOINTS.getValue());
        return registrationBean;
    }
}
