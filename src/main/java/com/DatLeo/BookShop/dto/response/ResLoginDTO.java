package com.DatLeo.BookShop.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResLoginDTO {

    UserLogin userLogin;
    String accessToken;
    String refreshToken;

    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserLogin {
        private Integer id;
        private String email;
        private String name;
        private String role;
    }
}
