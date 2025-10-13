package com.DatLeo.BookShop.repository;

import com.DatLeo.BookShop.entity.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DiscountRepository extends JpaRepository<Discount,Integer>, JpaSpecificationExecutor<Discount> {
    boolean existsByCode(String code);

    @Query("SELECT DISTINCT d FROM Discount d LEFT JOIN FETCH d.categories WHERE d.id = :id")
    Optional<Discount> findByIdWithCategories(@Param("id") Integer id);
}
