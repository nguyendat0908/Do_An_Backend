package com.DatLeo.BookShop.dto.request;

import com.DatLeo.BookShop.exception.ApiMessage;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Setter
@Getter
public class ReqUpdateBookDTO {

    Integer id;
    String name;
    @JsonFormat(pattern = "dd-MM-yyyy")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    LocalDate publicationDate;

    @NotNull(message = ApiMessage.AUTHOR_NOT_NULL)
    Integer authorId;

    @NotNull(message = ApiMessage.CATEGORY_NOT_NULL)
    Integer categoryId;

    @Lob
    String description;
    Integer quantity;
    Double price;
    String imageUrl;
}
