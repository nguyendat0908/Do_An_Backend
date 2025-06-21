package com.DatLeo.BookShop.entity;

import com.DatLeo.BookShop.util.constant.GenderEnum;
import com.DatLeo.BookShop.util.constant.OrderStatusEnum;
import com.DatLeo.BookShop.util.constant.PaymentMethodEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "tb_orders")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatusEnum status;

    private Double totalPrice;

    @Enumerated(EnumType.STRING)
    private PaymentMethodEnum paymentMethod;
}
