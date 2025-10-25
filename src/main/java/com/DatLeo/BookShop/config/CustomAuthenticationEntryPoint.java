package com.DatLeo.BookShop.config;

import com.DatLeo.BookShop.dto.response.ResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final AuthenticationEntryPoint delegate = new BearerTokenAuthenticationEntryPoint();
    private final ObjectMapper objectMapper;

    public CustomAuthenticationEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        delegate.commence(request, response, authException);

        // Ghi log lỗi rõ ràng
        log.error("Authentication error: {}", authException.getMessage(), authException);

        // Chuẩn bị dữ liệu trả về JSON
        ResponseDTO<Object> res = new ResponseDTO<>();
        res.setCode(HttpStatus.UNAUTHORIZED.value());
        res.setMessage("Token không hợp lệ hoặc đã hết hạn. Vui lòng đăng nhập lại.");
        res.setError(
                authException.getCause() != null
                        ? authException.getCause().getMessage()
                        : authException.getMessage()
        );

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json;charset=UTF-8");
        objectMapper.writeValue(response.getWriter(), res);
    }
}