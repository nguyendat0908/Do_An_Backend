package com.DatLeo.BookShop.dto.response;

import com.DatLeo.BookShop.util.constant.GenderEnum;
import com.DatLeo.BookShop.util.constant.SsoTypeEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResUserDTO {

    Integer id;
    String name;
    String email;
    String address;
    String phone;
    String imageUrl;
    String ssoID;
    GenderEnum gender;
    SsoTypeEnum ssoType;
    Boolean active;
    String refreshToken;
    Instant createdAt;
    Instant updatedAt;

}
