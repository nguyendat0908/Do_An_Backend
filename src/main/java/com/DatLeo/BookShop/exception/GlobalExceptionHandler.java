package com.DatLeo.BookShop.exception;

import com.DatLeo.BookShop.dto.response.ResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;
import java.util.stream.Collectors;

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
        res.setError("Xảy ra ngoại lệ ...");
        res.setMessage(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ResponseDTO<Object>> handleBadCredentials(BadCredentialsException ex) {
        ResponseDTO<Object> res = new ResponseDTO<Object>();
        res.setCode(HttpStatus.BAD_REQUEST.value());
        res.setError(ex.getMessage());
        res.setMessage("Tên người dùng hoặc mật khẩu không đúng.");
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

    // Handle validation exception
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseDTO<Object>> validationError(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        final List<FieldError> fieldErrors = result.getFieldErrors();

        ResponseDTO<Object> res = new ResponseDTO<Object>();
        res.setCode(HttpStatus.BAD_REQUEST.value());
        res.setError(ex.getBody().getDetail());

        List<String> errors = fieldErrors.stream().map(f -> f.getDefaultMessage()).collect(Collectors.toList());
        res.setMessage(errors.size() > 1 ? errors : errors.get(0));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }

    @ExceptionHandler(FieldException.class)
    public ResponseEntity<ResponseDTO<Object>> handleCustomValidation(FieldException ex) {
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();

        List<String> errors = fieldErrors.stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());

        ResponseDTO<Object> res = new ResponseDTO<>();
        res.setCode(HttpStatus.BAD_REQUEST.value());
        res.setError("Validation failed");

        if (errors.size() == 1) {
            res.setMessage(errors.get(0));
        } else {
            res.setMessage(errors);
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }


}
