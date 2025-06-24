package com.ecommerce.service;

import com.ecommerce.entity.Product;
import com.ecommerce.entity.Watchlist;
import com.ecommerce.repository.ProductRepository;
import com.ecommerce.repository.WatchlistRepository;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WatchlistService {

    @Autowired
    private WatchlistRepository watchlistRepository;

    @Autowired
    private ProductRepository productRepository;

    // Add to watchlist
    public Watchlist addToWatchlist(Long userId, Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Watchlist watchlistItem = new Watchlist();
        watchlistItem.setUserId(userId);
        watchlistItem.setProduct(product);

        return watchlistRepository.save(watchlistItem);
    }

    // Get all watchlist items
    public List<Watchlist> getWatchlistItems(Long userId) {
        return watchlistRepository.findByUserId(userId);
    }

    public List<Watchlist> getWatchList()
    {
        return watchlistRepository.findAll();
    }

    // Remove item from watchlist
    @Transactional
    public void removeFromWatchlist(Long userId, Long productId) {
        watchlistRepository.deleteByUserIdAndProductId(userId, productId);
    }
}
