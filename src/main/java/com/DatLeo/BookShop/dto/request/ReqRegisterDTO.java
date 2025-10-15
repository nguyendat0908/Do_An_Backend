package com.DatLeo.BookShop.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReqRegisterDTO {
    String username;
    String email;
    String password;
}
