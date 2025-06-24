package com.ecommerce.controller;

import com.ecommerce.entity.Order;
import com.ecommerce.repository.OrderRepository;
import com.ecommerce.service.EmailService;

import org.bouncycastle.crypto.RuntimeCryptoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.mail.MessagingException;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private EmailService emailService;

    // Create Order and Send Email
    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
        // Save the order to the database
        Order savedOrder = orderRepository.save(order);

        // Send email after order creation
        try {
            logger.info("Sending order confirmation email for order ID: {}", savedOrder.getId());
            String customerEmail = order.getCustomerEmail();

            // Check if customerEmail is valid
            if (customerEmail == null || customerEmail.isEmpty()) {
                logger.error("Invalid email address for order ID: {}", savedOrder.getId());
                throw new IllegalArgumentException("Invalid email address");
            }

            emailService.sendOrderConfirmationEmail(savedOrder, customerEmail);
        } catch (MessagingException e) {
            logger.error("Error sending order confirmation email for order ID: {}", savedOrder.getId());
            logger.error("MessagingException: {}", e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }

        return ResponseEntity.ok(savedOrder);
    }

    // Get All Orders
    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        logger.info("Fetching all orders");
        if (orderRepository.findAll().isEmpty()) {
            logger.warn("No orders found in the database");
        }
        return ResponseEntity.ok(orderRepository.findAll());
    }

    // Get Order by ID
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        logger.info("Fetching order with ID: {}", id);
        if (!orderRepository.existsById(id)) {
            logger.error("Order with ID: {} not found", id);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(orderRepository.findById(id).orElse(null));
    }

    // Update Order by ID
    @PutMapping("/{id}")
    public ResponseEntity<Order> updateOrder(@PathVariable Long id, @RequestBody Order order) {
        logger.info("Updating order with ID: {}", id);
        if(!orderRepository.existsById(id)){
            logger.error("Order with ID: {} not found for update", id);
            throw new RuntimeCryptoException("Order not found with ID: " + id);
        }
        Order updatedOrder = orderRepository.findById(id).get();

        updatedOrder.setQuantity(order.getQuantity());
        updatedOrder.setTotalPrice(order.getTotalPrice());
        updatedOrder.setOrderDate(order.getOrderDate());

        return ResponseEntity.ok(orderRepository.save(updatedOrder));
    }

    // Delete Order
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrder(@PathVariable Long id) {
        logger.info("Deleting order with ID: {}", id);
        if(!orderRepository.existsById(id)){
            logger.error("Order with ID: {} not found for deletion", id);
            return ResponseEntity.notFound().build();
        }
        orderRepository.deleteById(id);
        return ResponseEntity.ok("Order deleted successfully!");
    }
}
