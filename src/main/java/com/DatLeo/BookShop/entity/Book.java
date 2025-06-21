package com.DatLeo.BookShop.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "tb_books")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Tên sách không được để trống!")
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    private Integer sold;

    @NotBlank(message = "Số lượng sách không được để trống!")
    private Integer quantity;

    @NotBlank(message = "Giá sách không được để trống!")
    private Double price;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate publicationDate;

    private String image;

}
