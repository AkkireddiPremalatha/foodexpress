package com.menu.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.menu.dto.CartDTO;
import com.menu.dto.MenuItemDTO;
import com.menu.dto.RestaurantDTO;
import com.menu.enums.Category;
import com.menu.service.CustomerMenuService;

//Swagger/OpenAPI Imports
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema; // For list responses
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin("*")
@RequiredArgsConstructor
@Tag(name = "Customer Menu & Restaurant Operations", description = "APIs for customers to browse restaurants, menus, and search for items.")
public class CustomerMenuController {

	private final CustomerMenuService customerMenuService;

	// GET /api/v1/restaurants/nearby?pinCode={userPinCode}
	// ............................... nearby restaurants by pin code........................
	@Operation(summary = "Get nearby restaurants by pin code", description = "Retrieves a list of restaurants available in a specific pin code area.", responses = {
			@ApiResponse(responseCode = "200", description = "Successfully retrieved list of nearby restaurants", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = RestaurantDTO.class)))),
			@ApiResponse(responseCode = "404", description = "No restaurants found for the provided pin code", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
			}
	)
	@GetMapping("customer/restaurants/nearby")
	public ResponseEntity<List<RestaurantDTO>> getNearbyRestaurants(
			@Parameter(description = "Get restaurants based on the customer's pincode by using customer ID", required = true, example = "10") @RequestParam Long customerId) {
		List<RestaurantDTO> restaurants = customerMenuService.getNearbyRestaurants(customerId);
		return new ResponseEntity<>(restaurants, HttpStatus.OK);
	}

	// GET /api/v1/restaurants/{restaurantId}
	//.................................. Individual restaurant and its details......................
	@Operation(summary = "Get restaurant details by ID", description = "Retrieves detailed information for a specific restaurant using its unique ID.", responses = {
			@ApiResponse(responseCode = "200", description = "Successfully retrieved restaurant details", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestaurantDTO.class))),
			@ApiResponse(responseCode = "404", description = "Restaurant not found for the given ID", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))) })
	@GetMapping("customer/restaurants/{restaurantId}")
	public ResponseEntity<RestaurantDTO> getRestaurantById(
			@Parameter(description = "ID of the restaurant to retrieve", required = true, example = "1") @PathVariable Long restaurantId) {
		RestaurantDTO restaurant = customerMenuService.getRestaurantById(restaurantId);
		return new ResponseEntity<>(restaurant, HttpStatus.OK);
	}

	// GET /api/v1/menu/{restaurantId}
	// ................................... menu of individual restaurant...............
	@Operation(summary = "Get menu for a specific restaurant", description = "Retrieves all available menu items for a given restaurant ID.", responses = {
			@ApiResponse(responseCode = "200", description = "Successfully retrieved menu items for the restaurant", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = MenuItemDTO.class)))),
			@ApiResponse(responseCode = "404", description = "Restaurant not found or no menu items available for the restaurant", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))) })
	@GetMapping("customer/menu/{restaurantId}")
	public ResponseEntity<List<MenuItemDTO>> getRestaurantMenu(
			@Parameter(description = "ID of the restaurant to get the menu from", required = true, example = "1") @PathVariable Long restaurantId) {
		List<MenuItemDTO> menu = customerMenuService.getRestaurantMenu(restaurantId);
		return new ResponseEntity<>(menu, HttpStatus.OK);
	}

	// GET /api/v1/menu/search?query={keyword}
	// .................................... search menu in particular pin code...............
	@Operation(summary = "Search menu items across all restaurants in a particular pincode", description = "Searches for menu items whose names contain the provided keyword, across all the restaurants in a pincode.", responses = {
			@ApiResponse(responseCode = "200", description = "Successfully retrieved matching menu items", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = MenuItemDTO.class)))),
			@ApiResponse(responseCode = "400", description = "Invalid input (e.g., empty search query)", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = "404", description = "No menu items found matching the search query", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))) })
	@GetMapping("customer/menu/search")
	public ResponseEntity<List<MenuItemDTO>> searchMenuItems(
			@Parameter(description = "Keyword to search for in menu item names", required = true, example = "Biryani")
			@RequestParam String query,
			@Parameter(description = "The 6-digit pin code to search for menu items within", required = true, example = "600001")
            @RequestParam Long customerId) {
		List<MenuItemDTO> menuItems = customerMenuService.searchMenuItems(query, customerId);

		return new ResponseEntity<>(menuItems, HttpStatus.OK);
	}
	
	// GET /api/v1/menu/filter?restaurantId={restaurantId}&category={categoryName}
	// ......................................by restaurant and by category......................
	@Operation(summary = "Filter menu items by category for a specific restaurant", description = "Retrieves menu items belonging to a specific category within a given restaurant.", responses = {
			@ApiResponse(responseCode = "200", description = "Successfully retrieved filtered menu items", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = MenuItemDTO.class)))),
			@ApiResponse(responseCode = "400", description = "Invalid input (e.g., empty category name)", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = "404", description = "Restaurant not found or no menu items found for the specified category in that restaurant", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))) })
	@GetMapping("customer/menu/filter")
	public ResponseEntity<List<MenuItemDTO>> getMenuItemsByCategory(
			@Parameter(description = "ID of the restaurant", required = true, example = "1") @RequestParam Long restaurantId,
			@Parameter(description = "Name of the category to filter by (e.g., Veg/Non-Veg)", required = true) @RequestParam Category category) {
		List<MenuItemDTO> menuItems = customerMenuService.getMenuItemsByCategory(restaurantId, category);

		return new ResponseEntity<>(menuItems, HttpStatus.OK);
	}
	
	@PostMapping("/customer/addItems")
	public String addItems(CartDTO cartDTO) {
		return customerMenuService.addItems(cartDTO);
	}
	
	@DeleteMapping("/customer/removeItems")
	public void deleteItems(Long customerId,Long menuItemId) {
		customerMenuService.deleteItems(customerId, menuItemId);		
	}
}
