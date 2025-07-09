package com.menu.service;

import java.io.IOException;


import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.menu.dto.AddMenuItemRequest;
import com.menu.dto.MenuItemDTO;
import com.menu.dto.UpdateAvailabilityRequest;
import com.menu.dto.UpdateMenuItemRequest;
import com.menu.entity.MenuItem;
import com.menu.entity.Restaurant;
import com.menu.exception.ResourceNotFoundException;
import com.menu.repository.MenuItemRepository;
import com.menu.repository.RestaurantRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class RestaurantMenuServiceImpl implements RestaurantMenuService {

	private final MenuItemRepository menuItemRepository;
	private final RestaurantRepository restaurantRepository;
//	private final CategoryRepository categoryRepository;

	private MenuItemDTO mapToMenuItemDTO(MenuItem menuItem) {
		if (menuItem == null) {
			log.warn("Attempted to map a null MenuItem entity to DTO.");
			return null;
		}
		log.debug("Mapping MenuItem entity with ID {} to DTO.", menuItem.getMenuItemId());


		return new MenuItemDTO(
				menuItem.getMenuItemId(), 
				menuItem.getRestaurant().getId(), 
				menuItem.getItemName(),
				menuItem.getItemDescription(), 
				menuItem.getPrice(),
				menuItem.getCategory(),
				menuItem.isAvailable(),
				
                menuItem.getItemImage()
		);
	}

	@Override
	public MenuItemDTO addMenuItem(AddMenuItemRequest request, MultipartFile itemImage) {
		log.info("Attempting to add new menu item: '{}' for restaurant ID: {}.",
				request.getItemName(), request.getRestaurantId());

		// 1. Validate Restaurant Existence
		Restaurant restaurant = restaurantRepository.findById(request.getRestaurantId()).orElseThrow(() -> {
			log.error("Restaurant not found with ID: {}. Cannot add menu item.", request.getRestaurantId());
			return new ResourceNotFoundException("Restaurant not found with ID: " + request.getRestaurantId());
		});


		MenuItem menuItem = new MenuItem();
		menuItem.setRestaurant(restaurant);
		menuItem.setCategory(request.getCategoryName());
		menuItem.setItemName(request.getItemName());
		menuItem.setItemDescription(request.getItemDescription());
		menuItem.setPrice(request.getPrice());

		menuItem.setAvailable(request.isAvailable());

		
		try {
			menuItem.setItemImage(itemImage.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
		menuItem.setCreatedBy("RestaurantOwner");

		log.debug("Mapping AddMenuItemRequest to MenuItem entity for item: '{}'.", request.getItemName());

		MenuItem savedMenuItem = menuItemRepository.save(menuItem);
		
		log.info("Menu item '{}' (ID: {}) added successfully to restaurant ID: {}.", savedMenuItem.getItemName(),
				savedMenuItem.getMenuItemId(), restaurant.getId());
		
		return mapToMenuItemDTO(savedMenuItem);
	}

	@Override
	public MenuItemDTO updateMenuItem(Long itemId, UpdateMenuItemRequest request, MultipartFile itemImage) {
		log.info("Attempting to update menu item with ID: {} for new category.", itemId);
		// Check if MenuItem exists
		MenuItem existingMenuItem = menuItemRepository.findById(itemId).orElseThrow(() -> {
			log.error("Menu Item not found with ID: {}. Cannot update.", itemId);
			return new ResourceNotFoundException("Menu Item not found with ID: " + itemId);
		});
		log.debug("Existing menu item with ID {} found. Item Name: '{}'.", itemId, existingMenuItem.getItemName());



		existingMenuItem.setItemName(request.getItemName());
		existingMenuItem.setItemDescription(request.getItemDescription());
		existingMenuItem.setPrice(request.getPrice());
		existingMenuItem.setAvailable(request.getIsAvailable());
	

		try {
			existingMenuItem.setItemImage(itemImage.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		} 
		existingMenuItem.setUpdatedBy("RestaurantOwner"); // Restaurant owner name should be dynamic
		log.debug("Updating fields for menu item ID: {}.", itemId);

		MenuItem updatedMenuItem = menuItemRepository.save(existingMenuItem);
		log.info("Menu item with ID {} updated successfully. New name: '{}'.", updatedMenuItem.getMenuItemId(),
				updatedMenuItem.getItemName());
		return mapToMenuItemDTO(updatedMenuItem);
	}

	@Override
	public void deleteMenuItem(Long itemId) {
		log.info("Attempting to delete menu item with ID: {}.", itemId);
		// Check if MenuItem exists before deleting
		if (!menuItemRepository.existsById(itemId)) {
			log.error("Menu Item not found with ID: {}. Cannot delete.", itemId);
			throw new ResourceNotFoundException("Menu Item not found with ID: " + itemId);
		}
		menuItemRepository.deleteById(itemId);
		log.info("Menu item with ID {} deleted successfully.", itemId);
	}

	@Override
	public MenuItemDTO updateMenuItemAvailability(Long itemId, UpdateAvailabilityRequest request) {
		log.info("Attempting to update availability for menu item with ID: {} to: {}.", itemId, request.isAvailable());
		MenuItem existingMenuItem = menuItemRepository.findById(itemId).orElseThrow(() -> {
			log.error("Menu Item not found with ID: {}. Cannot update availability.", itemId);
			return new ResourceNotFoundException("Menu Item not found with ID: " + itemId);
		});
		log.debug("Existing menu item with ID {} found. Current availability: {}.", itemId,
				existingMenuItem.isAvailable());

		existingMenuItem.setAvailable(request.isAvailable());

		existingMenuItem.setUpdatedBy("RestaurantOwner"); // Restaurant owner name should be dynamic
		log.debug("Setting availability of item ID {} to {}.", itemId, request.isAvailable());

		MenuItem updatedMenuItem = menuItemRepository.save(existingMenuItem);
		log.info("Menu item with ID {} availability updated to {}.", updatedMenuItem.getMenuItemId(),
				updatedMenuItem.isAvailable());
		return mapToMenuItemDTO(updatedMenuItem);
	}

	@Override
	public MenuItemDTO getMenuItemById(Long itemId) {
		log.info("Fetching menu item with ID: {}.", itemId);
		MenuItem menuItem = menuItemRepository.findById(itemId).orElseThrow(() -> {
			log.error("Menu Item not found with ID: {}.", itemId);
			return new ResourceNotFoundException("Menu Item not found with ID: " + itemId);
		});
		log.info("Menu item with ID {} fetched successfully. Name: '{}'.", menuItem.getMenuItemId(), menuItem.getItemName());
		return mapToMenuItemDTO(menuItem);
	}

}
// 2. Validate Category Existence
//Category category = categoryRepository.findById(request.getCategoryId()).orElseThrow(() -> {
//	log.error("Category not found with ID: {}. Cannot add menu item.", request.getCategoryId());
//	return new ResourceNotFoundException("Category not found with ID: " + request.getCategoryId());
//	// InvalidInputException
//});
//how to get in the form of image ??
//have to check with the variable name
// Set Audit Fields (created_by) - this would come from the authenticated user
// Validate Category Existence (to upadte categoryId)
//Category categoryToUpdate = categoryRepository.findById(request.getCategoryId()).orElseThrow(() -> {
//	log.error("Category not found with ID: {}. Cannot update menu item.", request.getCategoryId());
//	return new ResourceNotFoundException("Category not found with ID: " + request.getCategoryId());
//	// InvalidInputException
//});

//log.debug("Category with ID {} found for update.", request.getCategoryId());
