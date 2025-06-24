package com.ecommerce.service;

import com.ecommerce.entity.Product;
import com.ecommerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    
    @Autowired
    private ProductRepository productRepository;

    // @Cacheable(value = "products")
    // public String test(){
    //     System.out.println("test method called");
    //     return "test method called";
    // }

    // Create Product
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    // Get All Products
    // @Cacheable(value = "allProducts")
    public List<Product> getAllProducts() {
        System.out.println("Inside allProducts method");
        return productRepository.findAll();
    }

    // Get Product by ID
    // @Cacheable(value = "productById", key = "#id")
    public Product getProductById(Long id) {
        System.out.println("Inside getProductById method");
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product with the given id is not found: " + id));
    }

    // Update Product
    public Product updateProduct(Long id, Product updatedProduct) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Product not found with ID: " + id));

        product.setName(updatedProduct.getName());
        product.setDescription(updatedProduct.getDescription());
        product.setPrice(updatedProduct.getPrice());
        product.setStock(updatedProduct.getStock());
        product.setCategory(updatedProduct.getCategory());

        return productRepository.save(product);
    }

    // Delete Product
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Product not found with ID: " + id);
        }
        productRepository.deleteById(id);
    }

    public Product partialUpdateProduct(Long id, Product product) {
        Product existingProduct = productRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Product not found with ID: " + id));

        if (product.getName() != null) {
            existingProduct.setName(product.getName());
        }
        if (product.getDescription() != null) {
            existingProduct.setDescription(product.getDescription());
        }
        if (product.getPrice() != null) {
            existingProduct.setPrice(product.getPrice());
        }
        if (product.getStock() != null) {
            existingProduct.setStock(product.getStock());
        }
        if (product.getCategory() != null) {
            existingProduct.setCategory(product.getCategory());
        }

        return productRepository.save(existingProduct);
    }
}
