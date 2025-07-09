package com.foodexpress.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.foodexpress.dto.RestaurantDTO;


@FeignClient(name="menu-management-service")
public interface MenuServiceFeignClient {
	
	@PostMapping("/api/v1/communication")
    public ResponseEntity<RestaurantDTO> createRestaurant(@RequestBody RestaurantDTO restaurantDTO);
	
	@GetMapping("/api/v1/restaurants/nearby")
	public List<RestaurantDTO> getNearbyRestaurants(Long customerId);

}
