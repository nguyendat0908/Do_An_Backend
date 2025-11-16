package com.DatLeo.BookShop.dto.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ReqAddItemCart {
    Integer bookId;
    Integer quantity;
}
