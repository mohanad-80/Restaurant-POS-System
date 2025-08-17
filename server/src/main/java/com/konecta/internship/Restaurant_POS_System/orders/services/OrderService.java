package com.konecta.internship.Restaurant_POS_System.orders.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.konecta.internship.Restaurant_POS_System.MenuItem.MenuItemService;
import com.konecta.internship.Restaurant_POS_System.orders.dto.OrderItemDTO;
import com.konecta.internship.Restaurant_POS_System.orders.dto.OrderRequestDTO;
import com.konecta.internship.Restaurant_POS_System.orders.dto.UpdateOrderDTO;
import com.konecta.internship.Restaurant_POS_System.orders.dto.UpdateOrderItemDTO;
import com.konecta.internship.Restaurant_POS_System.orders.entity.Order;
import com.konecta.internship.Restaurant_POS_System.orders.entity.OrderItem;
import com.konecta.internship.Restaurant_POS_System.orders.enums.OrderItemStatus;
import com.konecta.internship.Restaurant_POS_System.orders.enums.OrderStatus;
import com.konecta.internship.Restaurant_POS_System.orders.exceptions.OrderItemNotFoundException;
import com.konecta.internship.Restaurant_POS_System.orders.exceptions.OrderNotFoundException;
import com.konecta.internship.Restaurant_POS_System.orders.repositories.OrderItemRepository;
import com.konecta.internship.Restaurant_POS_System.orders.repositories.OrderRepository;

@Service
public class OrderService {

  private final OrderRepository orderRepository;
  private final OrderItemRepository orderItemRepository;
  private final MenuItemService menuItemService;

  public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository, MenuItemService menuItemService) {
    this.orderRepository = orderRepository;
    this.orderItemRepository = orderItemRepository;
    this.menuItemService = menuItemService;
  }

  public List<Order> getAllOrders() {
    return orderRepository.findAll();
  }

  public Order getOrderById(Long id) {
    return orderRepository.findById(id)
        .orElseThrow(() -> new OrderNotFoundException("Order with ID " + id + " not found."));
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
      OrderItem item = buildOrderItemFromDTO(itemDto, order);
      total = total.add(item.getTotalPrice());
      items.add(item);
    }

    order.setItems(items);
    order.setTotalAmount(total);
    order.setDiscount(BigDecimal.ZERO);
    order.setTaxAmount(calculateTax(total));

    return orderRepository.save(order);
  }

  private OrderItem buildOrderItemFromDTO(OrderItemDTO dto, Order order) {
    OrderItem item = new OrderItem();
    item.setOrder(order);
    item.setMenuItemId(dto.getMenuItemId());
    item.setQuantity(dto.getQuantity());

    BigDecimal unitPrice = menuItemService.getMenuItemPrice(dto.getMenuItemId());
    item.setUnitPrice(unitPrice);
    item.setTotalPrice(unitPrice.multiply(BigDecimal.valueOf(dto.getQuantity())));
    item.setStatus(OrderItemStatus.PENDING);
    item.setNotes(dto.getNotes());

    return item;
  }

  private BigDecimal calculateTax(BigDecimal total) {
    return total.multiply(BigDecimal.valueOf(0.14)); // 14%
  }

  public Order updateOrder(Long id, UpdateOrderDTO dto) {
    Order order = this.getOrderById(id);

    if (dto.getTableId() != null) {
      order.setTableId(dto.getTableId());
    }
    if (dto.getDiscount() != null) {
      order.setDiscount(dto.getDiscount());
    }
    if (dto.getStatus() != null) {
      order.setStatus(dto.getStatus());
    }

    return orderRepository.save(order);
  }

  public void deleteOrderById(Long id) {
    orderRepository.deleteById(id);
  }

  public Order addOrderItemToOrder(Long id, OrderItemDTO dto) {
    Order order = this.getOrderById(id);
    List<OrderItem> items = order.getItems();
    BigDecimal total = order.getTotalAmount();

    OrderItem item = buildOrderItemFromDTO(dto, order);
    total = total.add(item.getTotalPrice());
    items.add(item);

    order.setItems(items);
    order.setTotalAmount(total);
    order.setTaxAmount(calculateTax(total));

    return orderRepository.save(order);
  }

  public OrderItem updateOrderItem(Long id, UpdateOrderItemDTO dto) {
    OrderItem item = orderItemRepository.findById(id)
        .orElseThrow(() -> new OrderItemNotFoundException(
            "Order item with ID " + id + " not found."));

    // Case 1: quantity change (with or without notes)
    if (dto.getQuantity() != null) {
      item.setQuantity(dto.getQuantity());

      BigDecimal unitPrice = item.getUnitPrice();
      item.setTotalPrice(unitPrice.multiply(BigDecimal.valueOf(dto.getQuantity())));

      Order order = item.getOrder();
      BigDecimal newOrderTotal = order.getItems().stream()
          .map(OrderItem::getTotalPrice)
          .reduce(BigDecimal.ZERO, BigDecimal::add);

      order.setTotalAmount(newOrderTotal);
      order.setTaxAmount(calculateTax(newOrderTotal));

      if (dto.getNotes() != null) {
        item.setNotes(dto.getNotes());
      }

      orderRepository.save(order);
    } else if (dto.getNotes() != null) {
      // Case 2: only notes changed
      item.setNotes(dto.getNotes());
      orderItemRepository.save(item);
    }

    return item;
  }

  public void deleteOrderItem(Long itemId) {
    OrderItem item = orderItemRepository.findById(itemId).orElse(null);
    if (item == null) {
      return;
    }

    Order order = item.getOrder();

    order.getItems().remove(item);

    BigDecimal newOrderTotal = order.getItems().stream()
        .map(OrderItem::getTotalPrice)
        .reduce(BigDecimal.ZERO, BigDecimal::add);

    order.setTotalAmount(newOrderTotal);
    order.setTaxAmount(calculateTax(newOrderTotal));

    orderItemRepository.delete(item);

    orderRepository.save(order);
  }
}
