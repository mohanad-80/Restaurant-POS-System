package com.konecta.internship.Restaurant_POS_System.orders.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.konecta.internship.Restaurant_POS_System.orders.enums.OrderStatus;

import com.konecta.internship.Restaurant_POS_System.table_management.entity.DiningTable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "orders")
@Getter
@Setter
public class Order {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "order_number", length = 50, unique = true, nullable = false)
  private String orderNumber;

  @Column(name = "total_amount", precision = 12, scale = 2)
  private BigDecimal totalAmount;

  @Column(name = "discount", precision = 10, scale = 2)
  private BigDecimal discount = BigDecimal.ZERO;

  @Column(name = "tax_amount", precision = 10, scale = 2)
  private BigDecimal taxAmount;

  @Enumerated(EnumType.STRING)
  @Column(length = 20)
  private OrderStatus status;

  @Column(name = "created_at")
  private LocalDateTime createdAt = LocalDateTime.now();

  // One-to-many with OrderItems
  @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
  @JsonManagedReference
  private List<OrderItem> items;

  @ManyToOne(fetch = FetchType.EAGER, cascade = {
          CascadeType.PERSIST,
          CascadeType.MERGE,
          CascadeType.DETACH,
          CascadeType.REFRESH
  })
  @JoinColumn(name = "table_id")
  private DiningTable table;

  @Column(name = "staff_id", nullable = true)
  private Long staffId;
}
