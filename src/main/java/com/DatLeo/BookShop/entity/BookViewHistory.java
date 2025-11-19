package com.DatLeo.BookShop.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Entity
@Table(name = "tb_book_view_histories")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookViewHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "book_id")
    Book book;

    Instant viewedAt;

    @PrePersist
    public void onCreate() {
        viewedAt = Instant.now();
    }

}
