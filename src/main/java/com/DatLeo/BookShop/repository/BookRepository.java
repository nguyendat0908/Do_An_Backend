package com.DatLeo.BookShop.repository;

import com.DatLeo.BookShop.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer>, JpaSpecificationExecutor<Book> {

    @Query("SELECT COUNT(b) FROM Book b WHERE b.author.id = :authorId")
    Integer countBooksByAuthorId(@Param("authorId") Integer authorId);

    boolean existsByName(String name);

    Page<Book> findByCategoryId(Integer categoryId, Pageable pageable);

    @Query("SELECT b FROM Book b WHERE b.category.id = :categoryId " +
            "AND (:minPrice IS NULL OR b.price >= :minPrice) " +
            "AND (:maxPrice IS NULL OR b.price <= :maxPrice)")
    Page<Book> findByCategoryIdAndPriceRange(@Param("categoryId") Integer categoryId,
                                             @Param("minPrice") Double minPrice,
                                             @Param("maxPrice") Double maxPrice,
                                             Pageable pageable);

    @Query("""
    SELECT b FROM Book b
    WHERE (:categoryIds IS NULL OR b.category.id IN :categoryIds)
      AND (:authorIds IS NULL OR b.author.id IN :authorIds)
      AND (:minPrice IS NULL OR b.price >= :minPrice)
      AND (:maxPrice IS NULL OR b.price <= :maxPrice)
      AND (
           :keyword IS NULL 
           OR LOWER(b.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(b.author.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
      )
""")
    Page<Book> findByFilters(@Param("categoryIds") List<Integer> categoryIds,
                             @Param("authorIds") List<Integer> authorIds,
                             @Param("minPrice") Double minPrice,
                             @Param("maxPrice") Double maxPrice,
                             @Param("keyword") String keyword,
                             Pageable pageable);
}
