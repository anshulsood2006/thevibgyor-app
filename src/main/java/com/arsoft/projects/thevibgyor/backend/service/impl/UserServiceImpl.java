package com.arsoft.projects.thevibgyor.backend.service.impl;

import com.arsoft.projects.thevibgyor.backend.model.CustomUserDetails;
import com.arsoft.projects.thevibgyor.backend.model.User;
import com.arsoft.projects.thevibgyor.backend.repository.UserRepository;
import com.arsoft.projects.thevibgyor.backend.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User getUser(String username) {
        return userRepository.getUser(username);
    }

    @Override
    public User createUser(User user) {
        return userRepository.createUser(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.getUser(username);
        return new CustomUserDetails(user);
    }

    @Override
    public List<User> getUsers() {
        return userRepository.getUsers();
    }
}
