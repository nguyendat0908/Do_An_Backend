package com.DatLeo.BookShop.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseDTO <T>{

    Integer code;
    String error;

    Object message;
    T data;

    public ResponseDTO(T data) {
        this.data = data;
    }
}
