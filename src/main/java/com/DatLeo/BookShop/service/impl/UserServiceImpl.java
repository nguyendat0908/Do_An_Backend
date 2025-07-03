package com.DatLeo.BookShop.service.impl;

import com.DatLeo.BookShop.entity.User;
import com.DatLeo.BookShop.repository.UserRepository;
import com.DatLeo.BookShop.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(User user) {
        log.info("Lưu người dùng thành công {}{}", user);
        return this.userRepository.save(user);
    }
}
