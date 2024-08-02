package com.arsoft.projects.thevibgyor.backend.repository;

import com.arsoft.projects.thevibgyor.backend.model.Role;
import com.arsoft.projects.thevibgyor.backend.model.app.Menu;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HeaderMenuRepository {
    List<Menu> findByRolesContaining(List<Role> roleIds);
}
