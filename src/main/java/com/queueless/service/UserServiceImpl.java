package com.queueless.service;

import com.queueless.entity.User;
import com.queueless.repository.UserRepository;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    // constructor injection (dependency injection)
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User saveUser(User user) {
        // Hibernate converts Java object into SQL insert.
        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserByEmail(String email) {

        return userRepository.findByEmail(email);
    }
}