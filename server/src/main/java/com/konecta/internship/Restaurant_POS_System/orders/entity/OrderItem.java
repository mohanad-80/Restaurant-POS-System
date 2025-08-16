package com.konecta.internship.Restaurant_POS_System.orders.entity;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.konecta.internship.Restaurant_POS_System.orders.enums.OrderItemStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "order_items")
@Data
public class OrderItem {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Integer quantity;

  @Column(name = "unit_price", precision = 10, scale = 2)
  private BigDecimal unitPrice;

  @Column(name = "total_price", precision = 12, scale = 2)
  private BigDecimal totalPrice;

  @Enumerated(EnumType.STRING)
  @Column(length = 20)
  private OrderItemStatus status;

  @Column(length = 255)
  private String notes;

  // Many-to-one with Orders
  @ManyToOne
  @JoinColumn(name = "order_id", nullable = false)
  @JsonBackReference
  private Order order;

  // Nullable foreign key for menu item (no entity yet)
  @Column(name = "menu_item_id", nullable = true)
  private Long menuItemId;
}
