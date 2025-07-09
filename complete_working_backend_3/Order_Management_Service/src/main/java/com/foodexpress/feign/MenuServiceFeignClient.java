package com.foodexpress.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="menu-management-service")
public interface MenuServiceFeignClient {
    @GetMapping("/api/v1/communication/getRestaurantStatus/{restaurantId}")
    public boolean getRestaurantStatus(@PathVariable int  restaurantId);
    
 
}
