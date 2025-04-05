// package com.ecommerce.controller;

// import com.ecommerce.entity.Order;
// import com.ecommerce.repository.OrderRepository;
// import com.itextpdf.kernel.colors.DeviceRgb;
// import com.itextpdf.kernel.pdf.PdfDocument;
// import com.itextpdf.kernel.pdf.PdfWriter;
// import com.itextpdf.layout.Document;
// import com.itextpdf.layout.element.Cell;
// import com.itextpdf.layout.element.Paragraph;
// import com.itextpdf.layout.element.Table;
// import com.itextpdf.layout.properties.TextAlignment;
// import com.itextpdf.layout.properties.UnitValue;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.core.io.InputStreamResource;
// import org.springframework.http.HttpHeaders;
// import org.springframework.http.MediaType;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;

// import java.io.ByteArrayInputStream;
// import java.io.ByteArrayOutputStream;

// @RestController
// @RequestMapping("/api/pdf")
// public class PdfController {

//     @Autowired
//     private OrderRepository orderRepository;

//     // Generate PDF Invoice
//     @GetMapping("/invoices/{id}")
//     public ResponseEntity<InputStreamResource> generateInvoice(@PathVariable Long id) {
//         Order order = orderRepository.findById(id)
//                 .orElseThrow(() -> new RuntimeException("Order not found with ID: " + id));

//         ByteArrayOutputStream out = new ByteArrayOutputStream();

//         try {
//             PdfWriter writer = new PdfWriter(out);
//             PdfDocument pdf = new PdfDocument(writer);
//             Document document = new Document(pdf);

//             // Add custom title with styling
//             Paragraph title = new Paragraph("Invoice for Order #" + order.getId())
//                     .setBold()
//                     .setFontSize(18)
//                     .setTextAlignment(TextAlignment.CENTER)
//                     .setMarginBottom(20);
//             document.add(title);

//             // Add order details in a table format
//             Table table = new Table(2);
//             table.setWidth(UnitValue.createPercentValue(100));
//             table.setMarginBottom(20);

//             // Define cell styles
//             Cell headerCellStyle = new Cell().setBold().setBackgroundColor(new DeviceRgb(230, 230, 230))
//                     .setPadding(5);
//             Cell normalCellStyle = new Cell().setPadding(5);

//             // Add headers
//             table.addCell(headerCellStyle.add(new Paragraph("Field")));
//             table.addCell(headerCellStyle.add(new Paragraph("Details")));

//             // Add order details
//             table.addCell(normalCellStyle.add(new Paragraph("Product Name")));
//             table.addCell(normalCellStyle.add(new Paragraph(order.getProduct().getName())));

//             table.addCell(normalCellStyle.add(new Paragraph("Description")));
//             table.addCell(normalCellStyle.add(new Paragraph(order.getProduct().getDescription())));

//             table.addCell(normalCellStyle.add(new Paragraph("Price")));
//             table.addCell(normalCellStyle.add(new Paragraph("₹" + order.getProduct().getPrice())));

//             table.addCell(normalCellStyle.add(new Paragraph("Quantity")));
//             table.addCell(normalCellStyle.add(new Paragraph(String.valueOf(order.getQuantity()))));

//             table.addCell(normalCellStyle.add(new Paragraph("Total Price")));
//             table.addCell(normalCellStyle.add(new Paragraph("₹" + order.getTotalPrice())));

//             table.addCell(normalCellStyle.add(new Paragraph("Order Date")));
//             table.addCell(normalCellStyle.add(new Paragraph(order.getOrderDate().toString())));

//             document.add(table);

//             // Add footer
//             Paragraph footer = new Paragraph("Thank you for your order!")
//                     .setFontSize(12)
//                     .setTextAlignment(TextAlignment.CENTER)
//                     .setMarginTop(20);
//             document.add(footer);

//             document.close();
//         } catch (Exception e) {
//             e.printStackTrace();
//         }

//         ByteArrayInputStream bis = new ByteArrayInputStream(out.toByteArray());

//         HttpHeaders headers = new HttpHeaders();
//         headers.add("Content-Disposition", "inline; filename=invoice_" + id + ".pdf");

//         return ResponseEntity
//                 .ok()
//                 .headers(headers)
//                 .contentType(MediaType.APPLICATION_PDF)
//                 .body(new InputStreamResource(bis));
//     }
// }

package com.ecommerce.controller;

import com.ecommerce.entity.Order;
import com.ecommerce.repository.OrderRepository;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.properties.VerticalAlignment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api/pdf")
public class PdfController {

    @Autowired
    private OrderRepository orderRepository;

    // Color scheme
    private static final Color PRIMARY_COLOR = new DeviceRgb(63, 81, 181);
    private static final Color LIGHT_GRAY = new DeviceRgb(245, 245, 245);
    private static final Color DARK_GRAY = new DeviceRgb(97, 97, 97);

