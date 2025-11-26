package com.DatLeo.BookShop.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReqApplyDiscount {
    String code;
    List<Integer> bookIds;
    Double subTotal;
    Double feeShip;
}
