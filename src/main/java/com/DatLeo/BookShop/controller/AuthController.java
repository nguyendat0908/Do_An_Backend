package com.DatLeo.BookShop.controller;

import com.DatLeo.BookShop.dto.request.ReqActiveAccount;
import com.DatLeo.BookShop.dto.request.ReqChangePasswordDTO;
import com.DatLeo.BookShop.dto.request.ReqLoginDTO;
import com.DatLeo.BookShop.dto.request.ReqRegisterDTO;
import com.DatLeo.BookShop.dto.response.ResLoginDTO;
import com.DatLeo.BookShop.service.AuthService;
import com.DatLeo.BookShop.util.annotation.CustomAnnotation;
import com.DatLeo.BookShop.util.constant.ApiConstants;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(ApiConstants.API_MAPPING_PREFIX)
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/auth/login")
    @CustomAnnotation("Đăng nhập thành công.")
    public ResponseEntity<?> login(@Valid @RequestBody ReqLoginDTO reqLoginDTO) {

        ResLoginDTO resLoginDTO = authService.handleLogin(reqLoginDTO);
        String refreshToken = resLoginDTO.getRefreshToken();

        ResponseCookie cookie = ResponseCookie
                .from("refresh_token", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(86400)
                .build();

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(resLoginDTO);
    }

    @PostMapping("/auth/login/google")
    @CustomAnnotation("Đăng nhập thành công.")
    public ResponseEntity<?> loginGoogle(@RequestBody Map<String, String> request) {
        return ResponseEntity.ok(authService.handleLoginWithGoogle(request));
    }

    // Get account when f5 website
    @GetMapping("/auth/account")
    @CustomAnnotation("Lấy thông tin tài khoản người dùng hiện tại thành công.")
    public ResponseEntity<ResLoginDTO.UserLogin> getAccount() {
        return ResponseEntity.ok(authService.handleGetAccount());
    }

    @GetMapping("/auth/refresh")
    @CustomAnnotation("Lấy lại access token thành công.")
    public ResponseEntity<?> getRefreshToken(
            @CookieValue(name = "refresh_token", defaultValue = "No refresh_token") String refreshToken) {

        ResLoginDTO resLoginDTO = authService.getRefreshToken(refreshToken);
        String newRefreshToken = resLoginDTO.getRefreshToken();

        ResponseCookie responseCookie = ResponseCookie.from("refresh_token", newRefreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(86400)
                .build();

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, responseCookie.toString()).body(resLoginDTO);
    }

    @PostMapping("/auth/logout")
    @CustomAnnotation("Đăng xuất thành công.")
    public ResponseEntity<Void> logout() {
        authService.handleLogout();
        ResponseCookie deleteCookie = ResponseCookie.from("refresh_token", null)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(0)
                .build();

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, deleteCookie.toString())
                .body(null);
    }

    @PostMapping("/auth/register")
    @CustomAnnotation("Mã OTP đã được gửi đến email của bạn thành công.")
    public ResponseEntity<?> register(@RequestBody ReqRegisterDTO reqRegisterDTO) {
        return ResponseEntity.ok(authService.handleRegister(reqRegisterDTO));
    }

    @PostMapping("/auth/active")
    @CustomAnnotation("Kích hoạt tài khoản thành công.")
    public ResponseEntity<?> activeAccountRegister(@RequestBody ReqActiveAccount reqActiveAccount) {
        return ResponseEntity.ok(authService.activeAccountRegister(reqActiveAccount.getUsername(), reqActiveAccount.getEmail(),  reqActiveAccount.getCode()));
    }

    @PostMapping("/auth/forgot-password")
    @CustomAnnotation("Mật khẩu mới đã được gửi đến email của bạn thành công.")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        return ResponseEntity.ok(authService.handleForgotPassword(email));
    }

    @PostMapping("/auth/change-password")
    @CustomAnnotation("Đổi mật khẩu thành công.")
    public ResponseEntity<?> changePassword(@RequestBody ReqChangePasswordDTO reqChangePasswordDTO) {
        return ResponseEntity.ok(authService.handleChangePassword(reqChangePasswordDTO));
    }

}
