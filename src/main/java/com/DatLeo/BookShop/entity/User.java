package com.DatLeo.BookShop.entity;

import com.DatLeo.BookShop.exception.ApiMessage;
import com.DatLeo.BookShop.util.constant.GenderEnum;
import com.DatLeo.BookShop.util.constant.SsoTypeEnum;
import com.cloudinary.Api;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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

    @NotBlank(message = ApiMessage.NAME_NOT_NULL)
    String name;

    @NotBlank(message = ApiMessage.EMAIL_NOT_NULL)
    @Email(message = ApiMessage.EMAIL_NOT_CORRECT_FORMAT)
    String email;

    @NotBlank(message = ApiMessage.PASSWORD_NOT_NULL)
    @Size(min = 8, message = ApiMessage.PASSWORD_MUST_BE_GREATER)
    String password;

    String address;

    @Pattern(regexp = "^\\d{10}$", message = ApiMessage.PHONE_NUMBER_FORMAT)
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
