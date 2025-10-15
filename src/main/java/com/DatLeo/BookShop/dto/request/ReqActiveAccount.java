package com.DatLeo.BookShop.dto.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ReqActiveAccount {
    String username;
    String email;
    String code;
}
