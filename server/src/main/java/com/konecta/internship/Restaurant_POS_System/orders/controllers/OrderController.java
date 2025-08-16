package com.konecta.internship.Restaurant_POS_System.orders.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/orders")
public class OrderController {
  @Autowired
  private OrderService orderService;

  @GetMapping
  public List<Order> getAllOrders() {
    return orderService.getAllOrders();
  }

  @GetMapping("/{id}")
  public Order getOrder(@PathVariable Long id) {
    return orderService.getOrderById(id);
  }

  @PostMapping
  public Order createOrder(@Valid @RequestBody OrderRequestDTO orderRequest) {
    return orderService.createOrder(orderRequest);
  }

  @PatchMapping("/{id}")
  public Order updateOrder(@PathVariable Long id, @Valid @RequestBody UpdateOrderDTO updateOrder) {
    return orderService.updateOrder(id, updateOrder);
  }

  @DeleteMapping("/{id}")
  public DeleteOrderResponse deleteOrder(@PathVariable Long id) {
    orderService.deleteOrderById(id);
    return new DeleteOrderResponse("Order with id " + id + " has been deleted successfully!");
  }

  @PostMapping("/{id}/items")
  public Order addOrderItemToOrder(@PathVariable Long id, @Valid @RequestBody OrderItemDTO itemDto) {
    return orderService.addOrderItemToOrder(id, itemDto);
  }

  @PatchMapping("/items/{itemId}")
  public OrderItem updateOrderItemForAnOrder(@PathVariable Long itemId,
      @Valid @RequestBody UpdateOrderItemDTO itemDto) {
    return orderService.updateOrderItem(itemId, itemDto);
  }

  @DeleteMapping("/items/{itemId}")
  public DeleteOrderItemResponse deleteOrderItem(@PathVariable Long itemId) {
    orderService.deleteOrderItem(itemId);
    return new DeleteOrderItemResponse("Order with id " + itemId + " has been deleted successfully!");
  }

}
