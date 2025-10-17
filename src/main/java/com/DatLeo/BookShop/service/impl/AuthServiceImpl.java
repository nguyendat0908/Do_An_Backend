package com.DatLeo.BookShop.service.impl;

import com.DatLeo.BookShop.dto.OTPData;
import com.DatLeo.BookShop.dto.request.ReqChangePasswordDTO;
import com.DatLeo.BookShop.dto.request.ReqLoginDTO;
import com.DatLeo.BookShop.dto.request.ReqRegisterDTO;
import com.DatLeo.BookShop.dto.response.ResLoginDTO;
import com.DatLeo.BookShop.dto.response.ResRegisterDTO;
import com.DatLeo.BookShop.dto.response.ResUserDTO;
import com.DatLeo.BookShop.entity.Role;
import com.DatLeo.BookShop.entity.User;
import com.DatLeo.BookShop.exception.ApiException;
import com.DatLeo.BookShop.exception.ApiMessage;
import com.DatLeo.BookShop.repository.RoleRepository;
import com.DatLeo.BookShop.repository.UserRepository;
import com.DatLeo.BookShop.service.AuthService;
import com.DatLeo.BookShop.service.EmailService;
import com.DatLeo.BookShop.service.MinioService;
import com.DatLeo.BookShop.service.UserService;
import com.DatLeo.BookShop.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    @Value("${minio.bucket-avatar}")
    private String bucketAvatar;

    private static final String ROLE_USER = "USER";

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final SecurityUtil securityUtil;
    private final UserService  userService;
    private final EmailService  emailService;
    private final ConcurrentHashMap<String, OTPData> otpStorage = new ConcurrentHashMap<String, OTPData>();
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final MinioService minioService;
    private final RoleRepository roleRepository;

    Map<String, ReqRegisterDTO> tempNewUser = new ConcurrentHashMap<>();

    @Override
    public ResLoginDTO handleLogin(ReqLoginDTO reqLoginDTO) {

        // Chứa thông tin người dùng chưa xác thực
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                reqLoginDTO.getEmail(), reqLoginDTO.getPassword());

        // Xác thực người dùng bằng cấu hình lại method loadUserByUsername
        Authentication authentication = authenticationManagerBuilder.getObject()
                .authenticate(usernamePasswordAuthenticationToken);

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = customUserDetails.getUser();
        ResLoginDTO.UserLogin resLoginDTOUserLogin = new ResLoginDTO.UserLogin();
        resLoginDTOUserLogin.setEmail(user.getEmail());
        resLoginDTOUserLogin.setId(user.getId());
        resLoginDTOUserLogin.setName(user.getName());
        resLoginDTOUserLogin.setRole(user.getRole() != null ? user.getRole().getName() : "USER");

        String imageUrl = null;

        if (user.getImageUrl() != null && !user.getImageUrl().isBlank()){
            try {
                imageUrl = minioService.getUrlFromMinio(bucketAvatar, user.getImageUrl());

            } catch (Exception e) {
                log.error("Lỗi khi tạo presigned URL cho avatar user {}", user.getId(), e);
            }
        }

        resLoginDTOUserLogin.setImageUrl(imageUrl);

        if (!user.getActive()) {
            throw new ApiException(ApiMessage.USER_INACTIVE);
        }

        // Create token
        String accessToken = securityUtil.createAccessToken(reqLoginDTO.getEmail(), resLoginDTOUserLogin);

        // Create refresh token
        String refreshToken = securityUtil.createRefreshToken(reqLoginDTO.getEmail(), resLoginDTOUserLogin);

        // Update refresh token vào DB
        userService.handleUpdateUserAddRefreshToken(reqLoginDTO.getEmail(), refreshToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        ResLoginDTO resLoginDTO = new ResLoginDTO();
        resLoginDTO.setAccessToken(accessToken);
        resLoginDTO.setRefreshToken(refreshToken);
        resLoginDTO.setUserLogin(resLoginDTOUserLogin);

        return resLoginDTO;
    }

    @Override
    public ResLoginDTO.UserLogin handleGetAccount() {
        String email = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "";

        ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin();
        User currentUserDB = this.userService.handleGetUserByUsername(email);
        if (currentUserDB != null) {
            userLogin.setId(currentUserDB.getId());
            userLogin.setEmail(currentUserDB.getEmail());
            userLogin.setName(currentUserDB.getName());
            userLogin.setRole(currentUserDB.getRole() != null ? currentUserDB.getRole().toString() : "USER");
        }

        return userLogin;
    }

    @Override
    public ResLoginDTO getRefreshToken(String refreshToken) {
        if (refreshToken.equals("No refresh_token")) {
            throw new ApiException(ApiMessage.NOT_REFRESH_TOKEN_COOKIE);
        }

        // Kiểm tra refresh token
        Jwt decodedToken = securityUtil.checkValidRefreshToken(refreshToken);
        String email = decodedToken.getSubject();

        // Kiểm tra người dùng với email và token
        User currentUser = userService.handleGetUserByRefreshTokenAndEmail(refreshToken, email);
        if (currentUser == null) {
            throw new ApiException(ApiMessage.ERROR_REFRESH_TOKEN);
        }

        ResLoginDTO resLoginDTO = new ResLoginDTO();
        ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin();

        User currentUserDB = userService.handleGetUserByUsername(email);
        if (currentUserDB != null) {
            userLogin.setId(currentUserDB.getId());
            userLogin.setEmail(currentUserDB.getEmail());
            userLogin.setName(currentUserDB.getName());
            userLogin.setRole(currentUserDB.getRole() != null ? currentUserDB.getRole().toString() : "USER");

            resLoginDTO.setUserLogin(userLogin);
        }

        String newAccessToken = securityUtil.createAccessToken(email, userLogin);
        resLoginDTO.setAccessToken(newAccessToken);

        String newRefreshToken = this.securityUtil.createRefreshToken(email, userLogin);
        userService.handleUpdateUserAddRefreshToken(email, newRefreshToken);
        resLoginDTO.setRefreshToken(newRefreshToken);

        return resLoginDTO;
    }

    @Override
    public void handleLogout() {
        String email = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "";
        userService.handleUpdateUserAddRefreshToken(email, null);

        if (email.equals("")) {
            throw new ApiException(ApiMessage.ERROR_REFRESH_TOKEN);
        }
    }

    @Override
    public ResRegisterDTO handleRegister(ReqRegisterDTO reqRegisterDTO) {
        boolean isCheckEmail = userService.handleCheckExistByEmail(reqRegisterDTO.getEmail());
        if (isCheckEmail) {
            throw new ApiException(ApiMessage.EMAIL_EXISTED);
        }
        handleSaveTempUser(reqRegisterDTO);
        String otp = handleGenerateOTP(reqRegisterDTO.getEmail());
        emailService.sendEmailActiveAccount(reqRegisterDTO.getEmail(), "Kích hoạt tài khoản.", "email", otp);
        ResRegisterDTO resRegisterDTO = new ResRegisterDTO();
        resRegisterDTO.setMessage("Làm ơn kiểm tra email của bạn để kích hoạt tài khoản.");

        return resRegisterDTO;
    }

    @Override
    public ResUserDTO activeAccountRegister(String username, String email, String code) {
        boolean isPass = handleVerifyOTP(email, code);

        Role role = roleRepository.findByName(ROLE_USER);

        if (isPass) {
            ReqRegisterDTO reqRegisterDTO = handleGetTempUser(email);
            String hashPassword = passwordEncoder.encode(reqRegisterDTO.getPassword());
            User user = new User();
            user.setEmail(email);
            user.setName(username);
            user.setPassword(hashPassword);
            user.setRole(role);
            user.setActive(true);
            userRepository.save(user);

            ResUserDTO res = userService.convertToResUserDTO(user);

            return res;
        } else {
            throw new ApiException(ApiMessage.ERROR_OTP);
        }
    }

    @Override
    public ResRegisterDTO handleForgotPassword(String email) {

        User user = userService.handleGetUserByEmailAndActive(email);
        if (user == null) {
            throw new ApiException(ApiMessage.ID_USER_NOT_EXIST);
        }

        String newPassword = this.handleGeneratePassword();
        emailService.sendEmailNewPassword(email, "Mật khẩu mới của bạn.", "password", newPassword);
        String hashPassword = passwordEncoder.encode(newPassword);
        user.setPassword(hashPassword);
        userService.handleUpdateUser(user);

        ResRegisterDTO resRegisterDTO = new ResRegisterDTO();
        resRegisterDTO.setMessage("Vui lòng kiểm tra email của bạn để nhận mật khẩu mới.");

        return resRegisterDTO;
    }

    @Override
    public ResRegisterDTO handleChangePassword(ReqChangePasswordDTO reqChangePasswordDTO) {
        Integer userId = securityUtil.getIdCurrentUserLogin();
        User user = userRepository.findById(userId).orElseThrow(() -> new ApiException(ApiMessage.ID_USER_NOT_EXIST));
        if (!passwordEncoder.matches(reqChangePasswordDTO.getOldPassword(), user.getPassword())) {
            throw new ApiException(ApiMessage.OLD_PASSWORD);
        }
        if (!reqChangePasswordDTO.getNewPassword().equals(reqChangePasswordDTO.getConfirmPassword())) {
            throw new ApiException(ApiMessage.ERROR_CHANGE_PASSWORD);
        }

        user.setPassword(passwordEncoder.encode(reqChangePasswordDTO.getNewPassword()));
        userRepository.save(user);
        ResRegisterDTO resRegisterDTO = new ResRegisterDTO();
        resRegisterDTO.setMessage("Đổi mật khẩu thành công.");

        return resRegisterDTO;
    }

    private String handleGeneratePassword() {
        String newPassword = UUID.randomUUID().toString().substring(0, 6);
        return newPassword;
    }

    public String handleGenerateOTP(String email) {
        String otp = String.format("%06d", new Random().nextInt(1000000));
        otpStorage.put(email, new OTPData(otp, 2));
        return otp;
    }

    public boolean handleVerifyOTP(String email, String inputOtp) {
        OTPData otpData = otpStorage.get(email);
        if (otpData == null)
            return false;
        if (otpData.isExpired()) {
            otpStorage.remove(email);
            return false;
        }
        boolean valid = otpData.getCode().equals(inputOtp);
        if (valid)
            otpStorage.remove(email);
        return valid;
    }

    private void handleSaveTempUser(ReqRegisterDTO reqRegisterDTO) {
        tempNewUser.put(reqRegisterDTO.getEmail(), reqRegisterDTO);
    }

    private ReqRegisterDTO handleGetTempUser(String email) {
        return tempNewUser.get(email);
    }
}
