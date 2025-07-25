package com.DatLeo.BookShop.controller;

import com.DatLeo.BookShop.dto.request.ReqCreateUserDTO;
import com.DatLeo.BookShop.dto.response.ResPaginationDTO;
import com.DatLeo.BookShop.dto.response.ResUserDTO;
import com.DatLeo.BookShop.entity.Role;
import com.DatLeo.BookShop.entity.User;
import com.DatLeo.BookShop.service.UserService;
import com.DatLeo.BookShop.util.annotation.CustomAnnotation;
import com.DatLeo.BookShop.util.constant.ApiConstants;
import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ResUserDTO> createUser (@RequestParam("name") String name,
                                                  @RequestParam("email") String email,
                                                  @RequestParam("password") String password,
                                                  @RequestParam("active") boolean active,
                                                  @RequestParam(value = "address", required = false) String address,
                                                  @RequestParam(value = "phone", required = false) String phone,
                                                  @RequestParam(value = "avatar", required = false) MultipartFile avatar) throws IOException {
        ReqCreateUserDTO reqCreateUserDTO = new ReqCreateUserDTO();
        reqCreateUserDTO.setName(name);
        reqCreateUserDTO.setEmail(email);
        reqCreateUserDTO.setPassword(password);
        reqCreateUserDTO.setAddress(address);
        reqCreateUserDTO.setPhone(phone);
        reqCreateUserDTO.setActive(active);
        reqCreateUserDTO.setAvatar(avatar);

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
