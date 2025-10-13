package com.DatLeo.BookShop.repository;

import com.DatLeo.BookShop.entity.PhotoHome;
import com.DatLeo.BookShop.util.constant.PhotoType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhotoHomeRepository extends JpaRepository<PhotoHome,Integer> {

    void deleteByType(PhotoType type);

    PhotoHome findByType(PhotoType type);

    List<PhotoHome> findAllByType(PhotoType photoType);
}
