package com.DatLeo.BookShop.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ShippingFeeResponse {
    String receiverName;
    String receiverPhone;
    String receiverAddress;
    Object ghnFee;
}
