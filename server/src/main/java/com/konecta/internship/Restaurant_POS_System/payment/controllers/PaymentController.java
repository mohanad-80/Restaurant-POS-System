package com.konecta.internship.Restaurant_POS_System.payment.controllers;


import com.konecta.internship.Restaurant_POS_System.orders.dto.OrderRequestDTO;

import com.konecta.internship.Restaurant_POS_System.payment.dto.OrderWithPaymentRequestDTO;
import com.konecta.internship.Restaurant_POS_System.payment.dto.OrderWithPaymentResponse;
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

    @PostMapping
    public ResponseEntity<OrderWithPaymentResponse> createOrder(@Valid @RequestBody OrderWithPaymentRequestDTO requestDTO) {
        OrderWithPaymentResponse response = paymentService.createOrderWithPayment(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
