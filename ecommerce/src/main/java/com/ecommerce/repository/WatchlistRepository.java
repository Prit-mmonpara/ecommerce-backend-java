package com.ecommerce.repository;

import com.ecommerce.entity.Watchlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WatchlistRepository extends JpaRepository<Watchlist, Long> {
    List<Watchlist> findByUserId(Long userId);
    void deleteByUserIdAndProductId(Long userId, Long productId);
}
