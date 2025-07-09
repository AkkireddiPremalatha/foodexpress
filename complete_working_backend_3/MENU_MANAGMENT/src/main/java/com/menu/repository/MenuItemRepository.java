package com.menu.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.menu.entity.MenuItem;
import com.menu.enums.Category;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {

	
    List<MenuItem> findByRestaurant_Id(Long restaurantId);

    
    // Searches for menu items where the item name contains the keyword (case-insensitive)
    List<MenuItem> findByRestaurantIdInAndItemNameContainingIgnoreCase(List<Long> restaurantIds, String itemName);

    
    // Finds menu items by category for a specific restaurant
    List<MenuItem> findByRestaurant_IdAndCategory(Long restaurantId, Category category);

   
    Optional<MenuItem> findById(Long id); 

    // If needed to search within a specific restaurant's menu
    List<MenuItem> findByRestaurant_IdAndItemNameContainingIgnoreCase(Long restaurantId, String keyword);
    
//    List<MenuItem> findByRestaurant_IdAndCategory_Id(Long restaurantId, Long categoryId);
}
