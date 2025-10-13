package com.DatLeo.BookShop.dto.response;

import com.DatLeo.BookShop.util.constant.ApplyDiscountType;
import com.DatLeo.BookShop.util.constant.DiscountType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Set;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResDiscountDTO {

    Integer id;
    String code;
    DiscountType type;
    ApplyDiscountType apply;
    Double value;
    Boolean active;
    Double minValue;
    LocalDate startDate;
    LocalDate endDate;
    Integer usageLimit;
    Integer usedCount;
    Instant createdAt;
    Instant updatedAt;
    Set<CategoryDTO> categories;

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
