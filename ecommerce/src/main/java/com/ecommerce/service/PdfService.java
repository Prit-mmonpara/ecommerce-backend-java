package com.ecommerce.service;

import com.ecommerce.entity.Order;
import com.ecommerce.entity.Product;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
public class PdfService {

    public byte[] generateInvoice(Order order) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(outputStream);
            Document document = new Document(new com.itextpdf.kernel.pdf.PdfDocument(writer));

            // Add Title
            document.add(new Paragraph("Invoice").setBold().setFontSize(20));
            document.add(new Paragraph("Order ID: " + order.getId()));
            document.add(new Paragraph("Order Date: " + order.getOrderDate()));

            // Add Product Details Table
            Table table = new Table(4); // 4 Columns
            table.addCell("Product Name");
            table.addCell("Quantity");
            table.addCell("Unit Price");
            table.addCell("Total Price");

            // Add Order Details
            Product product = order.getProduct();
            table.addCell(product.getName());
            table.addCell(String.valueOf(order.getQuantity()));
            table.addCell("₹" + product.getPrice());
            table.addCell("₹" + order.getTotalPrice());

            document.add(table);
            
            // Add Summary
            document.add(new Paragraph("\nTotal Amount: ₹" + order.getTotalPrice())
                    .setBold().setFontSize(12));
            
            document.close();
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error while generating PDF: " + e.getMessage());
        }
    }
}
