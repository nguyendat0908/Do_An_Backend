package com.DatLeo.BookShop.controller;

import com.DatLeo.BookShop.dto.request.ReqLoginDTO;
import com.DatLeo.BookShop.service.AuthService;
import com.DatLeo.BookShop.util.constant.ApiConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiConstants.API_MAPPING_PREFIX)
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody ReqLoginDTO reqLoginDTO) {
        return ResponseEntity.ok(authService.handleLogin(reqLoginDTO));
    }
}
