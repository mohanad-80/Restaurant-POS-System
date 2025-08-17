package com.konecta.internship.Restaurant_POS_System.orders.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.konecta.internship.Restaurant_POS_System.orders.entity.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long>{

}
