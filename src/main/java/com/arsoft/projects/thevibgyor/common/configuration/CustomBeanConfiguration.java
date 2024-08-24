package com.arsoft.projects.thevibgyor.common.configuration;

import com.arsoft.projects.thevibgyor.common.exception.ThrowableMixing;
import com.arsoft.projects.thevibgyor.backend.repository.HeaderMenuRepository;
import com.arsoft.projects.thevibgyor.backend.repository.UserRepository;
import com.arsoft.projects.thevibgyor.backend.repository.impl.HeaderMenuRepositoryImpl;
import com.arsoft.projects.thevibgyor.common.security.auth.CustomDaoAuthenticationProvider;
import com.arsoft.projects.thevibgyor.common.security.auth.CustomTokenRequestAuthenticationHandler;
import com.arsoft.projects.thevibgyor.common.security.auth.CustomTokenRequestAuthorizationHandler;
import com.arsoft.projects.thevibgyor.backend.repository.impl.UserRepositoryImpl;
import com.arsoft.projects.thevibgyor.backend.service.HeaderMenuService;
import com.arsoft.projects.thevibgyor.backend.service.impl.UserServiceImpl;
import com.arsoft.projects.thevibgyor.dictionary.repository.DictionaryRepository;
import com.arsoft.projects.thevibgyor.dictionary.repository.impl.DictionaryRepositoryImpl;
import com.arsoft.projects.thevibgyor.dictionary.service.DictionaryService;
import com.arsoft.projects.thevibgyor.dictionary.service.impl.DictionaryServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Slf4j
@Configuration
public class CustomBeanConfiguration {

//    @Bean
//    public UserDetailsService userDetailsService() {
//        UserDetails user = User.builder()
//                .username("akhil")
//                .password(passwordEncoder().encode("sood"))
//                .roles("USER")
//                .build();
//        UserDetails admin = User.builder()
//                .username("anshul")
//                .password(passwordEncoder().encode("sood"))
//                .roles("ADMIN")
//                .build();
//        log.info("userDetailsService has been set up.");
//        return new InMemoryUserDetailsManager(user, admin);
//    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        log.info("BCryptPasswordEncoder has been set up.");
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper().registerModule(new JavaTimeModule()).addMixIn(Throwable.class, ThrowableMixing.class);
    }

    @Bean
    public CustomTokenRequestAuthenticationHandler customTokenRequestAuthenticationHandler(ObjectMapper objectMapper) {
        return new CustomTokenRequestAuthenticationHandler(objectMapper);
    }

    @Bean
    public CustomTokenRequestAuthorizationHandler customTokenRequestAuthorizationHandler(ObjectMapper objectMapper) {
        return new CustomTokenRequestAuthorizationHandler(objectMapper);
    }

    @Bean
    public UserRepository userRepository() {
        return new UserRepositoryImpl();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new CustomDaoAuthenticationProvider();
        authProvider.setUserDetailsService(userService(userRepository()));
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public UserDetailsService userService(UserRepository userRepository) {
        return new UserServiceImpl(userRepository);
    }

    /**
     * This bean removes ROLE_ prefix from all the user roles
     */
    @Bean
    public GrantedAuthorityDefaults grantedAuthorityDefaults() {
        return new GrantedAuthorityDefaults(""); // Empty string removes the prefix
    }

    @Bean
    public HeaderMenuRepository headerMenuRepository() {
        return new HeaderMenuRepositoryImpl();
    }

    @Bean
    public HeaderMenuService headerMenuService() {
        return new HeaderMenuService();
    }

    @Bean
    public DictionaryService dictionaryService() {
        return new DictionaryServiceImpl();
    }

    @Bean
    public DictionaryRepository dictionaryRepository() {
        return new DictionaryRepositoryImpl();
    }
}
