package com.konecta.internship.Restaurant_POS_System.orders.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.konecta.internship.Restaurant_POS_System.orders.dto.OrderRequestDTO;
import com.konecta.internship.Restaurant_POS_System.orders.entity.Order;
import com.konecta.internship.Restaurant_POS_System.orders.services.OrderService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/orders")
public class OrderController {
  @Autowired
  private OrderService orderService;

  @GetMapping
  public List<Order> getAllOrders(){
    return orderService.getAllOrders();
  }

  @GetMapping("/{id}")
  public Order getOrder(@PathVariable Long id){
    return orderService.getOrderById(id);
  }

  @PostMapping
  public Order createOrder(@Valid @RequestBody OrderRequestDTO orderRequest) {
    return orderService.createOrder(orderRequest);
  }
}
