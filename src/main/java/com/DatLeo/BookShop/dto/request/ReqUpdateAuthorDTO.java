package com.DatLeo.BookShop.dto.request;

import jakarta.persistence.Lob;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
public class ReqUpdateAuthorDTO {

    Integer id;
    String name;
    @Lob
    String description;
    String address;
    String type;
    String imageUrl;
}
