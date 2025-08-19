package com.DatLeo.BookShop.repository;

import com.DatLeo.BookShop.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {

    @Query("SELECT COUNT(b) FROM Book b WHERE b.author.id = :authorId")
    Integer countBooksByAuthorId(@Param("authorId") Integer authorId);
}
