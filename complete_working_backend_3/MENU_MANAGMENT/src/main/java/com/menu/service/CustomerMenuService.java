package com.menu.service;

import java.util.List;

import com.menu.dto.CartDTO;
import com.menu.dto.MenuItemDTO;
import com.menu.dto.RestaurantDTO;
import com.menu.enums.Category;
public interface CustomerMenuService {

	// list of nearby restaurants by pin code.
    List<RestaurantDTO> getNearbyRestaurants(Long customerId);

    // restaurant menu by restaurant id.
    List<MenuItemDTO> getRestaurantMenu(Long restaurantId);

    // search menu items
    List<MenuItemDTO> searchMenuItems(String query, Long customerId);

    // menu by category (for a specific restaurant)
    List<MenuItemDTO> getMenuItemsByCategory(Long restaurantId, Category categoryName);
    
    
//    MenuRequestDTO addItem(Long menuItemId,Long customerId);
    
    RestaurantDTO getRestaurantById(Long restaurantId);
    
    String addItems(CartDTO cartDTO);
    
    void deleteItems(Long customerId,Long menuItemId);
    
    // getRestaurantByName
}
