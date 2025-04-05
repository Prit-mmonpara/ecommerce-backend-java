package com.ecommerce.controller;

import com.ecommerce.dto.PaymentRequest;
import com.ecommerce.dto.PaymentResponse;
import com.ecommerce.repository.OrderRepository;
import com.ecommerce.repository.ProductRepository;
import com.ecommerce.service.EmailService;
import com.ecommerce.service.PaymentService;
import com.ecommerce.entity.Order;
import com.ecommerce.entity.Product;

import jakarta.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private ProductRepository productRepository; // Assuming you have a ProductRepository

    // @PostMapping("/create")
    // public ResponseEntity<?> createOrder(@RequestBody PaymentRequest request) {
    //     try {
    //         com.razorpay.Order order = paymentService.createOrder(request.getAmount(), request.getReceipt());
    //         Map<String, Object> response = new HashMap<>();
    //         response.put("orderId", order.get("id"));
    //         response.put("amount", order.get("amount"));
    //         response.put("currency", order.get("currency"));
    //         return ResponseEntity.ok(response);
    //     } catch (Exception e) {
    //         return ResponseEntity.status(500).body(Map.of("error", "Error creating order: " + e.getMessage()));
    //     }
    // }

    @PostMapping("/create")
    public ResponseEntity<?> createOrder(@RequestBody PaymentRequest request) {
        try {
            // Validate request
            if (request.getProductId() == null) {
                return ResponseEntity.badRequest().body(Map.of(
                    "error", "Product ID is required",
                    "status", "validation_failed"
                ));
            }

            // Fetch product from repository
            Product product = productRepository.findById((Long) request.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

            // Create the order in your database
            Order order = new Order();
            order.setCustomerEmail(request.getCustomerEmail());
            order.setTotalPrice(request.getAmount() / 100.0); // Convert from paisa to rupees
            order.setQuantity(1); // Or get from request
            order.setProduct(product);
            order.setRazorpayOrderId("temp_" + System.currentTimeMillis());

            
            // Save to database first
            Order savedOrder = orderRepository.save(order);
            
            // Now create Razorpay order
            com.razorpay.Order razorpayOrder = paymentService.createOrder(request.getAmount(), request.getReceipt());
            
            // Update with actual Razorpay order ID
            savedOrder.setRazorpayOrderId(razorpayOrder.get("id"));
            orderRepository.save(savedOrder);
            
            Map<String, Object> response = new HashMap<>();
            response.put("orderId", razorpayOrder.get("id"));
            response.put("amount", razorpayOrder.get("amount"));
            response.put("currency", razorpayOrder.get("currency"));
            response.put("dbOrderId", savedOrder.getId());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                "error", "Error creating order: " + e.getMessage(),
                "status", "creation_failed"
            ));
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyPayment(@RequestBody Map<String, String> payload) {
        String orderId = payload.get("razorpay_order_id");
        String paymentId = payload.get("razorpay_payment_id");
        String signature = payload.get("razorpay_signature");
        
        if (orderId == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Order ID is required for verification."));
        }
        
        boolean isValid = paymentService.verifyPayment(orderId, paymentId, signature);
        if (isValid) {
            return ResponseEntity.ok(new PaymentResponse(paymentId, "Payment verified successfully", orderId));
        } else {
            return ResponseEntity.status(400).body(Map.of("error", "Payment verification failed."));
        }
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendEmailAfterPayment(@RequestBody Map<String, Object> payload) {
        System.out.println("payload" + payload);

        System.out.println("📧 Sending email after payment...");

        String orderId = (String) payload.get("orderId");
        String customerEmail = (String) payload.get("customerEmail");
        Object amountObj = payload.get("amount");
        if (orderId == null || customerEmail == null || amountObj == null) {
            return ResponseEntity.badRequest().body("Missing required parameters.");
        }

        // Find order by orderId
        // System.out.println("orderId" + orderId);
        // System.out.println("customerEmail" + customerEmail);
        // System.out.println("amountObj" + amountObj);
        Order order = orderRepository.findByRazorpayOrderId(orderId);
        // System.out.println("pppppppp");
        System.out.println(order);

        if (order != null) {
            try {
                emailService.sendOrderConfirmationEmail(order, customerEmail);
                return ResponseEntity.ok("✅ Email sent successfully after payment.");
            } catch (MessagingException e) {
                e.printStackTrace();
                return ResponseEntity.internalServerError().body("❌ Error sending email.");
            }
        } else {
            return ResponseEntity.badRequest().body("❌ Order not found.");
        }
    }
}