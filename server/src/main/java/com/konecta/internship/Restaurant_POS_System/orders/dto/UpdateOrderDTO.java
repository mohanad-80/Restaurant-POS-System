package com.konecta.internship.Restaurant_POS_System.orders.dto;

import java.math.BigDecimal;

import com.konecta.internship.Restaurant_POS_System.orders.enums.OrderStatus;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class UpdateOrderDTO {
  @Positive(message = "tableId must be a positive value")
  private Long tableId;

  @DecimalMax(value = "1.0", message = "maximum value for discount is 1.0")
  @DecimalMin(value = "0.0", message = "minimum value for discount is 0.0")
  private BigDecimal discount;

  private OrderStatus status;
}
