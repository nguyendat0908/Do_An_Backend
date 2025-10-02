package com.DatLeo.BookShop.service.impl;

import com.DatLeo.BookShop.dto.request.ReqCreateUserDTO;
import com.DatLeo.BookShop.dto.response.ResPaginationDTO;
import com.DatLeo.BookShop.dto.response.ResUploadDTO;
import com.DatLeo.BookShop.dto.response.ResUserDTO;
import com.DatLeo.BookShop.entity.User;
import com.DatLeo.BookShop.exception.ApiException;
import com.DatLeo.BookShop.exception.ApiMessage;
import com.DatLeo.BookShop.exception.StorageException;
import com.DatLeo.BookShop.repository.UserRepository;
import com.DatLeo.BookShop.service.FileService;
import com.DatLeo.BookShop.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final FileService fileService;
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, FileService fileService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.fileService = fileService;
    }

    @Override
    public User handleCreateUser(ReqCreateUserDTO reqCreateUserDTO) throws IOException, StorageException {
        log.info("Lưu người dùng thành công!");
        boolean isCheckEmail = this.handleCheckEmailExisted(reqCreateUserDTO.getEmail());
        if (isCheckEmail){
            log.error("Không lưu người dùng thành công! {}", ApiMessage.EMAIL_EXISTED);
            throw new ApiException(ApiMessage.EMAIL_EXISTED);
        }
        String hashPassword = this.passwordEncoder.encode(reqCreateUserDTO.getPassword());
        reqCreateUserDTO.setPassword(hashPassword);

        User user = new User();
        user.setName(reqCreateUserDTO.getName());
        user.setEmail(reqCreateUserDTO.getEmail());
        user.setPassword(reqCreateUserDTO.getPassword());
        user.setAddress(reqCreateUserDTO.getAddress());
        user.setPhone(reqCreateUserDTO.getPhone());
        user.setActive(reqCreateUserDTO.getActive() != null ? reqCreateUserDTO.getActive() : false);


        if (reqCreateUserDTO.getImageUrl() != null && !reqCreateUserDTO.getImageUrl().isEmpty()) {
            ResUploadDTO resUploadDTO = this.fileService.uploadImage(reqCreateUserDTO.getImageUrl());
            user.setImageUrl(resUploadDTO.getUrl());
            user.setImagePublicId(resUploadDTO.getPublicId());
        }

        return this.userRepository.save(user);
    }

    @Override
    public User handleGetUserById(Integer id) {
        Optional<User> user = this.userRepository.findById(id);
        if (user.isEmpty()) {
            log.error("Người dùng với ID {} không tồn tại!", id);
            throw new ApiException(ApiMessage.ID_USER_NOT_EXIST);
        }
        if (user.isPresent()) {
            log.info("Thông tin người dùng với ID {}", user.get());
        }
        return user.get();
    }

    @Override
    public User handleUpdateUser(User user) {
        User currentUser = this.handleGetUserById(user.getId());
        if (currentUser != null) {
            currentUser.setActive(user.getActive());
        }
        this.userRepository.save(currentUser);
        log.info("Cập nhật thông tin người dùng thành công {}", currentUser);
        return currentUser;
    }

    @Override
    public void handleDeleteUser(Integer id) {
        User user = this.handleGetUserById(id);
        if (user == null) {
            log.error("Người dùng với không tồn tại!");
            throw new ApiException(ApiMessage.ID_USER_NOT_EXIST);
        }

        if (user.getImagePublicId() != null && !user.getImagePublicId().isEmpty()) {
            fileService.deleteImage(user.getImagePublicId());
        }

        this.userRepository.deleteById(id);
        log.info("Xóa người dùng thành công với ID {}", id);
    }

    @Override
    public boolean handleCheckEmailExisted(String email) {
        return this.userRepository.existsByEmail(email);
    }

    // Convert User to ResUserDTO
    @Override
    public ResUserDTO convertToResUserDTO(User user) {
        ResUserDTO resUserDTO = ResUserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .address(user.getAddress())
                .phone(user.getPhone())
                .imageUrl(user.getImageUrl())
                .imagePublicId(user.getImagePublicId())
                .ssoID(user.getSsoID())
                .gender(user.getGender())
                .ssoType(user.getSsoType())
                .active(user.getActive())
                .refreshToken(user.getRefreshToken())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();

        return resUserDTO;
    }

    @Override
    public ResPaginationDTO handleGetUsers(Specification<User> spec, Pageable pageable) {

        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<User> pageUser = this.userRepository.findAll(spec, pageable);
        ResPaginationDTO resPaginationDTO = new ResPaginationDTO();
        ResPaginationDTO.Meta meta = new ResPaginationDTO.Meta();

        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(pageUser.getTotalPages());
        meta.setTotal(pageUser.getTotalElements());

        resPaginationDTO.setMeta(meta);

        List<ResUserDTO> listUserDTOs = pageUser.getContent().stream().map(item ->
                this.convertToResUserDTO(item)).collect(Collectors.toList());

        resPaginationDTO.setResult(listUserDTOs);

        return resPaginationDTO;
    }
}
