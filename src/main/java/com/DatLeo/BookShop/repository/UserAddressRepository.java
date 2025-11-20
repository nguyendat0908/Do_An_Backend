package com.DatLeo.BookShop.repository;

import com.DatLeo.BookShop.entity.UserAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserAddressRepository extends JpaRepository<UserAddress,Integer> {
    List<UserAddress> findAllByUserId(Integer userId);
}
