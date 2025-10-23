package com.DatLeo.BookShop.controller;

import com.DatLeo.BookShop.entity.Role;
import com.DatLeo.BookShop.service.RoleService;
import com.DatLeo.BookShop.util.annotation.CustomAnnotation;
import com.DatLeo.BookShop.util.constant.ApiConstants;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiConstants.API_MAPPING_PREFIX)
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @PostMapping("/roles")
    @CustomAnnotation("Tạo vai trò thành công.")
    public ResponseEntity<?> createRole(@Valid @RequestBody Role role) {
        return ResponseEntity.ok(roleService.handleCreateRole(role));
    }

    @PutMapping("/roles")
    @CustomAnnotation("Cập nhật vai trò thành công.")
    public ResponseEntity<?> updateRole(@Valid @RequestBody Role role) {
        return ResponseEntity.ok(roleService.handleUpdateRole(role));
    }

    @GetMapping("/roles/{roleId}")
    @CustomAnnotation("Hiển thị thông tin chi tiết vai trò.")
    public ResponseEntity<?> getRole(@PathVariable("roleId") Integer roleId) {
        return ResponseEntity.ok(roleService.handleGetRoleById(roleId));
    }

    @GetMapping("/admins/roles")
    @CustomAnnotation("Hiển thị danh sách vai trò.")
    public ResponseEntity<?> getRoles() {
        return ResponseEntity.ok(roleService.handleGetAllRoles());
    }

    @DeleteMapping("/roles/{roleId}")
    @CustomAnnotation("Xóa vai trò thành công.")
    public ResponseEntity<Void> deleteRole(@PathVariable("roleId") Integer roleId) {
        roleService.handleDeleteRole(roleId);
        return ResponseEntity.ok(null);
    }
}
