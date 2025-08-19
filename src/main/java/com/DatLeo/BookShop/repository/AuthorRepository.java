package com.DatLeo.BookShop.repository;

import com.DatLeo.BookShop.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Integer> {
    boolean existsByName(String name);
}
