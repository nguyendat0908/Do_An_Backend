package com.DatLeo.BookShop.dto.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ReqChangePasswordDTO {
    private String oldPassword;
    private String newPassword;
    private String confirmPassword;
}
