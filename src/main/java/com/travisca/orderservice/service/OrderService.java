package com.travisca.orderservice.service;

import com.travisca.orderservice.config.OrderServiceProperties;
import com.travisca.orderservice.domain.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class OrderService {

  @Autowired
  private OrderServiceProperties orderServiceProperties;
  @Autowired
  private ProductService productService;
  @Autowired
  private OrderValidator orderValidator;
  @Autowired
  private PriceCalculatorService priceCalculatorService;

  public Map<String, Double> process(final List<String> orders) throws Exception {
    Map<String, Double> orderPriceMap = new HashMap<>();
    List<Product> products = productService.createProductList();
    orderValidator.validateOrder(orders, products);
    orders
        .stream()
        .filter(s -> Objects.nonNull(s))
        .forEach(s -> priceCalculatorService.calculatePriceForEachOrder(s, orderPriceMap, products));
    return orderPriceMap;
  }

}
