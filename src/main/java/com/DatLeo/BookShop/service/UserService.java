package com.DatLeo.BookShop.service;

import com.DatLeo.BookShop.entity.User;

public interface UserService {

    User handleCreateUser(User user);

    User handleGetUserById(Integer id);

    User handleUpdateUser(User user);

    void handleDeleteUser(Integer id);
}
