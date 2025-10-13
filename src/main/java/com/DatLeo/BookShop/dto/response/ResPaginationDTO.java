package com.DatLeo.BookShop.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResPaginationDTO {

    Meta meta;
    Object result;

    @Setter
    @Getter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Meta {
        Integer page;
        Integer pageSize;
        Integer pages;
        Long total;
    }
}
