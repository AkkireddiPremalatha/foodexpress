package com.menu.service;

import org.springframework.web.multipart.MultipartFile;

import com.menu.dto.AddMenuItemRequest;
import com.menu.dto.MenuItemDTO;
import com.menu.dto.UpdateAvailabilityRequest;
import com.menu.dto.UpdateMenuItemRequest;

public interface RestaurantMenuService {
	
	
    MenuItemDTO addMenuItem(AddMenuItemRequest request, MultipartFile itemImage);

    
    MenuItemDTO updateMenuItem(Long itemId, UpdateMenuItemRequest request, MultipartFile itemImage);

   
    void deleteMenuItem(Long itemId);

    
    MenuItemDTO updateMenuItemAvailability(Long itemId, UpdateAvailabilityRequest request);

    
    MenuItemDTO getMenuItemById(Long itemId);
    
    
}
