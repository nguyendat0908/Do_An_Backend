package com.DatLeo.BookShop.dto.request;

import com.DatLeo.BookShop.exception.ApiMessage;
import com.DatLeo.BookShop.util.constant.ApplyDiscountType;
import com.DatLeo.BookShop.util.constant.DiscountType;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Setter
@Getter
public class ReqDiscountDTO {

    @NotBlank(message = ApiMessage.CODE_NOT_NULL)
    String code;

    @Enumerated(EnumType.STRING)
    DiscountType type;

    @Enumerated(EnumType.STRING)
    ApplyDiscountType apply;

    Double valueCash;
    Integer valuePercent;
    Double minValue = 0.0;

    @NotNull(message = ApiMessage.DISCOUNT_START_DATE)
    LocalDate startDate;

    @NotNull(message = ApiMessage.DISCOUNT_END_DATE)
    LocalDate endDate;

    @NotNull(message = ApiMessage.DISCOUNT_COUNT_NOT_NULL)
    Integer usageLimit;

    Set<Integer> categoryIds;
}
