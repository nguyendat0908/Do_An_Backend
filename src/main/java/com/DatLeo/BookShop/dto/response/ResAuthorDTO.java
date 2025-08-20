package com.DatLeo.BookShop.dto.response;

import com.DatLeo.BookShop.entity.Book;
import com.DatLeo.BookShop.exception.ApiMessage;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.List;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResAuthorDTO {
    Integer id;
    String name;
    String description;
    String address;
    String type;
    Integer totalBooks;
    String imageUrl;
    String imagePublicId;
    Instant createdAt;
    Instant updatedAt;
}
