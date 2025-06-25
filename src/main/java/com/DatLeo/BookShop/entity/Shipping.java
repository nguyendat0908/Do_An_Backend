package com.DatLeo.BookShop.entity;

import com.DatLeo.BookShop.util.constant.StatusShipEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "tb_shippings")
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Shipping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @NotBlank(message = "Tên người nhận hàng không được để trống!")
    String receiveName;

    @NotBlank(message = "Địa chỉ người nhận hàng không được để trống!")
    String receiveAddress;

    @NotBlank(message = "Số điện thoại người nhận hàng không được để trống!")
    String receivePhone;

    Double shippingFee;

    @Enumerated(EnumType.STRING)
    StatusShipEnum status;

    @OneToOne(mappedBy = "shipping")
    Order order;
}
