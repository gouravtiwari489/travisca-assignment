package com.travisca.orderservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Component
@Data
@ConfigurationProperties("order-service")
public class OrderServiceProperties {

  private Menu menu;


}
