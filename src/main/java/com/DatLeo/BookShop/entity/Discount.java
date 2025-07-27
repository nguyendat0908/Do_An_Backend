package com.DatLeo.BookShop.entity;

import com.DatLeo.BookShop.exception.ApiMessage;
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
@Table(name = "tb_discounts")
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Discount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @NotBlank(message = ApiMessage.CODE_NOT_NULL)
    String code;

    Boolean type;
    Double value;
    Double minValue;
    LocalDate startDate;
    LocalDate endDate;
    Boolean active;
    Integer usageLimit;
    Integer usedCount;

    @OneToMany(mappedBy = "discount", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<Order> orders;
}
