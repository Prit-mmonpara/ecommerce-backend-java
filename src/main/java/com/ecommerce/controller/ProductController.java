package com.ecommerce.controller;

import com.ecommerce.entity.Product;
import com.ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService productService;
    // Get All Products - Everyone can access
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        logger.info("Fetching all products");
        return ResponseEntity.ok(productService.getAllProducts());
    }

    // Get Product by ID - Everyone can access
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        logger.info("Fetching product with ID: {}", id);
        return ResponseEntity.ok(productService.getProductById(id));
    }    

    // Create Product - Only ADMIN can create products
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        logger.info("Creating new product: {}", product);
        return ResponseEntity.ok(productService.createProduct(product));
    }

    // Update Product by ID - Only ADMIN can update
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        logger.info("Updating product with ID: {} to {}", id, product);
        return ResponseEntity.ok(productService.updateProduct(id, product));
    }

    // Delete Product - Only ADMIN can delete
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        logger.info("Deleting product with ID: {}", id);
        productService.deleteProduct(id);
        logger.info("Product with ID: {} deleted successfully", id);
        return ResponseEntity.ok("Product deleted successfully!");
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Product> partialUpdateProduct(@PathVariable Long id, @RequestBody Product product) {
        logger.info("Partially updating product with ID: {} to {}", id, product);
        return ResponseEntity.ok(productService.partialUpdateProduct(id, product));
    }
}
