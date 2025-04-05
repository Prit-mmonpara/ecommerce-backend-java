package com.ecommerce.dto;

import lombok.Data;

@Data
public class PaymentRequest {
    private int amount;           // Amount in paisa
    private String receipt;       // Unique receipt ID
    private String currency = "INR";
    private String customerEmail;
    private Long productId;       // Add product ID field
}