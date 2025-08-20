package com.DatLeo.BookShop.entity;

import com.DatLeo.BookShop.exception.ApiMessage;
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
    @Column(name = "id")
    Integer id;

    @NotBlank(message = ApiMessage.NAME_NOT_NULL)
    @Column(name = "name")
    String name;

    @Column(name = "description", columnDefinition = "TEXT")
    String description;

    @Column(name = "sold")
    Integer sold;

    @NotBlank(message = ApiMessage.QUANTITY_NOT_NULL)
    @Column(name = "quantity")
    Integer quantity;

    @NotBlank(message = ApiMessage.PRICE_NOT_NULL)
    @Column(name = "price")
    Double price;

    @JsonFormat(pattern = "dd-MM-yyyy")
    @Column(name = "publication_date")
    LocalDate publicationDate;

    @Column(name = "image")
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
