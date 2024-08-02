package com.arsoft.projects.thevibgyor.common.constant;

import lombok.Getter;

import static com.arsoft.projects.thevibgyor.common.constant.ApiUri.ADMIN;
import static com.arsoft.projects.thevibgyor.common.constant.ApiUri.ALL;
import static com.arsoft.projects.thevibgyor.common.constant.ApiUri.APP;
import static com.arsoft.projects.thevibgyor.common.constant.ApiUri.GET_TOKEN;
import static com.arsoft.projects.thevibgyor.common.constant.ApiUri.SUPER_ADMIN;
import static com.arsoft.projects.thevibgyor.common.constant.ApiUri.USER;

@Getter
public enum Endpoint {

    GET_TOKEN_ENDPOINT(GET_TOKEN),
    SUPER_ADMIN_ENDPOINTS(SUPER_ADMIN),
    ADMIN_ENDPOINTS(ADMIN),
    LOGGED_USER_ENDPOINTS(USER),
    COMMON_ENDPOINTS(APP),
    ALL_ENDPOINTS(ALL);

    private final String value;

    Endpoint(String value) {
        this.value = value;
    }

}
