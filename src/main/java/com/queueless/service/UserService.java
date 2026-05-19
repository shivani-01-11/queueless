package com.queueless.service;

import com.queueless.entity.User;

import java.util.List;

public interface UserService {

    User saveUser(User user);

    List<User> getAllUsers();

    User getUserByEmail(String email);
}