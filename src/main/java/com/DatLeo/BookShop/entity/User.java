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
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedDate;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "tb_users")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Integer id;

    @NotBlank(message = ApiMessage.NAME_NOT_NULL)
    @Column(name = "name")
    String name;

    @NotBlank(message = ApiMessage.EMAIL_NOT_NULL)
    @Email(message = ApiMessage.EMAIL_NOT_CORRECT_FORMAT)
    @Column(name = "email")
    String email;

    @NotBlank(message = ApiMessage.PASSWORD_NOT_NULL)
    @Size(min = 8, message = ApiMessage.PASSWORD_MUST_BE_GREATER)
    @Column(name = "password")
    String password;

    String address;

    @Pattern(regexp = "^\\d{10}$", message = ApiMessage.PHONE_NUMBER_FORMAT)
    @Column(name = "phone")
    String phone;

    @Column(name = "image_url")
    String imageUrl;

    @Column(name = "sso_id")
    String ssoID;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    GenderEnum gender;

    @Enumerated(EnumType.STRING)
    @Column(name = "sso_type")
    SsoTypeEnum ssoType;

    @Column(name = "active", nullable = false)
    Boolean active;

    @Column(name = "refresh_token", columnDefinition = "TEXT")
    String refreshToken;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<Order> orders;

    @ManyToOne
    @JoinColumn(name = "role_id")
    Role role;

    @OneToOne(mappedBy = "user")
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
