package com.DatLeo.BookShop.repository;

import com.DatLeo.BookShop.entity.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface DiscountRepository extends JpaRepository<Discount,Integer>, JpaSpecificationExecutor<Discount> {
    boolean existsByCode(String code);
}
