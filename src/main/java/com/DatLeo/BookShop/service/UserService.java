package com.DatLeo.BookShop.service;

import com.DatLeo.BookShop.dto.request.ReqCreateUserDTO;
import com.DatLeo.BookShop.dto.response.ResPaginationDTO;
import com.DatLeo.BookShop.dto.response.ResUploadDTO;
import com.DatLeo.BookShop.dto.response.ResUserDTO;
import com.DatLeo.BookShop.entity.User;
import com.DatLeo.BookShop.exception.StorageException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UserService {

    ResUserDTO handleCreateUser(ReqCreateUserDTO reqCreateUserDTO);

    ResUserDTO handleGetUserById(Integer id);

    ResUserDTO handleUpdateUser(User user);

    void handleDeleteUser(Integer id);

    boolean handleCheckEmailExisted(String email);

    ResUserDTO convertToResUserDTO(User user);

    ResPaginationDTO handleGetUsers(Specification<User> spec, Pageable pageable);

    ResUploadDTO uploadAvatar(MultipartFile imageUrl);

    void handleUpdateUserAddRefreshToken(String email, String refreshToken);

    User handleGetUserByUsername(String email);

    User handleGetUserByEmailAndActive(String email);

    User handleGetUserByRefreshTokenAndEmail(String refreshToken, String email);

    boolean handleCheckExistByEmail(String email);
}
