package com.DatLeo.BookShop.service.impl;

import com.DatLeo.BookShop.dto.response.ResPaginationDTO;
import com.DatLeo.BookShop.dto.response.ResUserDTO;
import com.DatLeo.BookShop.entity.User;
import com.DatLeo.BookShop.exception.ApiException;
import com.DatLeo.BookShop.exception.ApiMessage;
import com.DatLeo.BookShop.repository.UserRepository;
import com.DatLeo.BookShop.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User handleCreateUser(User user) {
        log.info("Lưu người dùng thành công!");
        boolean isCheckEmail = this.handleCheckEmailExisted(user.getEmail());
        if (isCheckEmail){
            log.error("Không lưu người dùng thành công! {}", ApiMessage.EMAIL_EXISTED);
            throw new ApiException(ApiMessage.EMAIL_EXISTED);
        }
        String hashPassword = this.passwordEncoder.encode(user.getPassword());
        user.setPassword(hashPassword);
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
            log.info("Thông tin người dùng với ID {}", user);
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
        ResUserDTO resUserDTO = new ResUserDTO();

        resUserDTO.setId(user.getId());
        resUserDTO.setName(user.getName());
        resUserDTO.setEmail(user.getEmail());
        resUserDTO.setAddress(user.getAddress());
        resUserDTO.setPhone(user.getPhone());
        resUserDTO.setAvatar(user.getAvatar());
        resUserDTO.setSsoID(user.getSsoID());
        resUserDTO.setGender(user.getGender());
        resUserDTO.setSsoType(user.getSsoType());
        resUserDTO.setActive(user.getActive());
        resUserDTO.setRefreshToken(user.getRefreshToken());
        resUserDTO.setCreatedAt(user.getCreatedAt());
        resUserDTO.setUpdatedAt(user.getUpdatedAt());

        return resUserDTO;
    }

    @Override
    public ResPaginationDTO handleGetUsers(Specification<User> spec, Pageable pageable) {

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
        log.info("Hiển thị danh sách người dùng phân trang thành công!");

        return resPaginationDTO;
    }
}
