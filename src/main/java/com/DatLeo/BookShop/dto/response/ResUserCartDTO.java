package com.DatLeo.BookShop.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResUserCartDTO {

    Integer userId;
    Integer cartId;
    Integer totalBooks;
    List<BookDetail> books;

    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class BookDetail {
        Integer id;
        Integer cartDetailId;
        String bookName;
        String bookAuthor;
        Double bookPrice;
        Integer bookQuantity;
        String bookImage;
    }
}
