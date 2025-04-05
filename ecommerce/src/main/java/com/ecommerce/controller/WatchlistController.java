package com.ecommerce.controller;

import com.ecommerce.entity.Watchlist;
import com.ecommerce.service.WatchlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/watchlist")
public class WatchlistController {

    @Autowired
    private WatchlistService watchlistService;

    // Add item to watchlist
    @PostMapping("/add")
    public ResponseEntity<Watchlist> addToWatchlist(@RequestParam Long userId,
                                                    @RequestParam Long productId) {
        return ResponseEntity.ok(watchlistService.addToWatchlist(userId, productId));
    }

    // Get all watchlist items
    @GetMapping("/{userId}")
    public ResponseEntity<List<Watchlist>> getWatchlistItems(@PathVariable Long userId) {
        return ResponseEntity.ok(watchlistService.getWatchlistItems(userId));
    }

    @GetMapping
    public ResponseEntity<List<Watchlist>> getWatchList()
    {
        return ResponseEntity.ok(watchlistService.getWatchList());
    }

    // Remove item from watchlist
    @DeleteMapping("/remove")
    public ResponseEntity<String> removeFromWatchlist(@RequestParam Long userId,
                                                      @RequestParam Long productId) {
        watchlistService.removeFromWatchlist(userId, productId);
        return ResponseEntity.ok("Item removed from watchlist!");
    }
}