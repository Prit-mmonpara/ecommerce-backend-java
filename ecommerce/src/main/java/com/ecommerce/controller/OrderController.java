package com.ecommerce.controller;

import com.ecommerce.entity.Order;
import com.ecommerce.repository.OrderRepository;
import com.ecommerce.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.mail.MessagingException;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

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
            String customerEmail = order.getCustomerEmail();

            // Check if customerEmail is valid
            if (customerEmail == null || customerEmail.isEmpty()) {
                throw new IllegalArgumentException("Invalid email address");
            }

            emailService.sendOrderConfirmationEmail(savedOrder, customerEmail);
        } catch (MessagingException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }

        return ResponseEntity.ok(savedOrder);
    }

    // Get All Orders
    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderRepository.findAll());
    }

    // Get Order by ID
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(orderRepository.findById(id).orElse(null));
    }

    // Update Order by ID
    @PutMapping("/{id}")
    public ResponseEntity<Order> updateOrder(@PathVariable Long id, @RequestBody Order order) {
        Order updatedOrder = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + id));

        updatedOrder.setQuantity(order.getQuantity());
        updatedOrder.setTotalPrice(order.getTotalPrice());
        updatedOrder.setOrderDate(order.getOrderDate());

        return ResponseEntity.ok(orderRepository.save(updatedOrder));
    }

    // Delete Order
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrder(@PathVariable Long id) {
        orderRepository.deleteById(id);
        return ResponseEntity.ok("Order deleted successfully!");
    }
}
