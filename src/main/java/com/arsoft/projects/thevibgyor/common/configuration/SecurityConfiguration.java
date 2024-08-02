package com.arsoft.projects.thevibgyor.common.configuration;

import com.arsoft.projects.thevibgyor.common.constant.Endpoint;
import com.arsoft.projects.thevibgyor.common.filter.JwtAuthenticationFilter;
import com.arsoft.projects.thevibgyor.common.filter.RequestTimeFilter;
import com.arsoft.projects.thevibgyor.common.security.auth.CustomTokenRequestAuthenticationHandler;
import com.arsoft.projects.thevibgyor.common.security.auth.CustomTokenRequestAuthorizationHandler;
import com.arsoft.projects.thevibgyor.common.security.auth.token.service.JwtService;
import com.arsoft.projects.thevibgyor.backend.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.DispatcherType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static com.arsoft.projects.thevibgyor.common.constant.Endpoint.ALL_ENDPOINTS;


@Slf4j
@Configuration
public class SecurityConfiguration {

    @Value("${cors.allowed-origins}")
    private String[] allowedOrigins;

    @Value("${cors.allowed-methods}")
    private String[] allowedMethods;
    @Value("${cors.allowed-headers}")
    private String[] allowedHeaders;

    @Value("${cors.allow-credentials}")
    private boolean allowCredentials;


    @Autowired
    private CustomTokenRequestAuthenticationHandler customTokenRequestAuthenticationHandler;
    @Autowired
    private CustomTokenRequestAuthorizationHandler customTokenRequestAuthorizationHandler;
    @Autowired
    private CustomTokenRequestAuthenticationHandler customRequestAuthenticationHandler;
    @Autowired
    private CustomTokenRequestAuthorizationHandler customRequestAuthorizationHandler;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserService userService;
    @Autowired
    private ObjectMapper objectMapper;

    @Bean
    public SecurityFilterChain basicAuthFilterChain(HttpSecurity http) throws Exception {
        http
                //This ensures that this filterChain gets applies to GET_TOKEN_ENDPOINT only
                .securityMatcher(Endpoint.GET_TOKEN_ENDPOINT.getValue())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authorize) -> authorize
                        .dispatcherTypeMatchers(DispatcherType.FORWARD, DispatcherType.ERROR).permitAll()
                        .requestMatchers(Endpoint.LOGGED_USER_ENDPOINTS.getValue() + ALL_ENDPOINTS.getValue()).permitAll()
                        .requestMatchers(Endpoint.ADMIN_ENDPOINTS.getValue() + ALL_ENDPOINTS.getValue()).permitAll()
                        .requestMatchers(Endpoint.COMMON_ENDPOINTS.getValue() + ALL_ENDPOINTS.getValue()).permitAll()
                        .requestMatchers(Endpoint.GET_TOKEN_ENDPOINT.getValue()).hasAnyRole("ADMIN", "USER", "GUEST")
                        .anyRequest().authenticated()
                )
                .exceptionHandling((exceptions) -> exceptions
                        .authenticationEntryPoint(customTokenRequestAuthenticationHandler)
                        .accessDeniedHandler(customTokenRequestAuthorizationHandler)
                )
                .httpBasic(httpBasicConfigurer ->
                        httpBasicConfigurer.authenticationEntryPoint(customTokenRequestAuthenticationHandler)
                )
                //Add RequestTimeFilter before BasicAuthenticationFilter to set requestTime in each request
                .addFilterBefore(new RequestTimeFilter(), BasicAuthenticationFilter.class);

        log.info("Basic authentication Security configuration has been set up.");
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain jwtAuthFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(new JwtAuthenticationFilter(jwtService, userService, objectMapper), UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests((authorize) -> authorize
                        .dispatcherTypeMatchers(DispatcherType.FORWARD, DispatcherType.ERROR).permitAll()
                        .requestMatchers(Endpoint.SUPER_ADMIN_ENDPOINTS.getValue() + ALL_ENDPOINTS.getValue()).hasAnyRole("SUPER_ADMIN")
                        .requestMatchers(Endpoint.ADMIN_ENDPOINTS.getValue() + ALL_ENDPOINTS.getValue()).hasAnyRole("ADMIN")
                        .requestMatchers(Endpoint.LOGGED_USER_ENDPOINTS.getValue() + ALL_ENDPOINTS.getValue()).hasAnyRole("ADMIN", "USER")
                        .requestMatchers(Endpoint.COMMON_ENDPOINTS.getValue() + ALL_ENDPOINTS.getValue()).hasAnyRole("ADMIN", "USER")
                        .anyRequest().authenticated()
                )
                .exceptionHandling((exceptions) -> exceptions
                        .authenticationEntryPoint(customRequestAuthenticationHandler)
                        .accessDeniedHandler(customRequestAuthorizationHandler)
                )
                .httpBasic(httpBasicConfigurer ->
                        httpBasicConfigurer.authenticationEntryPoint(customRequestAuthenticationHandler)
                )
                //Add RequestTimeFilter before JwtAuthenticationFilter to set requestTime in each request
                .addFilterBefore(new RequestTimeFilter(), JwtAuthenticationFilter.class);
        log.info("JWT authentication Security configuration has been set up.");
        return http.build();
    }

    /**
     * This configuration has been added to fix CORS error while trying to call the APIs via front end.
     *
     * @return WebMvcConfigurer
     */
    @Bean
    public WebMvcConfigurer corsConfigurationProvider() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry corsRegistry) {
                corsRegistry.addMapping("/**")
                        .allowedOrigins(allowedOrigins)
                        .allowedMethods(allowedMethods)
                        .allowedHeaders(allowedHeaders)
                        .allowCredentials(allowCredentials);
            }
        };
    }
}
