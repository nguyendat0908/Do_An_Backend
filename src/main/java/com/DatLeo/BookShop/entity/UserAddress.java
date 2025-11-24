package com.DatLeo.BookShop.entity;

import com.DatLeo.BookShop.exception.ApiMessage;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Entity
@Table(name = "tb_user_addresses")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @NotBlank(message = ApiMessage.RECEIVER_NAME_NOT_NULL)
    @Column(name = "receive_name")
    String receiveName;

    @NotBlank(message = ApiMessage.RECEIVER_PHONE_NOT_NULL)
    @Pattern(regexp = "^\\d{9,15}$")
    @Column(name = "receive_phone")
    String receivePhone;

    @Column(name = "province")
    String province;

    @Column(name = "district")
    String district;

    @Column(name = "ward")
    String ward;

    @Column(name = "detail_address")
    String detailAddress;

    @Column(name = "is_default")
    Boolean isDefault = false;

    @Column(name = "province_id")
    Integer provinceId;

    @Column(name = "district_id")
    Integer districtId;

    @Column(name = "ward_code")
    String wardCode;

    Instant createdAt;
    Instant updatedAt;

    @PrePersist
    public void onCreate() { this.createdAt = Instant.now(); }

    @PreUpdate
    public void onUpdate() { this.updatedAt = Instant.now(); }
}
