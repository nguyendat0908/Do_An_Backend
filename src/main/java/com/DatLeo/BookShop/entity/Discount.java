package com.DatLeo.BookShop.entity;

import com.DatLeo.BookShop.exception.ApiMessage;
import com.DatLeo.BookShop.util.constant.ApplyDiscountType;
import com.DatLeo.BookShop.util.constant.DiscountType;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "tb_discounts")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Discount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Integer id;

    @NotBlank(message = ApiMessage.CODE_NOT_NULL)
    @Column(name = "code")
    String code;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    DiscountType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "apply")
    ApplyDiscountType apply;

    @Column(name = "value_cash")
    Double valueCash;

    @Column(name = "value_percent")
    Integer valuePercent;

    @Column(name = "active")
    Boolean active;

    @Column(name = "min_value")
    Double minValue = 0.0;

    @Column(name = "start_date")
    @NotNull(message = ApiMessage.DISCOUNT_START_DATE)
    LocalDate startDate;

    @Column(name = "end_date")
    @NotNull(message = ApiMessage.DISCOUNT_END_DATE)
    LocalDate endDate;

    @Column(name = "usage_limit")
    @NotNull(message = ApiMessage.DISCOUNT_COUNT_NOT_NULL)
    Integer usageLimit;

    @Column(name = "used_count")
    Integer usedCount = 0;

    @OneToMany(mappedBy = "discount", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<Order> orders;

    @ManyToMany
    @JoinTable(
            name = "tb_discount_category",
            joinColumns = @JoinColumn(name = "discount_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    Set<Category> categories = new HashSet<>();

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
