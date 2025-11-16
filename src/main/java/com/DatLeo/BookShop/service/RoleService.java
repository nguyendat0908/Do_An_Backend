package com.DatLeo.BookShop.service;

import com.DatLeo.BookShop.entity.Role;

import java.util.List;

public interface RoleService {

    Role handleCreateRole(Role role);
    Role handleUpdateRole(Role role);
    void handleDeleteRole(Integer roleId);
    Role handleGetRoleById(Integer roleId);
    List<Role> handleGetAllRoles();
}
