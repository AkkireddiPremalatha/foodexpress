package com.menu.service;

import org.springframework.http.ResponseEntity;

import com.menu.dto.RestaurantDTO;
import com.menu.entity.Restaurant;
import com.menu.enums.Status;
public interface RestaurantService {

	RestaurantDTO createRestaurant(RestaurantDTO restaurantDTO);
    // already have getRestaurantById in CustomerMenuService
    // RestaurantDTO getRestaurantById(Long id);
	
	
    // List<RestaurantDTO> getAllRestaurants();
	
	
    // update/delete methods needed
	public String updateRestaurantStatus(Long userId,boolean status);
	
	public boolean getRestaurantStatus(int restaurantId);
	
	public ResponseEntity<Restaurant> getRestaurantById(Long userId);
	
	public String updateOrderStatus(Long orderId,Status status);
}
