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
    Integer provinceId;
    String province;
    Integer districtId;
    String district;
    String wardCode;
    String ward;
    String detailAddress;
    Boolean isDefault = false;
}
