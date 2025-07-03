package com.DatLeo.BookShop.controller;

import com.DatLeo.BookShop.entity.User;
import com.DatLeo.BookShop.service.UserService;
import com.DatLeo.BookShop.util.annotation.CustomAnnotation;
import com.DatLeo.BookShop.util.constant.ApiConstants;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
