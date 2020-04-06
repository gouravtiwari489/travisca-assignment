package com.travisca.orderservice.service;

import com.travisca.orderservice.config.OrderServiceProperties;
import com.travisca.orderservice.domain.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.summingDouble;

@Service
public class PriceCalculatorService {

  @Autowired
  private OrderServiceProperties orderServiceProperties;

  public Double calculatePriceForEachProduct(final List<String> ingredients) {
    return ingredients.size()>0? ingredients.stream().collect(summingDouble(this::findPricing)): 0.0;
  }

  public void calculatePriceForEachOrder(final String s, final Map<String, Double> orderPriceMap, List<Product> products) {
    String[] items = s.split(", -");
    Double amountTobeDeductedFromOriginal = 0.0;

    if(items.length>1){
      for (int i = 1; i < items.length; i++) {
        amountTobeDeductedFromOriginal = amountTobeDeductedFromOriginal + findPricing( items[i]);
      }
      Double finalAmount = findOriginalPriceOfItem(items[0], products) - amountTobeDeductedFromOriginal;
      if(!orderPriceMap.containsKey(items[0])){
        orderPriceMap.put(items[0], finalAmount);
      }else{
        Double amount =orderPriceMap.get(items[0]);
        Double amountUpdated  = amount + finalAmount;
        orderPriceMap.put(items[0], amountUpdated);
      }

    }else{
      if(!orderPriceMap.containsKey(s)){
        orderPriceMap.put(s, findOriginalPriceOfItem(s, products));
      }else{
        Double amount =orderPriceMap.get(s);
        Double amountUpdated  = amount + findOriginalPriceOfItem(s, products);
        orderPriceMap.put(s, amountUpdated);
      }

    }

  }

  private Double findOriginalPriceOfItem(final String item, List<Product> products) {
    Double price = 0.0;
    for (Product p: products){
      if (p.getName().equals(item)){
        price = p.getPrice();
      }
    }

    return price;
  }

  private Double findPricing(final String s) {
    return orderServiceProperties.getMenu().getItems().containsKey(s) ?
           orderServiceProperties.getMenu().getItems().get(s) :
           0.0;
  }
}
