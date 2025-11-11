package com.DatLeo.BookShop.dto.request;

import com.DatLeo.BookShop.util.constant.GenderEnum;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReqUpdateInfoUser {

    Integer id;
    String imageUrl;
    String name;
    String address;
    GenderEnum gender;
    String phone;
}
