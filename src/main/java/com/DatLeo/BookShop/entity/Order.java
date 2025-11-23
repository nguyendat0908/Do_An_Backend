package com.DatLeo.BookShop.entity;

import com.DatLeo.BookShop.util.constant.GenderEnum;
import com.DatLeo.BookShop.util.constant.OrderStatusEnum;
import com.DatLeo.BookShop.util.constant.PaymentMethodEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "tb_orders")
@Setter
@Getter
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

    Double subTotal;

    Double shippingFee;

    Double discountAmount;

    String reasonCancel;

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

    Instant createdAt;
    Instant updatedAt;

    @PrePersist
    public void handleBeforeCreate() {
        this.createdAt = Instant.now();
    }

    @PreUpdate
    public void handleBeforeUpdate() {
        this.updatedAt = Instant.now();
    }
}
