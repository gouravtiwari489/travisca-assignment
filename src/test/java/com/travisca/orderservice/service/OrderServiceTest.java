package com.travisca.orderservice.service;

import com.travisca.orderservice.config.Menu;
import com.travisca.orderservice.config.OrderServiceProperties;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(MockitoJUnitRunner.class)
public class OrderServiceTest {

  @Mock
  private Menu menu;
  @Mock
  private OrderServiceProperties orderServiceProperties;
  @InjectMocks
  private OrderService orderService;

  @Before
  public void setUp(){
    Mockito.when(orderServiceProperties.getMenu()).thenReturn(menu);
    Mockito.when(menu.getItems()).thenReturn(buildProduct());
  }

  @Test
  public void shouldCalculatePriceForValidInput() throws Exception {
    List<String> orders = Arrays.asList("coffee, -water, -sugar", "chai, -sugar, -milk", "coffee", "chai", "chai, -water, -sugar");
    Map<String, Double> response =  orderService.process(orders);
    Assert.assertEquals(new Double(12.5), response.get("chai"));
    Assert.assertEquals(new Double(11), response.get("coffee"));
  }

  @Test
  public void shouldThrowExceptionWhenNoMenuItemInOrder(){
    List<String> orders = Arrays.asList("BananaSmoothie");
    try {
      orderService.process(orders);
      Assert.fail();
    } catch (Exception e) {
      Assert.assertEquals("No items in order are found in menu", e.getMessage());
    }
  }

  @Test
  public void shouldCalculatePriceForValidItemWhenOrderhasBothAvailableAndNonAvailableItemsInOrder() throws Exception {
    List<String> orders = Arrays.asList("BananaSmoothie", "coffee");
    Map<String, Double> response = orderService.process(orders);
    Assert.assertEquals(new Double(6.0),response.get("coffee"));
    Assert.assertEquals(new Double(0.0),response.get("BananaSmoothie"));
  }

  @Test
  public void shouldThrowExcpetionWhenAllIngredientsRemovedFromMenuItemInOrder() throws Exception {
    List<String> orders = Arrays.asList("BananaSmoothie", "coffee");
    Map<String, Double> response = orderService.process(orders);
    Assert.assertEquals(new Double(6.0),response.get("coffee"));
    Assert.assertEquals(new Double(0.0),response.get("BananaSmoothie"));
  }

  private Map<String, Double> buildProduct() {
    Map<String, Double> map = new HashMap<>();
    map.put("tea", 3.0);
    map.put("coffee", 4.0);
    map.put("milk", 1.0);
    map.put("water", 0.5);
    map.put("sugar", 0.5);
    return map;
  }

}
