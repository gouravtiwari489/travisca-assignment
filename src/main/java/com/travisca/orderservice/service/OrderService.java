package com.travisca.orderservice.service;

import com.travisca.orderservice.config.OrderServiceProperties;
import com.travisca.orderservice.domain.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.util.stream.Collectors.summingDouble;

@Service
public class OrderService {

  @Autowired
  private OrderServiceProperties orderServiceProperties;
  private List<Product>          products;

  public Map<String, Double> process(final List<String> orders) throws Exception {
    Map<String, Double>    orderPriceMap = new HashMap<>();
    createProductList();
    validateOrder(orders);
    orders.stream().filter(s -> Objects.nonNull(s)).forEach(s -> calculatePriceForEachOrder(s, orderPriceMap));
    return orderPriceMap;
  }

  private void validateOrder(List<String> orders) throws Exception {
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

  private Double calculatePriceForEachProduct(final List<String> ingredients) {
    return ingredients.size()>0? ingredients.stream().collect(summingDouble(this::findPricing)): 0.0;
  }

  private Double findPricing(final String s) {
    return orderServiceProperties.getMenu().getItems().containsKey(s) ?
                    orderServiceProperties.getMenu().getItems().get(s) :
                    0.0;
  }

  private void calculatePriceForEachOrder(final String s, final Map<String, Double> orderPriceMap) {
    String[] items = s.split(", -");
    Double amountTobeDeductedFromOriginal = 0.0;

    if(items.length>1){
      for (int i = 1; i < items.length; i++) {
        amountTobeDeductedFromOriginal = amountTobeDeductedFromOriginal + findPricing( items[i]);
      }
      Double finalAmount = findOriginalPriceOfItem(items[0]) - amountTobeDeductedFromOriginal;
      if(!orderPriceMap.containsKey(items[0])){
        orderPriceMap.put(items[0], finalAmount);
      }else{
        Double amount =orderPriceMap.get(items[0]);
        Double amountUpdated  = amount + finalAmount;
        orderPriceMap.put(items[0], amountUpdated);
      }

    }else{
      if(!orderPriceMap.containsKey(s)){
        orderPriceMap.put(s, findOriginalPriceOfItem(s));
      }else{
        Double amount =orderPriceMap.get(s);
        Double amountUpdated  = amount + findOriginalPriceOfItem(s);
        orderPriceMap.put(s, amountUpdated);
      }

    }

  }

  private Double findOriginalPriceOfItem(final String item) {
    Double price = 0.0;
    for (Product p: products){
      if (p.getName().equals(item)){
        price = p.getPrice();
      }
    }

    return price;
  }

  public List<Product> createProductList() {
    products = new ArrayList<>();
    Product product1 = new Product();
    product1.setName("chai");
    product1.setIngredients(Arrays.asList("tea", "milk", "sugar", "water"));
    List<String> price = Objects.nonNull(product1.getIngredients()) ? product1.getIngredients() : new ArrayList<>();
    product1.setPrice(calculatePriceForEachProduct(price));

    Product product2 = new Product();
    product2.setName("coffee");
    product2.setIngredients(Arrays.asList("coffee", "milk", "sugar", "water"));
    product2.setPrice(calculatePriceForEachProduct(product2.getIngredients()));

    products.add(product1);
    products.add(product2);

    return products;
  }

}
