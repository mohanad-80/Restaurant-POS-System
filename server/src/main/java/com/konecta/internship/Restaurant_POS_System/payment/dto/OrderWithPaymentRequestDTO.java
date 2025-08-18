package com.konecta.internship.Restaurant_POS_System.payment.dto;


import com.konecta.internship.Restaurant_POS_System.orders.dto.OrderRequestDTO;
import com.konecta.internship.Restaurant_POS_System.payment.enums.PaymentMethod;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OrderWithPaymentRequestDTO {
    private OrderRequestDTO order;
    private PaymentMethod paymentMethod;
}
