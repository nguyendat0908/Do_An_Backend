package com.DatLeo.BookShop.service.impl;

import com.DatLeo.BookShop.entity.User;
import com.DatLeo.BookShop.repository.UserRepository;
import com.DatLeo.BookShop.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(User user) {
        return this.userRepository.save(user);
    }
}
