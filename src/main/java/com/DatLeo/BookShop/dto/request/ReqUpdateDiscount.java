package com.DatLeo.BookShop.dto.request;

import com.DatLeo.BookShop.exception.ApiMessage;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReqUpdateDiscount {

    Integer id;

    @NotNull(message = ApiMessage.DISCOUNT_END_DATE)
    LocalDate endDate;

    @NotNull(message = ApiMessage.DISCOUNT_COUNT_NOT_NULL)
    Integer usageLimit;

    Boolean active;
}
