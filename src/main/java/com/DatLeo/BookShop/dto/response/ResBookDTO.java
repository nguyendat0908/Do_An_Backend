package com.DatLeo.BookShop.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
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

    Integer id;
    String name;
    String description;
    Integer sold = 0;
    Integer quantity;
    Double price;
    LocalDate publicationDate;
    String imageUrl;
    String imagePublicId;
    AuthorDTO author;
    CategoryDTO category;

    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class AuthorDTO {
        Integer id;
        String name;
    }

    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class CategoryDTO {
        Integer id;
        String name;
    }
}
