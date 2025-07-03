package com.DatLeo.BookShop.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseDTO <T>{

    private int code;
    private String error;

    private Object message;
    private T data;
}
