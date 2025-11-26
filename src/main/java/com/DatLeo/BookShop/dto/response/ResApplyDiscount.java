package com.DatLeo.BookShop.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResApplyDiscount {
    Double discountAmount;
    Double feeShip;
    Double newTotal;
}
