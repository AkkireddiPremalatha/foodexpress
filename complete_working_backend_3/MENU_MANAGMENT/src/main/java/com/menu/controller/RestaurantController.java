package com.menu.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.menu.dto.RestaurantDTO;
import com.menu.entity.Restaurant;
import com.menu.enums.Status;
import com.menu.service.RestaurantService;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1") 
@RequiredArgsConstructor
@CrossOrigin("*")
public class RestaurantController {

    private final RestaurantService restaurantService;

    // POST /api/v1/restaurants
    @PostMapping("/communication")
    public ResponseEntity<RestaurantDTO> createRestaurant(@Valid @RequestBody RestaurantDTO restaurantDTO) {
        RestaurantDTO createdRestaurant = restaurantService.createRestaurant(restaurantDTO);
        return new ResponseEntity<>(createdRestaurant, HttpStatus.CREATED);
    }

    //  GET /api/v1/restaurants/{restaurantId} is already in CustomerMenuController
    // GET /api/v1/restaurants/nearby?pinCode={userPinCode} is also in CustomerMenuController
    
    @PutMapping("/restaurants/updateRestaurantStatus/{userId}/{status}")
    public ResponseEntity<String> updateRestaurantStatus(@PathVariable Long userId, @PathVariable boolean status) {
    	return ResponseEntity.ok(restaurantService.updateRestaurantStatus(userId, status));
    }
    
    @GetMapping("/communication/getRestaurantStatus/{restaurantId}")
    public boolean getRestaurantStatus(@PathVariable int  restaurantId) {
    	return restaurantService.getRestaurantStatus(restaurantId);
    }
    
	@GetMapping("/restaurants/{userId}")
	public ResponseEntity<Restaurant> getRestaurantById(
			@Parameter(description = "User ID of the restaurant to retrieve", required = true) @PathVariable Long userId) {
		ResponseEntity<Restaurant> restaurant = restaurantService.getRestaurantById(userId);
		return restaurant;
	}
	
	@PutMapping("/restaurants/update/{status}")
	public String updateOrderStatus(Long orderId,Status status) {
		return restaurantService.updateOrderStatus(orderId, status);
	}
   
}
