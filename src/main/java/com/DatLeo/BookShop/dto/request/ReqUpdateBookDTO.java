package com.DatLeo.BookShop.dto.request;

import jakarta.persistence.Lob;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
public class ReqUpdateBookDTO {

    Integer id;
    String name;

    @Lob
    String description;
    Integer quantity;
    Double price;
    MultipartFile imageUrl;
}
