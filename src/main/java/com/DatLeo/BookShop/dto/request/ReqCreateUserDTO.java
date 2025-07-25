package com.DatLeo.BookShop.dto.request;

import com.DatLeo.BookShop.entity.Role;
import com.DatLeo.BookShop.exception.ApiMessage;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;

@Getter
@Setter
public class ReqCreateUserDTO {

    @NotBlank(message = ApiMessage.NAME_NOT_NULL)
    String name;

    @NotBlank(message = "Email không được để trống!")
    @Email(message = "Email không đúng định dạng!")
    String email;

    @NotBlank(message = "Mật khẩu không được để trống!")
    @Size(min = 8, message = "Mật khẩu phải lớn hơn 8 ký tự!")
    String password;

    String address;
    String phone;
    MultipartFile avatar;
    Boolean active;
    Role role;
}
