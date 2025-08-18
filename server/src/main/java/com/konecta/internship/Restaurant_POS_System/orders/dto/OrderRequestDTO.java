package com.konecta.internship.Restaurant_POS_System.orders.dto;

import java.util.List;

import com.konecta.internship.Restaurant_POS_System.payment.enums.PaymentMethod;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderRequestDTO {
    private Long tableId; // nullable for takeout

    @NotNull(message = "staffId is required")
    private Long staffId;

    @NotEmpty(message = "Order must have at least one item")
    @Valid
    private List<OrderItemDTO> items;

    @NotNull(message = "order must have a payment method ")
    @Valid
    private PaymentMethod method;
}
