package com.travisca.orderservice.service;

import com.travisca.orderservice.domain.Product;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderValidator {

  public void validateOrder(List<String> orders, List<Product> products) throws Exception {

    boolean isValidOrder = false;
    for (Product p: products){
      for(String order: orders){

        String[] items = order.split(", -");
        if(items.length>1){
          order = items[0];
        }

        if(p.getName().equals(order)){
          isValidOrder=  true;
        }
      }
    }

    if(!isValidOrder){
      throw new Exception("No items in order are found in menu");
    }
  }
}


