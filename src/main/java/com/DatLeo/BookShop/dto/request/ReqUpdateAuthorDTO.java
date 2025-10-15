package com.DatLeo.BookShop.dto.request;

import jakarta.persistence.Lob;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReqUpdateAuthorDTO {

    Integer id;
    String name;
    @Lob
    String description;
    String address;
    String type;
    String imageUrl;
}
