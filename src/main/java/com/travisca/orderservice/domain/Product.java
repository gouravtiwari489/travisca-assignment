package com.travisca.orderservice.domain;

import lombok.Data;

import java.util.List;

@Data
public class Product {

  private String name;
  private Double price;
  private List<String> ingredients;

}
