package com.konecta.internship.Restaurant_POS_System.orders.services;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.konecta.internship.Restaurant_POS_System.orders.entity.Order;
import com.konecta.internship.Restaurant_POS_System.orders.entity.OrderItem;

@Service
public class OrderNotificationService {

  private final SimpMessagingTemplate messagingTemplate;

  public OrderNotificationService(SimpMessagingTemplate messagingTemplate) {
    this.messagingTemplate = messagingTemplate;
  }

  public void notifyOrder(Order order) {
    messagingTemplate.convertAndSend("/topic/orders", order);
  }

  public void notifyOrderItem(OrderItem item) {
    messagingTemplate.convertAndSend("/topic/order-items", item);
  }
}
