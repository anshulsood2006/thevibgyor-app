package com.arsoft.projects.thevibgyor.backend.controller.common;

import com.arsoft.projects.thevibgyor.common.constant.ApiUri;
import com.arsoft.projects.thevibgyor.common.constant.HttpStatus;
import com.arsoft.projects.thevibgyor.backend.model.GenericResponse;
import com.arsoft.projects.thevibgyor.backend.model.GenericResponseInfo;
import com.arsoft.projects.thevibgyor.backend.model.Header;
import com.arsoft.projects.thevibgyor.backend.model.Role;
import com.arsoft.projects.thevibgyor.backend.model.app.Menu;
import com.arsoft.projects.thevibgyor.common.security.auth.token.service.JwtService;
import com.arsoft.projects.thevibgyor.backend.service.HeaderMenuService;
import com.arsoft.projects.thevibgyor.common.util.HttpRequestUtil;
import com.arsoft.projects.thevibgyor.common.util.TimeUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;

import static com.arsoft.projects.thevibgyor.common.constant.ApiUri.HEADER_MENUS;

@Slf4j
@RestController
@RequestMapping(ApiUri.APP)
public class HeaderMenuController {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private HeaderMenuService headerMenuService;

    @GetMapping(value = HEADER_MENUS)
    public GenericResponse<List<Menu>> getHeaderMenus(@RequestAttribute("requestTime") ZonedDateTime requestTime, HttpServletRequest request) {
        log.info("Request has reached header-menus controller now");
        ZonedDateTime startZonedDateTime = Objects.requireNonNullElseGet(requestTime, ZonedDateTime::now);
        Header header;
        int httpStatus = HttpStatus.OK.getValue();
        String url = HttpRequestUtil.getFullURL(request);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String bearerToken = authentication.getCredentials().toString();
        List<Role> roles = jwtService.getRoles(bearerToken);
        log.info("Roles associated with the token are: " + roles);
        long elapsedTime = TimeUtil.getElapsedTimeInMillis(startZonedDateTime, ZonedDateTime.now());
        header = new Header(url, startZonedDateTime, ZonedDateTime.now(), elapsedTime, httpStatus);
        GenericResponseInfo<List<Menu>> genericResponseInfo = null;
        List<Menu> rolesList = headerMenuService.getHeadersByRoleId(roles);
        genericResponseInfo = new GenericResponseInfo<>(true,
                    "header-menus", rolesList.size(), rolesList);
        return new GenericResponse<>(header, genericResponseInfo);
    }
}
