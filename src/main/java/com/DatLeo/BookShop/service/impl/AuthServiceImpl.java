package com.DatLeo.BookShop.service.impl;

import com.DatLeo.BookShop.dto.request.ReqLoginDTO;
import com.DatLeo.BookShop.dto.request.ReqRegisterDTO;
import com.DatLeo.BookShop.dto.response.ResLoginDTO;
import com.DatLeo.BookShop.dto.response.ResRegisterDTO;
import com.DatLeo.BookShop.dto.response.ResUserDTO;
import com.DatLeo.BookShop.entity.User;
import com.DatLeo.BookShop.service.AuthService;
import com.DatLeo.BookShop.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final SecurityUtil securityUtil;

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
        resLoginDTOUserLogin.setRole(user.getRole() != null ? user.getRole().toString() : "USER");

        // Create token
        String accessToken = securityUtil.createAccessToken(reqLoginDTO.getEmail(), resLoginDTOUserLogin);

        ResLoginDTO resLoginDTO = new ResLoginDTO();
        resLoginDTO.setAccessToken(accessToken);

        return resLoginDTO;
    }

    @Override
    public ResLoginDTO.UserLogin handleGetAccount() {
        return null;
    }

    @Override
    public ResLoginDTO getRefreshToken(String refreshToken) {
        return null;
    }

    @Override
    public ResLoginDTO handleLogout() {
        return null;
    }

    @Override
    public ResRegisterDTO handleRegister(ReqRegisterDTO reqRegisterDTO) {
        return null;
    }

    @Override
    public ResUserDTO activeAccountRegister(String email, String code) {
        return null;
    }

    @Override
    public ResRegisterDTO handleResetPassword(String email) {
        return null;
    }
}
