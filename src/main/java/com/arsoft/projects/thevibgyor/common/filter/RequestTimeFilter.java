package com.arsoft.projects.thevibgyor.common.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.ZonedDateTime;

@Slf4j
public class RequestTimeFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        //Adding time information to request attribute so that it is available in controller
        ZonedDateTime requestTime = ZonedDateTime.now();
        request.setAttribute("requestTime", requestTime);
        log.info("Request Time Filter is called to add requestTime info");
        filterChain.doFilter(request, response);
    }
}
