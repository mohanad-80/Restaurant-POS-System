package com.konecta.internship.Restaurant_POS_System.payment.services;


import com.konecta.internship.Restaurant_POS_System.orders.entity.Order;
import com.konecta.internship.Restaurant_POS_System.orders.exceptions.OrderNotFoundException;
import com.konecta.internship.Restaurant_POS_System.orders.repositories.OrderRepository;
import com.konecta.internship.Restaurant_POS_System.orders.services.OrderService;
import com.konecta.internship.Restaurant_POS_System.payment.dto.OrderWithPaymentRequestDTO;
import com.konecta.internship.Restaurant_POS_System.payment.dto.OrderWithPaymentResponse;
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

}
