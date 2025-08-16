package com.konecta.internship.Restaurant_POS_System.orders.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.konecta.internship.Restaurant_POS_System.orders.dto.DeleteOrderItemResponse;
import com.konecta.internship.Restaurant_POS_System.orders.dto.DeleteOrderResponse;
import com.konecta.internship.Restaurant_POS_System.orders.dto.OrderItemDTO;
import com.konecta.internship.Restaurant_POS_System.orders.dto.OrderRequestDTO;
import com.konecta.internship.Restaurant_POS_System.orders.dto.UpdateOrderDTO;
import com.konecta.internship.Restaurant_POS_System.orders.dto.UpdateOrderItemDTO;
import com.konecta.internship.Restaurant_POS_System.orders.entity.Order;
import com.konecta.internship.Restaurant_POS_System.orders.entity.OrderItem;
import com.konecta.internship.Restaurant_POS_System.orders.services.OrderService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

  private final OrderService orderService;

  public OrderController(OrderService orderService) {
    this.orderService = orderService;
  }

  @GetMapping
  public ResponseEntity<List<Order>> getAllOrders() {
    return ResponseEntity.ok(orderService.getAllOrders());
  }

  @GetMapping("/{id}")
  public ResponseEntity<Order> getOrder(@PathVariable Long id) {
    return ResponseEntity.ok(orderService.getOrderById(id));
  }

  @PostMapping
  public ResponseEntity<Order> createOrder(@Valid @RequestBody OrderRequestDTO orderRequest) {
    Order createdOrder = orderService.createOrder(orderRequest);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
  }

  @PatchMapping("/{id}")
  public ResponseEntity<Order> updateOrder(@PathVariable Long id, @Valid @RequestBody UpdateOrderDTO updateOrder) {
    return ResponseEntity.ok(orderService.updateOrder(id, updateOrder));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<DeleteOrderResponse> deleteOrder(@PathVariable Long id) {
    orderService.deleteOrderById(id);
    return ResponseEntity.ok(new DeleteOrderResponse("Order with id " + id + " has been deleted successfully!"));
  }

  @PostMapping("/{id}/items")
  public ResponseEntity<Order> addOrderItemToOrder(@PathVariable Long id, @Valid @RequestBody OrderItemDTO itemDto) {
    return ResponseEntity.status(HttpStatus.CREATED).body(orderService.addOrderItemToOrder(id, itemDto));
  }

  @PatchMapping("/items/{itemId}")
  public ResponseEntity<OrderItem> updateOrderItemForAnOrder(@PathVariable Long itemId,
      @Valid @RequestBody UpdateOrderItemDTO itemDto) {
    return ResponseEntity.ok(orderService.updateOrderItem(itemId, itemDto));
  }

  @DeleteMapping("/items/{itemId}")
  public ResponseEntity<DeleteOrderItemResponse> deleteOrderItem(@PathVariable Long itemId) {
    orderService.deleteOrderItem(itemId);
    return ResponseEntity
        .ok(new DeleteOrderItemResponse("Order item with id " + itemId + " has been deleted successfully!"));
  }
}
