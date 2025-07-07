package com.DatLeo.BookShop.service.impl;

import com.DatLeo.BookShop.entity.User;
import com.DatLeo.BookShop.exception.ApiException;
import com.DatLeo.BookShop.exception.ApiMessage;
import com.DatLeo.BookShop.repository.UserRepository;
import com.DatLeo.BookShop.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User handleCreateUser(User user) {
        log.info("Lưu người dùng thành công! {}", user);
        boolean isCheckEmail = this.handleCheckEmailExisted(user.getEmail());
        if (isCheckEmail){
            log.error("Không lưu người dùng thành công! {}", ApiMessage.EMAIL_EXISTED);
            throw new ApiException(ApiMessage.EMAIL_EXISTED);
        }
        return this.userRepository.save(user);
    }

    @Override
    public User handleGetUserById(Integer id) {
        Optional<User> user = this.userRepository.findById(id);
        if (user.isEmpty()) {
            log.error("Người dùng với ID {} không tồn tại!", id);
            throw new ApiException(ApiMessage.ID_USER_NOT_EXIST);
        }
        if (user.isPresent()) {
            log.info("Thông tin người dùng với ID {}", user);
        }
        return user.get();
    }

    @Override
    public User handleUpdateUser(User user) {
        User currentUser = this.handleGetUserById(user.getId());
        if (currentUser != null) {
            currentUser.setActive(user.getActive());
        }
        this.userRepository.save(currentUser);
        log.info("Cập nhật thông tin người dùng thành công {}", currentUser);
        return currentUser;
    }

    @Override
    public void handleDeleteUser(Integer id) {
        User user = this.handleGetUserById(id);
        if (user == null) {
            log.error("Người dùng với không tồn tại!");
            throw new ApiException(ApiMessage.ID_USER_NOT_EXIST);
        }
        this.userRepository.deleteById(id);
        log.info("Xóa người dùng thành công với ID {}", id);
    }

    public boolean handleCheckEmailExisted(String email) {
        return this.userRepository.existsByEmail(email);
    }
}
