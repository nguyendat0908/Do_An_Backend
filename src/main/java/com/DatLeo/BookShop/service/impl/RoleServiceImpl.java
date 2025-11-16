package com.DatLeo.BookShop.service.impl;

import com.DatLeo.BookShop.entity.Role;
import com.DatLeo.BookShop.exception.ApiException;
import com.DatLeo.BookShop.exception.ApiMessage;
import com.DatLeo.BookShop.repository.RoleRepository;
import com.DatLeo.BookShop.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public Role handleCreateRole(Role role) {
        boolean isCheckNameExisted = roleRepository.existsByName(role.getName());
        if (isCheckNameExisted) {
            throw new ApiException(ApiMessage.ROLE_NAME_EXISTED);
        }
        return roleRepository.save(role);
    }

    @Override
    public Role handleUpdateRole(Role role) {
        boolean isCheckNameExisted = roleRepository.existsByName(role.getName());
        if (isCheckNameExisted) {
            throw new ApiException(ApiMessage.ROLE_NAME_EXISTED);
        }

        Role currentRole = roleRepository.findById(role.getId())
                .orElseThrow(() -> new ApiException(ApiMessage.ROLE_NOT_EXIST));
        currentRole.setName(role.getName());
        currentRole.setDescription(role.getDescription());
        return roleRepository.save(currentRole);
    }

    @Override
    public void handleDeleteRole(Integer roleId) {
        Role currentRole = roleRepository.findById(roleId)
                .orElseThrow(() -> new ApiException(ApiMessage.ROLE_NOT_EXIST));

        roleRepository.deleteById(roleId);
    }

    @Override
    public Role handleGetRoleById(Integer roleId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ApiException(ApiMessage.ROLE_NOT_EXIST));

        return role;
    }

    @Override
    public List<Role> handleGetAllRoles() {
        return roleRepository.findAll();
    }
}
