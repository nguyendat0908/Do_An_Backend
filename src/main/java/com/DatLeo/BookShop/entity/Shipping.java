package com.DatLeo.BookShop.entity;

import com.DatLeo.BookShop.exception.ApiMessage;
import com.DatLeo.BookShop.util.constant.StatusShipEnum;
import com.cloudinary.Api;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Entity
@Table(name = "tb_shippings")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Shipping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @NotBlank(message = ApiMessage.RECEIVER_NAME_NOT_NULL)
    String receiveName;

    @NotBlank(message = ApiMessage.RECEIVER_ADDRESS_NOT_NULL)
    String receiveAddress;

    @NotBlank(message = ApiMessage.RECEIVER_PHONE_NOT_NULL)
    String receivePhone;

    Double shippingFee;

    @Enumerated(EnumType.STRING)
    StatusShipEnum status;

    @OneToOne(mappedBy = "shipping")
    Order order;

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
