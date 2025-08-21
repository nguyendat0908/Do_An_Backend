package com.DatLeo.BookShop.dto.response;

import com.DatLeo.BookShop.exception.ApiMessage;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResBookDTO {

    String name;
    String description;
    Integer sold = 0;
    Integer quantity;
    Double price;
    LocalDate publicationDate;
    String imageUrl;
    String imagePublicId;
}
