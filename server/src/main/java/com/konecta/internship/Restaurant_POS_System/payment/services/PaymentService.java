package com.konecta.internship.Restaurant_POS_System.payment.services;


import com.konecta.internship.Restaurant_POS_System.orders.entity.Order;
import com.konecta.internship.Restaurant_POS_System.orders.entity.OrderItem;
import com.konecta.internship.Restaurant_POS_System.orders.exceptions.OrderNotFoundException;
import com.konecta.internship.Restaurant_POS_System.orders.repositories.OrderRepository;
import com.konecta.internship.Restaurant_POS_System.payment.entity.Payment;
import com.konecta.internship.Restaurant_POS_System.payment.enums.PaymentMethod;
import com.konecta.internship.Restaurant_POS_System.payment.repositories.PaymentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    @Transactional
    public Payment payForOrder(long orderId, PaymentMethod method) {

        Order order =orderRepository.findById(orderId).orElseThrow(
                () -> new OrderNotFoundException("Order with ID " + orderId + " not found.")
        );
        return payForOrder(order,method);
    }

    @Transactional
    public Payment payForOrder(Order order, PaymentMethod method){
        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setAmount(order.getTotalAmount());
        payment.setMethod(method);
        payment.setPaid_at(LocalDateTime.now());

        return paymentRepository.save(payment);
    }

    public byte[] generateReceiptPdf(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new OrderNotFoundException("Order with ID " + orderId + " not found.")
        );
        Payment payment = paymentRepository.findByOrder_Id(orderId).orElse(null);

        try (var baos = new java.io.ByteArrayOutputStream()) {
            com.lowagie.text.Document doc = new com.lowagie.text.Document();
            com.lowagie.text.pdf.PdfWriter.getInstance(doc, baos);
            doc.open();

            // --- Header ---
            var title = new com.lowagie.text.Paragraph("Restaurant POS - Receipt",
                    new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA, 16, com.lowagie.text.Font.BOLD));
            title.setAlignment(com.lowagie.text.Element.ALIGN_CENTER);
            doc.add(title);

            doc.add(new com.lowagie.text.Paragraph("Order #: " + order.getOrderNumber()));
            doc.add(new com.lowagie.text.Paragraph("Date: " + order.getCreatedAt()));
            if (order.getTable() != null) {
                doc.add(new com.lowagie.text.Paragraph("Table: " + order.getTable().getTableNumber()));
            }
            if (order.getStaff() != null) {
                doc.add(new com.lowagie.text.Paragraph("Staff: " + order.getStaff().getName()));
            }
            doc.add(new com.lowagie.text.Paragraph("Status: " + order.getStatus()));
            doc.add(com.lowagie.text.Chunk.NEWLINE);

            // --- Items table ---
            com.lowagie.text.pdf.PdfPTable table = new com.lowagie.text.pdf.PdfPTable(4);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{46, 14, 20, 20});
            table.addCell(boldCell("Item"));
            table.addCell(boldCell("Qty"));
            table.addCell(boldCell("Unit"));
            table.addCell(boldCell("Total"));

            if (order.getItems() != null) {
                for (OrderItem it : order.getItems()) {
                    String name = it.getMenuItem() != null ? it.getMenuItem().getName() : "-";
                    table.addCell(name);
                    table.addCell(String.valueOf(it.getQuantity()));
                    table.addCell(formatMoney(it.getUnitPrice()));
                    table.addCell(formatMoney(it.getTotalPrice()));
                }
            }
            doc.add(table);
            doc.add(com.lowagie.text.Chunk.NEWLINE);

            // --- Summary ---
            java.math.BigDecimal subtotal = order.getTotalAmount() != null ? order.getTotalAmount() : java.math.BigDecimal.ZERO;
            java.math.BigDecimal discount = order.getDiscount() != null ? order.getDiscount() : java.math.BigDecimal.ZERO;
            java.math.BigDecimal tax = order.getTaxAmount() != null ? order.getTaxAmount() : java.math.BigDecimal.ZERO;
            java.math.BigDecimal grand = subtotal.subtract(discount).add(tax);

            doc.add(new com.lowagie.text.Paragraph("Subtotal: " + formatMoney(subtotal)));
            doc.add(new com.lowagie.text.Paragraph("Discount: " + formatMoney(discount)));
            doc.add(new com.lowagie.text.Paragraph("Tax: " + formatMoney(tax)));
            var totalP = new com.lowagie.text.Paragraph("Total: " + formatMoney(grand),
                    new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA, 12, com.lowagie.text.Font.BOLD));
            doc.add(totalP);


            doc.add(com.lowagie.text.Chunk.NEWLINE);
            if (payment != null) {
                doc.add(new com.lowagie.text.Paragraph("Payment Method: " + payment.getMethod()));
                doc.add(new com.lowagie.text.Paragraph("Paid Amount: " + formatMoney(payment.getAmount())));
                doc.add(new com.lowagie.text.Paragraph("Paid At: " + payment.getPaid_at()));
            } else {
                doc.add(new com.lowagie.text.Paragraph("Payment: Not recorded"));
            }

            doc.add(com.lowagie.text.Chunk.NEWLINE);
            doc.add(new com.lowagie.text.Paragraph("Thank you for dining with us!"));

            doc.close();
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate receipt PDF for order " + orderId, e);
        }
    }

    private static com.lowagie.text.pdf.PdfPCell boldCell(String text) {
        var font = new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA, 10, com.lowagie.text.Font.BOLD);
        var cell = new com.lowagie.text.pdf.PdfPCell(new com.lowagie.text.Phrase(text, font));
        cell.setHorizontalAlignment(com.lowagie.text.Element.ALIGN_LEFT);
        return cell;
    }

    private static String formatMoney(java.math.BigDecimal v) {
        if (v == null) return "0.00";
        return v.setScale(2, java.math.RoundingMode.HALF_UP).toPlainString();
    }


}
