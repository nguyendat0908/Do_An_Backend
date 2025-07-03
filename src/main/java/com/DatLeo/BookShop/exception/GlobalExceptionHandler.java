package com.DatLeo.BookShop.exception;

import com.DatLeo.BookShop.dto.response.ResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Xử lý ngoại lệ chưa được định nghĩa
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDTO<Object>> handleAllException(Exception ex) {
        ResponseDTO<Object> res = new ResponseDTO<Object>();
        res.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        res.setError("Internal server error");
        res.setMessage(ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
    }

    @ExceptionHandler(value = { ApiException.class,
            UsernameNotFoundException.class, IllegalArgumentException.class })
    public ResponseEntity<ResponseDTO<Object>> handleIdException(Exception ex) {
        ResponseDTO<Object> res = new ResponseDTO<Object>();
        res.setCode(HttpStatus.BAD_REQUEST.value());
        res.setError(ex.getMessage());
        res.setMessage("Xảy ra ngoại lệ...");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }

    // Handle 404 exception
    @ExceptionHandler(value = {
            NoResourceFoundException.class,
    })
    public ResponseEntity<ResponseDTO<Object>> handleNotFoundException(Exception ex) {
        ResponseDTO<Object> res = new ResponseDTO<Object>();
        res.setCode(HttpStatus.NOT_FOUND.value());
        res.setError(ex.getMessage());
        res.setMessage("404 không tìm thấy. URL có thể không tồn tại...");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }

}
