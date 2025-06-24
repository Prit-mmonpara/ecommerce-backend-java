package com.ecommerce.controller;

import com.ecommerce.entity.Watchlist;
import com.ecommerce.service.WatchlistService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/watchlist")
public class WatchlistController {

    private static final Logger logger = LoggerFactory.getLogger(WatchlistController.class);
    @Autowired
    private WatchlistService watchlistService;

    // Add item to watchlist
    @PostMapping("/add")
    public ResponseEntity<Watchlist> addToWatchlist(@RequestParam Long userId,
                                                    @RequestParam Long productId) {
        logger.info("Adding product with ID: {} to watchlist for user ID: {}", productId, userId);
        if(userId == null || productId == null){
            logger.error("User ID or Product ID is null");
            return ResponseEntity.badRequest().body(null);
        }
        return ResponseEntity.ok(watchlistService.addToWatchlist(userId, productId));
    }

    // Get all watchlist items
    @GetMapping("/{userId}")
    public ResponseEntity<List<Watchlist>> getWatchlistItems(@PathVariable Long userId) {
        logger.info("Fetching watchlist items for user ID: {}", userId);
        if(userId == null){
            logger.error("User ID is null");
            return ResponseEntity.badRequest().body(null);
        }
        return ResponseEntity.ok(watchlistService.getWatchlistItems(userId));
    }

    @GetMapping
    public ResponseEntity<List<Watchlist>> getWatchList(){
        logger.info("Fetching all watchlist items");
        return ResponseEntity.ok(watchlistService.getWatchList());
    }

    // Remove item from watchlist
    @DeleteMapping("/remove")
    public ResponseEntity<String> removeFromWatchlist(@RequestParam Long userId,
                                                      @RequestParam Long productId) {
        logger.info("Removing product with ID: {} from watchlist for user ID: {}", productId, userId);
        if(userId == null || productId == null){
            logger.error("User ID or product ID is null");
            return ResponseEntity.badRequest().body("User ID or Product ID cannot be null");
        }
        watchlistService.removeFromWatchlist(userId, productId);
        return ResponseEntity.ok("Item removed from watchlist!");
    }
}