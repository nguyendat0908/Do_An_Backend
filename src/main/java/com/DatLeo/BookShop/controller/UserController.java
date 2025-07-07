package com.DatLeo.BookShop.controller;

import com.DatLeo.BookShop.entity.User;
import com.DatLeo.BookShop.service.UserService;
import com.DatLeo.BookShop.util.annotation.CustomAnnotation;
import com.DatLeo.BookShop.util.constant.ApiConstants;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiConstants.API_MAPPING_PREFIX)
public class UserController {

    private final UserService userService;

    private UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    @CustomAnnotation("Thêm mới người dùng thành công!")
    public ResponseEntity<User> createUser (@RequestBody @Valid User user) {
        return ResponseEntity.ok(this.userService.handleCreateUser(user));
    }

    @GetMapping("/users/{id}")
    @CustomAnnotation("Thông tin chi tiết người dùng!")
    public ResponseEntity<User> getUserById(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(this.userService.handleGetUserById(id));
    }

    @PutMapping("/users")
    @CustomAnnotation("Cập nhật thông tin người dùng thành công!")
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        return ResponseEntity.ok(this.userService.handleUpdateUser(user));
    }

    @DeleteMapping("/users/{id}")
    @CustomAnnotation("Xóa người dùng thành công!")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Integer id) {
        this.userService.handleDeleteUser(id);
        return ResponseEntity.ok(null);
    }
}
