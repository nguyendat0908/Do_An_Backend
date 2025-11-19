package com.DatLeo.BookShop.repository;

import com.DatLeo.BookShop.entity.BookViewHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookViewHistoryRepository extends JpaRepository<BookViewHistory, Integer> {
    Optional<BookViewHistory> findByUserIdAndBookId(Integer userId, Integer id);

    List<BookViewHistory> findTop6ByUserIdOrderByViewedAtDesc(Integer userId);
}
