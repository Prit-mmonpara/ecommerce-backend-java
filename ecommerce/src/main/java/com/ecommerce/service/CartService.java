package com.ecommerce.service;

import com.ecommerce.entity.Cart;
import com.ecommerce.entity.Product;
import com.ecommerce.repository.CartRepository;
import com.ecommerce.repository.ProductRepository;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    // Add item to cart
    public Cart addToCart(Long userId, Long productId, int quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Cart cartItem = new Cart();
        cartItem.setUserId(userId);
        cartItem.setProduct(product);
        cartItem.setQuantity(quantity);

        return cartRepository.save(cartItem);
    }

    public List<Cart> getAllCartItems()
    {
        return cartRepository.findAll();
    }

    // Get all items in user's cart
    public List<Cart> getCartItems(Long userId) {
        return cartRepository.findByUserId(userId);
    }

    // Update item quantity in cart
    public Cart updateCart(Long userId, Long productId, int quantity) {
        List<Cart> cartItems = cartRepository.findByUserId(userId);

        for (Cart item : cartItems) {
            if (item.getProduct().getId().equals(productId)) {
                item.setQuantity(quantity);
                return cartRepository.save(item);
            }
        }
        throw new RuntimeException("Item not found in cart");
    }

    // Remove item from cart
    @Transactional
    public void removeFromCart(Long userId, Long productId) {
        cartRepository.deleteByUserIdAndProductId(userId, productId);
    }
}
