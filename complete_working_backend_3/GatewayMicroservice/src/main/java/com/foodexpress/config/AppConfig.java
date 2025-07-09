package com.foodexpress.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
	@Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
            .route("user-management-service", r -> r.path("/api/v1/auth/**")
                .uri("lb://user-management-service"))  
            .route("menu-management-service", r -> r.path("/api/v1/menu/**","/api/v1/restaurants/**","/api/v1/**","/api/v1/categories/**")
                .uri("lb://menu-management-service"))  
            .route("delivery-management-service", r -> r.path("/api/v1/deliveryAgent/**")
                    .uri("lb://delivery-management-service"))  
            .route("order-management-service", r -> r.path("/api/v1/cart","api/v1/order")
                    .uri("lb://order-management-service"))  
            .route("payment-management-service", r -> r.path("/api/v1/customer/payment")
                    .uri("lb://payment-management-service")) 
            
            .build();
    }
}			
