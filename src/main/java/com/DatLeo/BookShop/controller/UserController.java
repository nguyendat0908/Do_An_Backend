package com.DatLeo.BookShop.controller;

import com.DatLeo.BookShop.dto.request.ReqCreateUserDTO;
import com.DatLeo.BookShop.dto.response.ResPaginationDTO;
import com.DatLeo.BookShop.dto.response.ResUserDTO;
import com.DatLeo.BookShop.entity.Role;
import com.DatLeo.BookShop.entity.User;
import com.DatLeo.BookShop.exception.FieldException;
import com.DatLeo.BookShop.service.UserService;
import com.DatLeo.BookShop.util.annotation.CustomAnnotation;
import com.DatLeo.BookShop.util.constant.ApiConstants;
import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping(ApiConstants.API_MAPPING_PREFIX)
public class UserController {

    private final UserService userService;

    private UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    @CustomAnnotation("Thêm mới người dùng thành công!")
    public ResponseEntity<ResUserDTO> createUser (@Valid @ModelAttribute ReqCreateUserDTO reqCreateUserDTO, BindingResult bindingResult) throws IOException {

        if (bindingResult.hasErrors()) {
            throw new FieldException(bindingResult);
        }
        User newUser = this.userService.handleCreateUser(reqCreateUserDTO);
        return ResponseEntity.ok(this.userService.convertToResUserDTO(newUser));
    }

    @GetMapping("/users/{id}")
    @CustomAnnotation("Thông tin chi tiết người dùng!")
    public ResponseEntity<ResUserDTO> getUserById(@PathVariable("id") Integer id) {
        User user = this.userService.handleGetUserById(id);
        return ResponseEntity.ok(this.userService.convertToResUserDTO(user));
    }

    @GetMapping("/users")
    @CustomAnnotation("Hiển thị danh sách thông tin người dùng!")
    public ResponseEntity<ResPaginationDTO> getListUsers(@Filter Specification<User> spec, Pageable pageable) {
        return ResponseEntity.ok(this.userService.handleGetUsers(spec, pageable));
    }

    @PutMapping("/users")
    @CustomAnnotation("Cập nhật thông tin người dùng thành công!")
    public ResponseEntity<ResUserDTO> updateUser(@RequestBody User user) {
        User newUser = this.userService.handleUpdateUser(user);
        return ResponseEntity.ok(this.userService.convertToResUserDTO(newUser));
    }

    @DeleteMapping("/users/{id}")
    @CustomAnnotation("Xóa người dùng thành công!")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Integer id) {
        this.userService.handleDeleteUser(id);
        return ResponseEntity.ok(null);
    }
}
