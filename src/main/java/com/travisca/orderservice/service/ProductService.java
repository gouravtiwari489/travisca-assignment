package com.travisca.orderservice.service;

import com.travisca.orderservice.config.OrderServiceProperties;
import com.travisca.orderservice.domain.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.summingDouble;

@Service
public class ProductService {

  @Autowired
  private OrderServiceProperties orderServiceProperties;
  @Autowired
  private PriceCalculatorService priceCalculatorService;

  public List<Product> createProductList() {
    List<Product> products = new ArrayList<>();
    Product product1 = new Product();
    product1.setName("chai");
    product1.setIngredients(Arrays.asList("tea", "milk", "sugar", "water"));
    List<String> price = Objects.nonNull(product1.getIngredients()) ? product1.getIngredients() : new ArrayList<>();
    product1.setPrice(priceCalculatorService.calculatePriceForEachProduct(price));

    Product product2 = new Product();
    product2.setName("coffee");
    product2.setIngredients(Arrays.asList("coffee", "milk", "sugar", "water"));
    product2.setPrice(priceCalculatorService.calculatePriceForEachProduct(product2.getIngredients()));

    products.add(product1);
    products.add(product2);

    return products;
  }

}
