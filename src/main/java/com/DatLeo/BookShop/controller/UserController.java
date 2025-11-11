package com.DatLeo.BookShop.controller;

import com.DatLeo.BookShop.dto.request.ReqCreateUserDTO;
import com.DatLeo.BookShop.dto.request.ReqUpdateInfoUser;
import com.DatLeo.BookShop.entity.User;
import com.DatLeo.BookShop.exception.FieldException;
import com.DatLeo.BookShop.exception.StorageException;
import com.DatLeo.BookShop.service.UserService;
import com.DatLeo.BookShop.util.annotation.CustomAnnotation;
import com.DatLeo.BookShop.util.constant.ApiConstants;
import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping(ApiConstants.API_MAPPING_PREFIX)
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/users")
    @CustomAnnotation("Thêm mới người dùng thành công.")
    public ResponseEntity<?> createUser (
            @Valid @ModelAttribute ReqCreateUserDTO reqCreateUserDTO, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new FieldException(bindingResult);
        }
        return ResponseEntity.ok(userService.handleCreateUser(reqCreateUserDTO));
    }

    @GetMapping("/users/{id}")
    @CustomAnnotation("Thông tin chi tiết người dùng.")
    public ResponseEntity<?> getUserById(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(userService.handleGetUserById(id));
    }

    @GetMapping("/admins/users")
    @CustomAnnotation("Hiển thị danh sách thông tin người dùng.")
    public ResponseEntity<?> getListUsers(@Filter Specification<User> spec, Pageable pageable) {
        return ResponseEntity.ok(userService.handleGetUsers(spec, pageable));
    }

    @PutMapping("/users")
    @CustomAnnotation("Cập nhật thông tin người dùng thành công.")
    public ResponseEntity<?> updateUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.handleUpdateUser(user));
    }

    @DeleteMapping("/users/{id}")
    @CustomAnnotation("Xóa người dùng thành công.")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Integer id) {
        this.userService.handleDeleteUser(id);
        return ResponseEntity.ok(null);
    }

    @PostMapping("/users/upload")
    @CustomAnnotation("Upload ảnh thành công.")
    public ResponseEntity<?> uploadAvatar(@RequestParam("imageUrl") MultipartFile imageUrl) {
        return  ResponseEntity.ok(userService.uploadAvatar(imageUrl));
    }

    @GetMapping("/users/info")
    @CustomAnnotation("Thông tin chi tiết người dùng.")
    public ResponseEntity<?> getCurrentUser() {
        return ResponseEntity.ok(userService.handleGetCurrentUser());
    }

    @PutMapping("/users/update-info")
    @CustomAnnotation("Chỉnh sửa thông tin cá nhân.")
    public ResponseEntity<?> updateUserInfo(@ModelAttribute ReqUpdateInfoUser reqUpdateInfoUser, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            throw new FieldException(bindingResult);
        }
        return ResponseEntity.ok(userService.handleUpdateInfoUser(reqUpdateInfoUser));
    }
}
