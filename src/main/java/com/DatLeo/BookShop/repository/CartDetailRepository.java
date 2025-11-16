package com.DatLeo.BookShop.repository;

import com.DatLeo.BookShop.entity.CartDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartDetailRepository extends JpaRepository<CartDetail,Integer> {

    Optional<CartDetail> findByCartIdAndBookId(Integer cartId, Integer bookId);
}