    @GetMapping("/invoices/{id}")
    public ResponseEntity<InputStreamResource> generateInvoice(@PathVariable Long id) throws IOException {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + id));

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            // Initialize PDF document with A4 size
            PdfWriter writer = new PdfWriter(out);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf, PageSize.A4);
            document.setMargins(40, 40, 40, 40);

            // Load fonts
            PdfFont fontRegular = PdfFontFactory.createFont("Helvetica");
            PdfFont fontBold = PdfFontFactory.createFont("Helvetica-Bold");

            // Header section
            Div header = new Div()
                    .setMarginBottom(30)
                    .setBorderBottom(new SolidBorder(PRIMARY_COLOR, 1))
                    .setPaddingBottom(10);

            // Company logo and info
            Table headerTable = new Table(new float[]{1, 3})
                    .setWidth(UnitValue.createPercentValue(100));

            Cell logoCell = new Cell()
                    .add(new Paragraph("ECOMM")
                            .setFont(fontBold)
                            .setFontSize(20)
                            .setFontColor(PRIMARY_COLOR))
                    .setBorder(Border.NO_BORDER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE);

            Cell infoCell = new Cell()
                    .add(new Paragraph("E-Commerce Inc.")
                            .setFont(fontBold)
                            .setFontSize(12))
                    .add(new Paragraph("123 Business Street")
                            .setFont(fontRegular)
                            .setFontSize(10))
                    .add(new Paragraph("Bangalore, India 560001")
                            .setFont(fontRegular)
                            .setFontSize(10))
                    .add(new Paragraph("GSTIN: 22AAAAA0000A1Z5")
                            .setFont(fontRegular)
                            .setFontSize(10))
                    .setTextAlignment(TextAlignment.RIGHT)
                    .setBorder(Border.NO_BORDER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE);

            headerTable.addCell(logoCell);
            headerTable.addCell(infoCell);
            header.add(headerTable);

            // Invoice title and details
            Paragraph invoiceTitle = new Paragraph("INVOICE")
                    .setFont(fontBold)
                    .setFontSize(24)
                    .setFontColor(PRIMARY_COLOR)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(20);

            // Invoice metadata table
            Table invoiceMetaTable = new Table(new float[]{1, 1, 1})
                    .setWidth(UnitValue.createPercentValue(100))
                    .setMarginBottom(30);

            // Invoice number and date
            invoiceMetaTable.addCell(createMetaCell("Invoice #", "INV-" + order.getId(), fontBold, fontRegular));
            invoiceMetaTable.addCell(createMetaCell("Order Date", 
                    order.getOrderDate().format(DateTimeFormatter.ofPattern("dd MMM, yyyy")), 
                    fontBold, fontRegular));
            invoiceMetaTable.addCell(createMetaCell("Due Date", 
                    order.getOrderDate().plusDays(7).format(DateTimeFormatter.ofPattern("dd MMM, yyyy")), 
                    fontBold, fontRegular));

            // Customer information
            // In the generateInvoice method's customer information section:

            // Customer information
            Table customerTable = new Table(new float[]{1, 1})
            .setWidth(UnitValue.createPercentValue(100))
            .setMarginBottom(30);

            Cell customerHeader = new Cell(1, 2)
            .add(new Paragraph("BILL TO")
                .setFont(fontBold)
                .setFontSize(12)
                .setFontColor(PRIMARY_COLOR))
            .setBackgroundColor(LIGHT_GRAY)
            .setPadding(8)
            .setBorder(Border.NO_BORDER);

            customerTable.addCell(customerHeader);

            // Assuming these fields exist in your Order entity
            customerTable.addCell(createCustomerCell("Product Name", order.getProduct().getName(), fontBold, fontRegular));
            customerTable.addCell(createCustomerCell("Product Description", order.getProduct().getDescription(), fontBold, fontRegular));
            customerTable.addCell(createCustomerCell("Price", "₹" + order.getProduct().getPrice(), fontBold, fontRegular));
            customerTable.addCell(createCustomerCell("Quantity", String.valueOf(order.getQuantity()), fontBold, fontRegular));
            customerTable.addCell(createCustomerCell("Total Price", "₹" + order.getTotalPrice(), fontBold, fontRegular));
            customerTable.addCell(createCustomerCell("Order Date", order.getOrderDate().format(DateTimeFormatter.ofPattern("dd MMM, yyyy")), fontBold, fontRegular));

            // Items table
            Table itemsTable = new Table(new float[]{3, 1, 1, 1, 1})
                    .setWidth(UnitValue.createPercentValue(100))
                    .setMarginBottom(20);

            // Table headers
            Color headerBgColor = new DeviceRgb(63, 81, 181);
            float headerFontSize = 10;

            itemsTable.addCell(createHeaderCell("Description", headerBgColor, fontBold, headerFontSize));
            itemsTable.addCell(createHeaderCell("Price", headerBgColor, fontBold, headerFontSize));
            itemsTable.addCell(createHeaderCell("Qty", headerBgColor, fontBold, headerFontSize));
            itemsTable.addCell(createHeaderCell("Tax", headerBgColor, fontBold, headerFontSize));
            itemsTable.addCell(createHeaderCell("Amount", headerBgColor, fontBold, headerFontSize));

            // Table content
            itemsTable.addCell(createContentCell(order.getProduct().getName() + "\n" + 
                    order.getProduct().getDescription(), fontRegular, 10));
            itemsTable.addCell(createContentCell("₹" + order.getProduct().getPrice(), fontRegular, 10));
            itemsTable.addCell(createContentCell(String.valueOf(order.getQuantity()), fontRegular, 10));
            itemsTable.addCell(createContentCell("₹" + (order.getTotalPrice() * 0.18), fontRegular, 10)); // Assuming 18% tax
            itemsTable.addCell(createContentCell("₹" + order.getTotalPrice(), fontBold, 10));

            // Summary table
            Table summaryTable = new Table(new float[]{3, 2})
                    .setWidth(UnitValue.createPercentValue(50))
                    .setHorizontalAlignment(HorizontalAlignment.RIGHT)
                    .setMarginBottom(30);

            summaryTable.addCell(createSummaryCell("Subtotal", fontBold, 10, false));
            summaryTable.addCell(createSummaryCell("₹" + (order.getTotalPrice() / 1.18), fontRegular, 10, true));
            
            summaryTable.addCell(createSummaryCell("Tax (18%)", fontBold, 10, false));
            summaryTable.addCell(createSummaryCell("₹" + (order.getTotalPrice() * 0.18), fontRegular, 10, true));
            
            summaryTable.addCell(createSummaryCell("Shipping", fontBold, 10, false));
            summaryTable.addCell(createSummaryCell("₹0.00", fontRegular, 10, true));
            
            summaryTable.addCell(createSummaryCell("Total", fontBold, 12, false));
            summaryTable.addCell(createSummaryCell("₹" + order.getTotalPrice(), fontBold, 12, true));

            // Footer
            Div footer = new Div()
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFont(fontRegular)
                    .setFontSize(10)
                    .setFontColor(DARK_GRAY)
                    .setMarginTop(30)
                    .setPaddingTop(20)
                    .setBorderTop(new SolidBorder(LIGHT_GRAY, 1));

            footer.add(new Paragraph("Thank you for your business!"));
            footer.add(new Paragraph("Terms & Conditions: Payment due within 7 days"));

            // Build the document
            document.add(header);
            document.add(invoiceTitle);
            document.add(invoiceMetaTable);
            document.add(customerTable);
            document.add(itemsTable);
            document.add(summaryTable);
            document.add(footer);

            document.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error generating PDF: " + e.getMessage());
        }

        ByteArrayInputStream bis = new ByteArrayInputStream(out.toByteArray());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=invoice_" + id + ".pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }

    private Cell createMetaCell(String label, String value, PdfFont boldFont, PdfFont regularFont) {
        return new Cell()
                .add(new Paragraph(label)
                        .setFont(boldFont)
                        .setFontSize(10))
                .add(new Paragraph(value)
                        .setFont(regularFont)
                        .setFontSize(10))
                .setBorder(Border.NO_BORDER)
                .setPadding(5);
    }

    private Cell createCustomerCell(String label, String value, PdfFont boldFont, PdfFont regularFont) {
        return new Cell()
                .add(new Paragraph(label + ":")
                        .setFont(boldFont)
                        .setFontSize(10))
                .add(new Paragraph(value != null ? value : "N/A")
                        .setFont(regularFont)
                        .setFontSize(10))
                .setBorder(Border.NO_BORDER)
                .setPadding(5);
    }

    private Cell createHeaderCell(String text, Color bgColor, PdfFont font, float fontSize) {
        return new Cell()
                .add(new Paragraph(text)
                        .setFont(font)
                        .setFontSize(fontSize)
                        .setFontColor(DeviceRgb.WHITE))
                .setBackgroundColor(bgColor)
                .setPadding(8)
                .setTextAlignment(TextAlignment.CENTER);
    }

    private Cell createContentCell(String text, PdfFont font, float fontSize) {
        return new Cell()
                .add(new Paragraph(text)
                        .setFont(font)
                        .setFontSize(fontSize))
                .setPadding(8)
                .setBorderBottom(new SolidBorder(LIGHT_GRAY, 1));
    }

    private Cell createSummaryCell(String text, PdfFont font, float fontSize, boolean alignRight) {
        Cell cell = new Cell()
                .add(new Paragraph(text)
                        .setFont(font)
                        .setFontSize(fontSize))
                .setPadding(5)
                .setBorder(Border.NO_BORDER);

        if (alignRight) {
            cell.setTextAlignment(TextAlignment.RIGHT);
        }

        return cell;
    }
}