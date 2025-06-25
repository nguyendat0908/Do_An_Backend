package com.DatLeo.BookShop.entity;

import com.DatLeo.BookShop.util.constant.GenderEnum;
import com.DatLeo.BookShop.util.constant.OrderStatusEnum;
import com.DatLeo.BookShop.util.constant.PaymentMethodEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "tb_orders")
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @JsonFormat(pattern = "dd-MM-yyyy")
    LocalDate orderDate;

    @Enumerated(EnumType.STRING)
    OrderStatusEnum status;

    Double totalPrice;

    @Enumerated(EnumType.STRING)
    PaymentMethodEnum paymentMethod;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    @ManyToOne
    @JoinColumn(name = "discount_id")
    Discount discount;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<OrderDetail> orderDetails;

    @OneToOne
    @JoinColumn(name = "shipping_id")
    Shipping shipping;

    @OneToOne
    @JoinColumn(name = "payment_id")
    Payment payment;

}
