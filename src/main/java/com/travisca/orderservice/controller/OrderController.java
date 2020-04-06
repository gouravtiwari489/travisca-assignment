package com.travisca.orderservice.controller;

import com.travisca.orderservice.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/orders")
public class OrderController {

  @Autowired
  private OrderService orderService;

  @PostMapping
  public ResponseEntity<?> createOrder(@Valid @RequestBody List<String> orders) {
    log.info("orders request {}", orders);
    try {
      Map<String, Double> response = orderService.process(orders);
      log.info("order service response {}", response);
      return new ResponseEntity<>(response, HttpStatus.CREATED);
    } catch (Exception e) {
      log.error("Failed to create order", e.getMessage());
      return new ResponseEntity<>("Error creating order ", HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

}
