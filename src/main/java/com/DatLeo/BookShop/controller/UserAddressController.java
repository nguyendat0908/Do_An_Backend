package com.DatLeo.BookShop.controller;

import com.DatLeo.BookShop.dto.request.ReqUserAddress;
import com.DatLeo.BookShop.service.impl.UserAddressServiceImpl;
import com.DatLeo.BookShop.util.annotation.CustomAnnotation;
import com.DatLeo.BookShop.util.constant.ApiConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiConstants.API_MAPPING_PREFIX)
@RequiredArgsConstructor
public class UserAddressController {

    private final UserAddressServiceImpl userAddressServiceImpl;

    @PostMapping("/user-address")
    @CustomAnnotation("Thêm mới địa chỉ thành công.")
    public ResponseEntity<?> createUserAddress(@RequestBody ReqUserAddress reqUserAddress) {
        return ResponseEntity.ok(userAddressServiceImpl.handleCreateAddress(reqUserAddress));
    }

    @PutMapping("/user-address")
    @CustomAnnotation("Cập nhật địa chỉ thành công.")
    public ResponseEntity<?> updateUserAddress(@RequestBody ReqUserAddress reqUserAddress) {
        return ResponseEntity.ok(userAddressServiceImpl.handleUpdateAddress(reqUserAddress));
    }

    @GetMapping("/user-address")
    @CustomAnnotation("Danh sách địa chỉ người dùng.")
    public ResponseEntity<?> getUserAddress() {
        return ResponseEntity.ok(userAddressServiceImpl.handleGetAddresses());
    }

    @DeleteMapping("/user-address/{id}")
    @CustomAnnotation("Xóa địa chỉ thành công.")
    public ResponseEntity<Void> deleteUserAddress(@PathVariable Integer id) {
        userAddressServiceImpl.handleDeleteAddress(id);
        return ResponseEntity.ok(null);
    }
}
