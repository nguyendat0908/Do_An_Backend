package com.DatLeo.BookShop.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "tb_discounts")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Discount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Mã CODE không được để trống!")
    private String code;

    private Boolean type;
    private Double value;
    private Double minValue;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean active;
    private Integer usageLimit;
    private Integer usedCount;
}
