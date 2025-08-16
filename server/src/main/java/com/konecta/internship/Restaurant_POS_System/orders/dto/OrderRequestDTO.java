package com.konecta.internship.Restaurant_POS_System.orders.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderRequestDTO {
  private Long tableId; // nullable for takeout

  @NotNull(message = "staffId is required")
  private Long staffId;

  @NotEmpty(message = "Order must have at least one item")
  @Valid
  private List<OrderItemDTO> items;
}
