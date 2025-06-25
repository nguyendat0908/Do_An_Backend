package com.DatLeo.BookShop.entity;

import com.DatLeo.BookShop.util.constant.GenderEnum;
import com.DatLeo.BookShop.util.constant.SsoTypeEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "tb_users")
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @NotBlank(message = "Tên không được để trống!")
    String name;

    @NotBlank(message = "Email không được để trống!")
    @Email(message = "Email không đúng định dạng!")
    String email;

    @NotBlank(message = "Mật khẩu không được để trống!")
    @Size(min = 8, message = "Mật khẩu phải lớn hơn 8 ký tự!")
    String password;

    String address;
    String phone;
    String avatar;
    String ssoID;

    @Enumerated(EnumType.STRING)
    GenderEnum gender;

    @Enumerated(EnumType.STRING)
    SsoTypeEnum ssoType;

    Boolean active;

    @Column(columnDefinition = "TEXT")
    String refreshToken;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<Order> orders;

    @ManyToOne
    @JoinColumn(name = "role_id")
    Role role;

    @OneToOne
    @JoinColumn(name = "cart_id")
    Cart cart;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<Review> reviews;

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
