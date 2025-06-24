package com.ecommerce.controller;

import com.ecommerce.entity.Cart;
import com.ecommerce.service.CartService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private static final Logger logger = LoggerFactory.getLogger(CartController.class);
    @Autowired
    private CartService cartService;

    // Add item to cart - User must be authenticated
    @PostMapping("/add")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Cart> addToCart(@RequestParam Long userId,
                                          @RequestParam Long productId,
                                          @RequestParam int quantity) {
        if(quantity <= 0) {
            logger.error("Invalid input: quantity={}", quantity);
            return ResponseEntity.badRequest().body(null);
        }
        return ResponseEntity.ok(cartService.addToCart(userId, productId, quantity));
    }

    // Get all the cart Items - Only ADMIN can see all carts
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Cart>> getCarts() {
        logger.info("Fetching all cart items");
        return ResponseEntity.ok(cartService.getAllCartItems());
    }

    // Get cart items by userId - User can only see their own cart, ADMIN can see any cart
    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN') or authentication.principal.id == #userId")
    public ResponseEntity<List<Cart>> getCartItems(@PathVariable Long userId) {
        logger.info("Fetching cart items for user ID: {}", userId);
        if(userId == null) {
            logger.error("User ID is null");
            return ResponseEntity.badRequest().body(null);
        }
        return ResponseEntity.ok(cartService.getCartItems(userId));
    }

    // Update cart item quantity - User can only update their own cart
    @PutMapping("/update")
    @PreAuthorize("hasRole('ADMIN') or authentication.principal.id == #userId")
    public ResponseEntity<Cart> updateCart(@RequestParam Long userId,
                                           @RequestParam Long productId,
                                           @RequestParam int quantity) {
        if(userId == null || productId == null || quantity <= 0) {
            logger.error("Invalid input: userId={}, productId={}, quantity={}", userId, productId, quantity);
            return ResponseEntity.badRequest().body(null);      
        }
        logger.info("Updating cart for user ID: {} with product ID: {} to quantity: {}", userId, productId, quantity);
        return ResponseEntity.ok(cartService.updateCart(userId, productId, quantity));
    }

    // Remove item from cart - User can only remove from their own cart
    @DeleteMapping("/remove")
    @PreAuthorize("hasRole('ADMIN') or authentication.principal.id == #userId")
    public ResponseEntity<String> removeFromCart(@RequestParam Long userId,
                                                 @RequestParam Long productId) {
        if(userId == null || productId == null) {
            logger.error("User ID or Product ID is null");  
            return ResponseEntity.badRequest().body("User ID or Product ID cannot be null");
        }
        logger.info("Removing product with ID: {} from cart for user ID: {}", productId, userId);
        cartService.removeFromCart(userId, productId);
        return ResponseEntity.ok("Item removed from cart!");
    }
}
