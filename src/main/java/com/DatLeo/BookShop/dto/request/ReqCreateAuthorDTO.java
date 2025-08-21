package com.DatLeo.BookShop.dto.request;

import com.DatLeo.BookShop.exception.ApiMessage;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;


@Setter
@Getter
public class ReqCreateAuthorDTO {

    @NotBlank(message = ApiMessage.NAME_NOT_NULL)
    String name;

    @Lob
    String description;

    String address;
    String type;

    @Nullable
    MultipartFile imageUrl;
}
