package com.DatLeo.BookShop.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "tb_books")
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @NotBlank(message = "Tên sách không được để trống!")
    String name;

    @Column(columnDefinition = "TEXT")
    String description;

    Integer sold;

    @NotBlank(message = "Số lượng sách không được để trống!")
    Integer quantity;

    @NotBlank(message = "Giá sách không được để trống!")
    Double price;

    @JsonFormat(pattern = "dd-MM-yyyy")
    LocalDate publicationDate;

    String image;

    @ManyToOne
    @JoinColumn(name = "author_id")
    Author author;

    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<Review> reviews;

    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<OrderDetail> orderDetails;

    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<CartDetail> cartDetails;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

}
