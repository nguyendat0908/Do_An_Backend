package com.DatLeo.BookShop.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResUserAddress {
    Integer id;
    String receiveName;
    String receivePhone;
    String province;
    String district;
    String ward;
    String detailAddress;
    Boolean isDefault = false;
}
