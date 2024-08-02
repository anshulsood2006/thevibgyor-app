package com.arsoft.projects.thevibgyor.backend.repository;

import com.arsoft.projects.thevibgyor.backend.model.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository {
    User getUser(String username);
    List<User> getUsers();
    User createUser(User user);
}
