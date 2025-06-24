package com.ecommerce.service;

import com.ecommerce.entity.Order;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    private static final String FROM_EMAIL = "pritmonpara1204@gmail.com";  // Corrected email

    public void sendOrderConfirmationEmail(Order order, String toEmail) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        // Set email sender
        helper.setFrom(FROM_EMAIL);

        // Set recipient email
        helper.setTo(toEmail);

        // Email subject
        helper.setSubject("Order Confirmation - Order ID: " + order.getId());

        // Email content (HTML enabled)
        String emailContent = generateEmailContent(order);
        helper.setText(emailContent, true);

        // Send email
        mailSender.send(message);
    }

    private String generateEmailContent(Order order) {
        StringBuilder content = new StringBuilder();
        System.out.println(order);
        content.append("<h2>Thank you for your order!</h2>");
        content.append("<p>Order ID: <strong>").append(order.getId()).append("</strong></p>");
        content.append("<p>Product Name: <strong>").append(order.getProduct().getName()).append("</strong></p>");
        content.append("<p>Quantity: ").append(order.getQuantity()).append("</p>");
        content.append("<p>Total Price: ₹").append(order.getTotalPrice()).append("</p>");
        content.append("<p>Order Date: ").append(order.getOrderDate()).append("</p>");
        content.append("<hr/>");
        content.append("<p><strong>E-Commerce Inc.</strong></p>");
        content.append("<p>123 Business Street, Bangalore, India</p>");

        return content.toString();
    }
}
