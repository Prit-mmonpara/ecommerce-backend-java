package com.ecommerce.controller;

import com.ecommerce.entity.Category;
import com.ecommerce.repository.CategoryRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// @CrossOrigin(origins = "http://localhost:3000") // Optional, change as needed
@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    
    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);
    @Autowired
    private CategoryRepository categoryRepository;

    // Create Category
    @PostMapping
    public ResponseEntity<Category> createCategory(@RequestBody Category category) {
        logger.info("Creating new category: {}", category);
        if(category.getName() == null || category.getName().isEmpty()) {
            logger.error("Category name cannot be null or empty");
            return ResponseEntity.badRequest().body(null);
        }
        return ResponseEntity.ok(categoryRepository.save(category));
    }

    // Get All Categories
    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() {
        logger.info("Fetching all categories");
        List<Category> categories = categoryRepository.findAll();
        if(categories.isEmpty()) {
            logger.warn("No categories found");
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(categories);
    }

    // Get Category by ID
    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Long id) {
        logger.info("Fetching category with ID: {}", id);

        return categoryRepository.findById(id)
            .map(category -> {
                logger.debug("Found category with ID: {}", category.getId());
                return ResponseEntity.ok(category);
            })
            .orElseGet(() -> {
                logger.error("Category not found with ID: {}", id);
                return ResponseEntity.notFound().build();
            });
    }

    // Update Category By ID
    @PutMapping("/{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable Long id, @RequestBody Category category) {
        logger.info("Updating category with ID: {}", id);
        if(category.getName() == null || category.getName().isEmpty()) {
            logger.error("Category name cannot be null or empty");
            return ResponseEntity.badRequest().body(null);
        }
        Category updatedCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with ID: " + id));

        updatedCategory.setName(category.getName());

        return ResponseEntity.ok(categoryRepository.save(updatedCategory));
    }

    // Delete Category
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
        logger.info("Deleting category with ID: {}", id);
        categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with ID: " + id));

        categoryRepository.deleteById(id);
        return ResponseEntity.ok("Category deleted successfully!");
    }
}