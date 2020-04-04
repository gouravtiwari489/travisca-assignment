package com.travisca.orderservice.config;

import lombok.Data;

import java.util.Map;

@Data
public class Menu {

  private Map<String, Double> items;

}
