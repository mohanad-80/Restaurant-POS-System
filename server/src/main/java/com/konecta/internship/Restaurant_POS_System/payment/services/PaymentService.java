package com.konecta.internship.Restaurant_POS_System.payment.services;


import com.konecta.internship.Restaurant_POS_System.orders.entity.Order;
import com.konecta.internship.Restaurant_POS_System.orders.repositories.OrderRepository;
import com.konecta.internship.Restaurant_POS_System.orders.services.OrderService;
import com.konecta.internship.Restaurant_POS_System.payment.dto.OrderWithPaymentRequestDTO;
import com.konecta.internship.Restaurant_POS_System.payment.dto.OrderWithPaymentResponse;
import com.konecta.internship.Restaurant_POS_System.payment.entity.Payment;
import com.konecta.internship.Restaurant_POS_System.payment.repositories.PaymentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class PaymentService {

    private final OrderService orderService;
    private final PaymentRepository paymentRepository;

    @Transactional
    public OrderWithPaymentResponse createOrderWithPayment(OrderWithPaymentRequestDTO dto) {

        Order order = orderService.createOrder(dto.getOrder());


        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setAmount(order.getTotalAmount()); // BigDecimal from order
        payment.setMethod(dto.getPaymentMethod());
        payment.setPaid_at(LocalDateTime.now());

        paymentRepository.save(payment);

        OrderWithPaymentResponse response = new OrderWithPaymentResponse();
        response.setOrder(order);
        response.setPayment(payment);

        return response;
    }

}
