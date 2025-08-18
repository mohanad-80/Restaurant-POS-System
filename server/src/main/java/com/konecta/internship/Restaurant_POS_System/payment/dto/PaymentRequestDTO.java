package com.konecta.internship.Restaurant_POS_System.payment.dto;


import com.konecta.internship.Restaurant_POS_System.payment.enums.PaymentMethod;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentRequestDTO {
    @NotNull(message = "order must have a payment method ")
    @Valid
    PaymentMethod method;
}
