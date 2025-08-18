package com.konecta.internship.Restaurant_POS_System.payment.controllers;



import com.konecta.internship.Restaurant_POS_System.payment.entity.Payment;
import com.konecta.internship.Restaurant_POS_System.payment.enums.PaymentMethod;
import com.konecta.internship.Restaurant_POS_System.payment.services.PaymentService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "http://127.0.0.1:3001", methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PATCH,
        RequestMethod.DELETE, RequestMethod.OPTIONS })
@AllArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/{id}")
    public ResponseEntity<Payment> payForOrder(@PathVariable Long id, @Valid @RequestBody PaymentMethod method) {
        Payment payment = paymentService.payForOrder(id,method);
        return ResponseEntity.status(HttpStatus.CREATED).body(payment);
    }

    @GetMapping("/{orderId}/receipt.pdf")
    public ResponseEntity<byte[]> downloadReceipt(@PathVariable Long orderId) {
        byte[] pdf = paymentService.generateReceiptPdf(orderId);

        var headers = new org.springframework.http.HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_PDF);
        headers.setContentDisposition(
                org.springframework.http.ContentDisposition
                        .attachment()
                        .filename("receipt-" + orderId + ".pdf")
                        .build()
        );

        return new ResponseEntity<>(pdf, headers, org.springframework.http.HttpStatus.OK);
    }

}
