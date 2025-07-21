package com.DatLeo.BookShop.service;

import com.DatLeo.BookShop.dto.response.ResPaginationDTO;
import com.DatLeo.BookShop.dto.response.ResUserDTO;
import com.DatLeo.BookShop.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface UserService {

    User handleCreateUser(User user);

    User handleGetUserById(Integer id);

    User handleUpdateUser(User user);

    void handleDeleteUser(Integer id);

    boolean handleCheckEmailExisted(String email);

    ResUserDTO convertToResUserDTO(User user);

    ResPaginationDTO handleGetUsers(Specification<User> spec, Pageable pageable);
}
