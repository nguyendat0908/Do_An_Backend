package com.DatLeo.BookShop.entity;

import com.DatLeo.BookShop.util.constant.StatusShipEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_shippings")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Shipping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Tên người nhận hàng không được để trống!")
    private String receiveName;

    @NotBlank(message = "Địa chỉ người nhận hàng không được để trống!")
    private String receiveAddress;

    @NotBlank(message = "Số điện thoại người nhận hàng không được để trống!")
    private String receivePhone;

    private Double shippingFee;

    @Enumerated(EnumType.STRING)
    private StatusShipEnum status;
}
