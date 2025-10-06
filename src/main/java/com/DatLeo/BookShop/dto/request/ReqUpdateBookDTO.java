package com.DatLeo.BookShop.dto.request;

import jakarta.persistence.Lob;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ReqUpdateBookDTO {

    Integer id;
    String name;

    @Lob
    String description;
    Integer quantity;
    Double price;
    String imageUrl;
}
