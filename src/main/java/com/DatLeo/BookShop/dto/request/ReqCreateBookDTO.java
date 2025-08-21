package com.DatLeo.BookShop.dto.request;

import com.DatLeo.BookShop.exception.ApiMessage;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.annotation.Nullable;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Setter
@Getter
public class ReqCreateBookDTO {

    @NotBlank(message = ApiMessage.NAME_NOT_NULL)
    String name;

    @Lob
    String description;

    @NotNull(message = ApiMessage.QUANTITY_NOT_NULL)
    Integer quantity;

    @NotNull(message = ApiMessage.PRICE_NOT_NULL)
    Double price;

    @JsonFormat(pattern = "dd-MM-yyyy")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    LocalDate publicationDate;

    Integer authorId;
    Integer categoryId;

    @Nullable
    MultipartFile imageUrl;
}
