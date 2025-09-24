package com.DatLeo.BookShop.entity;

import com.DatLeo.BookShop.exception.ApiMessage;
import com.DatLeo.BookShop.util.constant.DiscountType;
import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Discount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Integer id;

    @NotBlank(message = ApiMessage.CODE_NOT_NULL)
    @Column(name = "code")
    String code;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    DiscountType type;

    @Column(name = "value")
    Double value;

    @Column(name = "min_value")
    Double minValue = 0.0;

    @Column(name = "start_date")
    @NotNull(message = ApiMessage.DISCOUNT_START_DATE)
    LocalDate startDate;

    @Column(name = "end_date")
    @NotNull(message = ApiMessage.DISCOUNT_END_DATE)
    LocalDate endDate;

    @Column(name = "usage_limit")
    @NotNull(message = ApiMessage.DISCOUNT_COUNT_NOT_NULL)
    Integer usageLimit;

    @Column(name = "used_count")
    Integer usedCount = 0;

    @OneToMany(mappedBy = "discount", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<Order> orders;
}
