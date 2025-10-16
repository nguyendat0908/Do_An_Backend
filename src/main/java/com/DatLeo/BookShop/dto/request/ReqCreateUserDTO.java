package com.DatLeo.BookShop.dto.request;

import com.DatLeo.BookShop.entity.Role;
import com.DatLeo.BookShop.exception.ApiMessage;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;


@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReqCreateUserDTO {

    @NotBlank(message = ApiMessage.NAME_NOT_NULL)
    String name;

    @NotBlank(message = ApiMessage.EMAIL_NOT_NULL)
    @Email(message = ApiMessage.EMAIL_NOT_CORRECT_FORMAT)
    String email;

    @NotBlank(message = ApiMessage.PASSWORD_NOT_NULL)
    @Size(min = 8, message = ApiMessage.PASSWORD_MUST_BE_GREATER)
    String password;

    String address;

    @Pattern(regexp = "^\\d{10}$", message = ApiMessage.PHONE_NUMBER_FORMAT)
    String phone;

    String imageUrl;

    Boolean active;

    Integer roleId;
}
