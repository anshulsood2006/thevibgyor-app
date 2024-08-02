package com.arsoft.projects.thevibgyor.backend.controller.admin;

import com.arsoft.projects.thevibgyor.common.constant.ApiUri;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(ApiUri.ADMIN)
public class AdminController {

    @PostMapping(value = "/page")
    public String adminPage() {
        log.info("Request has reached admin controller now");
        return "You are on admin page.";
    }
}
