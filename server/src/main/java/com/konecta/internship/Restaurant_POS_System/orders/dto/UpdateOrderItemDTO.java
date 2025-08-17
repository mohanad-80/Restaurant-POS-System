package com.konecta.internship.Restaurant_POS_System.orders.dto;

import com.konecta.internship.Restaurant_POS_System.orders.enums.OrderItemStatus;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateOrderItemDTO {
  @Min(value = 1, message = "Quantity must be at least 1")
  private Integer quantity;

  private String notes;

  private OrderItemStatus status;
}
