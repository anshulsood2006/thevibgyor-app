package com.arsoft.projects.thevibgyor.backend.controller.superadmin;

import com.arsoft.projects.thevibgyor.common.constant.ApiUri;
import com.arsoft.projects.thevibgyor.common.constant.HttpStatus;
import com.arsoft.projects.thevibgyor.common.exception.GenericException;
import com.arsoft.projects.thevibgyor.backend.model.GenericResponse;
import com.arsoft.projects.thevibgyor.backend.model.GenericResponseInfo;
import com.arsoft.projects.thevibgyor.backend.model.Header;
import com.arsoft.projects.thevibgyor.backend.model.User;
import com.arsoft.projects.thevibgyor.backend.service.UserService;
import com.arsoft.projects.thevibgyor.common.util.HttpRequestUtil;
import com.arsoft.projects.thevibgyor.common.util.TimeUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping(ApiUri.SUPER_ADMIN)
public class SuperAdminUserController {

    @Autowired
    private UserService userService;

    @GetMapping(value = ApiUri.GET_USERS)
    public GenericResponse<List<User>> getUsers(@RequestAttribute("requestTime") ZonedDateTime requestTime, HttpServletRequest request) {
        log.info("Request has reached super admin user controller now");
        Header header;
        ZonedDateTime startZonedDateTime = Objects.requireNonNullElseGet(requestTime, ZonedDateTime::now);
        String url = HttpRequestUtil.getFullURL(request);
        try {
            List<User> userList = userService.getUsers();
            GenericResponseInfo<List<User>> genericResponseInfo = new GenericResponseInfo<>(true,
                    "users", userList.size(), userList);
            long elapsedTime = TimeUtil.getElapsedTimeInMillis(startZonedDateTime, ZonedDateTime.now());
            header = new Header(url, startZonedDateTime, ZonedDateTime.now(), elapsedTime, HttpStatus.OK.getValue());
            return new GenericResponse<>(header, genericResponseInfo);
        } catch (Exception e) {
            throw new GenericException(e, requestTime);
        }
    }

    @PostMapping(value = ApiUri.GET_USERS)
    public GenericResponse<User> createUser(@RequestAttribute("requestTime") ZonedDateTime requestTime, HttpServletRequest request, @RequestBody User user) {
        log.info("Request has reached super admin user controller now");
        Header header;
        ZonedDateTime startZonedDateTime = Objects.requireNonNullElseGet(requestTime, ZonedDateTime::now);
        String url = HttpRequestUtil.getFullURL(request);
        try {
            User userList = userService.createUser(user);
            GenericResponseInfo<User> genericResponseInfo = new GenericResponseInfo<>(false,
                    "users", 1, userList);
            long elapsedTime = TimeUtil.getElapsedTimeInMillis(startZonedDateTime, ZonedDateTime.now());
            header = new Header(url, startZonedDateTime, ZonedDateTime.now(), elapsedTime, HttpStatus.OK.getValue());
            return new GenericResponse<>(header, genericResponseInfo);
        } catch (Exception e) {
            throw new GenericException(e, requestTime);
        }
    }
}
