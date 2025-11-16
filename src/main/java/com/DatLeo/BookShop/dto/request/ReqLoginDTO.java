package com.DatLeo.BookShop.dto.request;

import com.DatLeo.BookShop.exception.ApiMessage;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReqLoginDTO {

    @NotBlank(message = ApiMessage.EMAIL_NOT_NULL)
    String email;

    @NotBlank(message = ApiMessage.PASSWORD_NOT_NULL)
    String password;
}
