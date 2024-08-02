package com.arsoft.projects.thevibgyor.common.filter;


import com.arsoft.projects.thevibgyor.backend.model.User;
import com.arsoft.projects.thevibgyor.backend.service.UserService;
import com.arsoft.projects.thevibgyor.common.util.HttpRequestUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class TokenRequestFilter extends OncePerRequestFilter {

    @Autowired
    private final UserService userService;

    public TokenRequestFilter(UserService userService) {
        this.userService = userService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("Token Request Filter is called to add user info");
        String authorizedUser = HttpRequestUtil.getAuthUser(request);
        User user = userService.getUser(authorizedUser);
        //Adding user information to request attribute so that it is available in controller
        request.setAttribute("user", user);
        filterChain.doFilter(request, response);
    }
}
