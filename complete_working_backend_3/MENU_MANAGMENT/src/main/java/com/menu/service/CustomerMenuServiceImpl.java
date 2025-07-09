package com.menu.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.menu.dto.CartDTO;
import com.menu.dto.CustomerResponseDto;
import com.menu.dto.MenuItemDTO;
import com.menu.dto.RestaurantDTO;
import com.menu.entity.MenuItem;
import com.menu.entity.Restaurant;
import com.menu.enums.Category;
import com.menu.exception.ResourceNotFoundException;
import com.menu.feign.CustomerServiceFeignClient;
import com.menu.feign.OrderServiceFeignClient;
import com.menu.repository.MenuItemRepository;
import com.menu.repository.RestaurantRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerMenuServiceImpl implements CustomerMenuService {

	private final RestaurantRepository restaurantRepository;
	private final MenuItemRepository menuItemRepository;
	private final CustomerServiceFeignClient customerServiceFeignClient;
	private final OrderServiceFeignClient orderServiceFeignClient;

	private RestaurantDTO mapToRestaurantDTO(Restaurant restaurant) {
		if (restaurant == null) {
			log.warn("Attempted to map a null Restaurant entity to DTO.");
			return null;
		}
		log.debug("Mapping Restaurant entity with ID {} to DTO.", restaurant.getId());
		return new RestaurantDTO(restaurant.getUserId(), restaurant.getId(), restaurant.getRestaurantName(),
				restaurant.getRestaurantLocation(),restaurant.getEmail(), restaurant.getRestaurantPin(), restaurant.getContactNumber());
	}

	private MenuItemDTO mapToMenuItemDTO(MenuItem menuItem) {
		if (menuItem == null) {
			log.warn("Attempted to map a null MenuItem entity to DTO.");
			return null;
		}
		log.debug("Mapping MenuItem entity with ID {} to DTO.", menuItem.getMenuItemId());

		return new MenuItemDTO(menuItem.getMenuItemId(), menuItem.getRestaurant().getId(), menuItem.getItemName(),
				menuItem.getItemDescription(), menuItem.getPrice(), menuItem.getCategory(),
				menuItem.isAvailable(), menuItem.getItemImage()

		);
	}

	@Override
	public List<RestaurantDTO> getNearbyRestaurants(Long customerId) {
		CustomerResponseDto customer = customerServiceFeignClient.getCustomerById(customerId);
		String customerPincode = customer.getCustomerPincode();
		List<Restaurant> restaurants = restaurantRepository.findByRestaurantPin(customerPincode);
		if (restaurants.isEmpty()) {
			log.warn("No restaurants found for pincode: {}", customerPincode);
			throw new ResourceNotFoundException("No restaurants found for pincode: " + customerPincode);
			// return List.of(); // Returns an empty list
		}
		log.info("Found {} restaurants for pincode: {}.", restaurants.size(), customerPincode);
		return restaurants.stream().map(this::mapToRestaurantDTO).collect(Collectors.toList());
	}

	@Override
	public List<MenuItemDTO> getRestaurantMenu(Long restaurantId) {
		log.info("Fetching menu for restaurant with ID: {}", restaurantId);
		if (!restaurantRepository.existsById(restaurantId)) {
			log.error("Restaurant not found with ID: {}. Throwing ResourceNotFoundException.", restaurantId);
			throw new ResourceNotFoundException("Restaurant not found with ID: " + restaurantId);
		}
		List<MenuItem> menuItems = menuItemRepository.findByRestaurant_Id(restaurantId);
		if (menuItems.isEmpty()) {
			log.warn("Restaurant with ID {} found, but no menu items available. Throwing ResourceNotFoundException.",
					restaurantId);
			throw new ResourceNotFoundException(
					"No menu items currently available for restaurant with ID: " + restaurantId);
			// If restaurant exists but there is no menu items, return empty list
			// return List.of();
		}
		log.info("Found {} menu items for restaurant with ID: {}.", menuItems.size(), restaurantId);
		return menuItems.stream().map(this::mapToMenuItemDTO).collect(Collectors.toList());
	}
//	@Override
//	public MenuRequestDTO addItem(Long menuItemId,Long customerId) {
//		MenuItem menuItem=menuItemRepository.findById(menuItemId).orElse(null);
//		if(menuItem!=null) {
//			CartDTO cartDTO=new CartDTO();
//			cartDTO.setCustomerId(customerId);
//			cartDTO.setMenuItemId(menuItemId);
//			cartDTO.setPrice(menuItem.getPrice());
//			orderServiceFeignClient.addItemToCart(cartDTO);
//		}
//		
		
	

	@Override
	public List<MenuItemDTO> searchMenuItems(String query, Long customerId) {
		log.info("Searching menu items for query: '{}' within pincode: '{}'.", query, customerId);
		if (query == null || query.trim().isEmpty()) {
			log.error("Search query is null or empty. Throwing IllegalArgumentException.");
			throw new IllegalArgumentException("Search query cannot be null or empty.");
		}
		List<RestaurantDTO> nearByRestaurants = getNearbyRestaurants(customerId);

		List<Long> restaurantIds = nearByRestaurants.stream().map(RestaurantDTO::getId) // Assuming RestaurantDTO has
																						// getId()
				.collect(Collectors.toList());
		List<MenuItem> menuItems = menuItemRepository.findByRestaurantIdInAndItemNameContainingIgnoreCase(restaurantIds,
				query);
		// i am searching across all restaurants here.
		if (menuItems.isEmpty()) {
			log.warn(
					"No menu items found matching the query: '{}' in the given pincode: '{}'. Throwing ResourceNotFoundException.",
					query);
			throw new ResourceNotFoundException(
					"No menu items found matching the query: '" + query + "' in pincode.");

		}
		log.info("Found {} menu items matching the query: '{}' in the given pincode.", menuItems.size(), query);
		return menuItems.stream().map(this::mapToMenuItemDTO).collect(Collectors.toList());
	}

	@Override
	public List<MenuItemDTO> getMenuItemsByCategory(Long restaurantId, Category categoryName) {
		log.info("Fetching menu items for restaurant ID: {} and category: '{}'.", restaurantId, categoryName);
		if (!restaurantRepository.existsById(restaurantId)) {
			log.error("Restaurant not found with ID: {}. Throwing ResourceNotFoundException.", restaurantId);
			throw new ResourceNotFoundException("Restaurant not found with ID: " + restaurantId);
		}
		// Ensure category name is not null/empty
		if (categoryName == null) {
			log.error("Category name is null or empty. Throwing IllegalArgumentException.");
			throw new IllegalArgumentException("Category name cannot be null.");
			// return List.of();
		}

		// filter by restaurant ID and category name
		List<MenuItem> menuItems = menuItemRepository.findByRestaurant_IdAndCategory(restaurantId, categoryName);

		if (menuItems.isEmpty()) {
			log.warn(
					"No menu items found for category '{}' in restaurant with ID {}. Throwing ResourceNotFoundException.",
					categoryName, restaurantId);
			throw new ResourceNotFoundException(
					"No menu items found for category '" + categoryName + "' in restaurant with ID: " + restaurantId);
			// return List.of();
		}
		log.info("Found {} menu items for category '{}' in restaurant with ID: {}.", menuItems.size(), categoryName,
				restaurantId);
		return menuItems.stream().map(this::mapToMenuItemDTO).collect(Collectors.toList());
	}

	@Override
	public RestaurantDTO getRestaurantById(Long restaurantId) {
		log.info("Fetching restaurant with ID: {}.", restaurantId);
		Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(() -> {
			log.error("Restaurant not found for ID: {}. Throwing ResourceNotFoundException.", restaurantId);
			return new ResourceNotFoundException("Restaurant not found with ID: " + restaurantId);
		});
		log.info("Restaurant with ID {} fetched successfully.", restaurantId);
		return mapToRestaurantDTO(restaurant);
	}
	
	@Override
	public String addItems(CartDTO cartDTO) {
		return orderServiceFeignClient.addItemToCart(cartDTO);
	}
	
	@Override
	public void deleteItems(Long customerId,Long menuItemId) {
		 orderServiceFeignClient.removeItemFromCart(customerId,menuItemId);
	}

}
