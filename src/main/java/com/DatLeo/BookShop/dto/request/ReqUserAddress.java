package com.DatLeo.BookShop.dto.request;

import com.DatLeo.BookShop.exception.ApiMessage;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReqUserAddress {

    Integer id;

    @NotBlank(message = ApiMessage.RECEIVER_NAME_NOT_NULL)
    String receiveName;

    @NotBlank(message = ApiMessage.RECEIVER_PHONE_NOT_NULL)
    @Pattern(regexp = "^\\d{9,15}$")
    String receivePhone;

    String province;

    String district;

    String ward;

    String detailAddress;

    Boolean isDefault = false;
}
