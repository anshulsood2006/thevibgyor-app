package com.arsoft.projects.thevibgyor.backend.service;

import com.arsoft.projects.thevibgyor.backend.model.Role;
import com.arsoft.projects.thevibgyor.backend.model.app.Menu;
import com.arsoft.projects.thevibgyor.backend.repository.HeaderMenuRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class HeaderMenuService {
    @Autowired
    private HeaderMenuRepository headerMenuRepository;

    public List<Menu> getHeadersByRoleId(List<Role> roleIds) {
        return headerMenuRepository.findByRolesContaining(roleIds);
    }
}
