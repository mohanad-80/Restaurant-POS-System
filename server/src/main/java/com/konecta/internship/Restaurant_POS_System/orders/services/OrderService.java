package com.konecta.internship.Restaurant_POS_System.orders.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.konecta.internship.Restaurant_POS_System.orders.dto.OrderItemDTO;
import com.konecta.internship.Restaurant_POS_System.orders.dto.OrderRequestDTO;
import com.konecta.internship.Restaurant_POS_System.orders.entity.Order;
import com.konecta.internship.Restaurant_POS_System.orders.entity.OrderItem;
import com.konecta.internship.Restaurant_POS_System.orders.enums.OrderItemStatus;
import com.konecta.internship.Restaurant_POS_System.orders.enums.OrderStatus;
import com.konecta.internship.Restaurant_POS_System.orders.repositories.OrderRepository;

@Service
public class OrderService {
  @Autowired
  private OrderRepository orderRepository;

  public List<Order> getAllOrders() {
    return orderRepository.findAll();
  }

  public Order createOrder(OrderRequestDTO dto) {
    Order order = new Order();

    order.setOrderNumber(UUID.randomUUID().toString());
    order.setCreatedAt(LocalDateTime.now());
    order.setStatus(OrderStatus.OPEN);
    order.setTableId(dto.getTableId());
    order.setStaffId(dto.getStaffId());

    BigDecimal total = BigDecimal.ZERO;
    List<OrderItem> items = new ArrayList<>();

    for (OrderItemDTO itemDto : dto.getItems()) {
      OrderItem item = new OrderItem();
      item.setOrder(order);
      item.setMenuItemId(itemDto.getMenuItemId());
      item.setQuantity(itemDto.getQuantity());

      // Need menu service to fetch the menu item price from the db
      // BigDecimal unitPrice = menuService.getMenuItemPrice(itemDto.getMenuItemId());
      BigDecimal unitPrice = BigDecimal.ONE; // placeholder
      item.setUnitPrice(unitPrice);
      item.setTotalPrice(unitPrice.multiply(BigDecimal.valueOf(itemDto.getQuantity())));
      item.setStatus(OrderItemStatus.PENDING);
      item.setNotes(itemDto.getNotes());

      total = total.add(item.getTotalPrice());
      items.add(item);
    }

    order.setItems(items);
    order.setTotalAmount(total);
    order.setDiscount(BigDecimal.ZERO);
    order.setTaxAmount(calculateTax(total));

    return orderRepository.save(order);
  }

  private BigDecimal calculateTax(BigDecimal total) {
    return total.multiply(BigDecimal.valueOf(0.14)); // 14%
  }
}
