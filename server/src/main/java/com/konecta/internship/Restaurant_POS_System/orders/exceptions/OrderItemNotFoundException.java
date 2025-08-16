package com.konecta.internship.Restaurant_POS_System.orders.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class OrderItemNotFoundException extends RuntimeException {
  public OrderItemNotFoundException(String message) {
    super(message);
  }
}
