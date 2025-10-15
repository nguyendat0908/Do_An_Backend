package com.DatLeo.BookShop.config;

import com.DatLeo.BookShop.dto.response.ResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final AuthenticationEntryPoint authenticationEntryPoint = new BearerTokenAuthenticationEntryPoint();
    private final ObjectMapper objectMapper;

    public CustomAuthenticationEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        this.authenticationEntryPoint.commence(request, response, authException);
        response.setContentType("application/json;charset=UTF-8");
        ResponseDTO<Object> res = new ResponseDTO<>();
        res.setCode(HttpStatus.UNAUTHORIZED.value());
        res.setError(authException.getCause().getMessage());
        res.setMessage("Token không hợp lệ (đã hết hạn, định dạng không đúng hoặc không có trong header JWT!)");

        objectMapper.writeValue(response.getWriter(), res);
    }

}
