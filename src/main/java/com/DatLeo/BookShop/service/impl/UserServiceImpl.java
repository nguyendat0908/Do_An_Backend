package com.DatLeo.BookShop.service.impl;

import com.DatLeo.BookShop.dto.request.ReqCreateUserDTO;
import com.DatLeo.BookShop.dto.response.ResPaginationDTO;
import com.DatLeo.BookShop.dto.response.ResUploadDTO;
import com.DatLeo.BookShop.dto.response.ResUserDTO;
import com.DatLeo.BookShop.entity.Role;
import com.DatLeo.BookShop.entity.User;
import com.DatLeo.BookShop.exception.ApiException;
import com.DatLeo.BookShop.exception.ApiMessage;
import com.DatLeo.BookShop.repository.RoleRepository;
import com.DatLeo.BookShop.repository.UserRepository;
import com.DatLeo.BookShop.service.MinioService;
import com.DatLeo.BookShop.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    private final MinioService minioService;
    private final RoleRepository roleRepository;

    @Value("${minio.bucket-avatar}")
    private String bucketAvatar;

    @Value("${minio.max-file-size-bytes}")
    private Long maxFileSizeBytes;

    @Value("#{'${minio.allowed-image-mimetypes}'.split(',')}")
    private List<String> allowedImageMimetypes;

    @Override
    public ResUserDTO handleCreateUser(ReqCreateUserDTO reqCreateUserDTO) {
        boolean isCheckEmail = this.handleCheckEmailExisted(reqCreateUserDTO.getEmail());
        if (isCheckEmail){
            log.error("Không lưu người dùng thành công! {}", ApiMessage.EMAIL_EXISTED);
            throw new ApiException(ApiMessage.EMAIL_EXISTED);
        }
        String hashPassword = passwordEncoder.encode(reqCreateUserDTO.getPassword());
        reqCreateUserDTO.setPassword(hashPassword);

        User user = buildUser(reqCreateUserDTO);

        log.info("Lưu người dùng thành công!");
        return convertToResUserDTO(user);
    }

    private User buildUser(ReqCreateUserDTO reqCreateUserDTO) {

        Role role = roleRepository.findById(reqCreateUserDTO.getRoleId())
                .orElseThrow(() -> new ApiException("Role không tồn tại!"));

        User user = new User();
        user.setName(reqCreateUserDTO.getName());
        user.setEmail(reqCreateUserDTO.getEmail());
        user.setPassword(reqCreateUserDTO.getPassword());
        user.setAddress(reqCreateUserDTO.getAddress());
        user.setPhone(reqCreateUserDTO.getPhone());
        user.setImageUrl(reqCreateUserDTO.getImageUrl());
        user.setActive(reqCreateUserDTO.getActive() != null ? reqCreateUserDTO.getActive() : false);
        user.setRole(role);

        return userRepository.save(user);
    }

    @Override
    public ResUserDTO handleGetUserById(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ApiException(ApiMessage.ID_USER_NOT_EXIST));
        return convertToResUserDTO(user);
    }

    @Override
    public ResUserDTO handleUpdateUser(User user) {
        User currentUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new ApiException(ApiMessage.ID_USER_NOT_EXIST));
        if (currentUser != null) {
            currentUser.setActive(user.getActive());
        }
        this.userRepository.save(currentUser);
        log.info("Cập nhật thông tin người dùng thành công {}", currentUser);
        return convertToResUserDTO(currentUser);
    }

    @Override
    public void handleDeleteUser(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ApiException(ApiMessage.ID_USER_NOT_EXIST));
        if (user == null) {
            log.error("Người dùng với không tồn tại!");
            throw new ApiException(ApiMessage.ID_USER_NOT_EXIST);
        }

        if (user.getImageUrl() != null && !user.getImageUrl().isBlank()){
            try {
                minioService.deleteFromMinio(bucketAvatar, user.getImageUrl());
            } catch (Exception e) {
                log.error("Lỗi khi tạo presigned URL cho avatar user {}", user.getId(), e);
            }
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

        String imageUrl = null;

        if (user.getImageUrl() != null && !user.getImageUrl().isBlank()){
            try {
                imageUrl = minioService.getUrlFromMinio(bucketAvatar, user.getImageUrl());

            } catch (Exception e) {
                log.error("Lỗi khi tạo presigned URL cho avatar user {}", user.getId(), e);
            }
        }
        ResUserDTO resUserDTO = ResUserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .address(user.getAddress())
                .phone(user.getPhone())
                .imageUrl(imageUrl)
                .roleName(user.getRole().getName())
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

    @Override
    public ResUploadDTO uploadAvatar(MultipartFile imageUrl) {
        String mimeType = imageUrl.getContentType();
        long fileSize = imageUrl.getSize();
        String folderPath = "user-avatar";

        if (fileSize > maxFileSizeBytes) {
            throw new ApiException(ApiMessage.ERROR_FILE_SIZE);
        }
        if (!allowedImageMimetypes.contains(mimeType)) {
            throw new ApiException(ApiMessage.ERROR_FILE_MIMETYPE);
        }

        return minioService.uploadToMinio(imageUrl, bucketAvatar, folderPath);
    }

    @Override
    public void handleUpdateUserAddRefreshToken(String email, String refreshToken) {
        User user = this.userRepository.findByEmail(email);
        if (user != null) {
            user.setRefreshToken(refreshToken);
            this.userRepository.save(user);
        }
    }

    @Override
    public User handleGetUserByUsername(String email) {
        return this.userRepository.findByEmail(email);
    }

    @Override
    public User handleGetUserByRefreshTokenAndEmail(String refreshToken, String email) {
        return userRepository.findByRefreshTokenAndEmail(refreshToken, email);
    }

    @Override
    public boolean handleCheckExistByEmail(String email) {
        return this.userRepository.existsByEmail(email);
    }
}
