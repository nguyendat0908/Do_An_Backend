package com.DatLeo.BookShop.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResCategoryDTO {
    Integer id;
    String name;
    String description;
}
