package com.konecta.internship.Restaurant_POS_System.payment.dto;


import com.konecta.internship.Restaurant_POS_System.orders.entity.Order;
import com.konecta.internship.Restaurant_POS_System.payment.entity.Payment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderWithPaymentResponse {

    private Order order;
    private Payment payment;
}
