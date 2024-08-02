package com.arsoft.projects.thevibgyor.backend.service;

import com.arsoft.projects.thevibgyor.backend.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    List<User> getUsers();

    User getUser(String username);

    User createUser(User user);
}
