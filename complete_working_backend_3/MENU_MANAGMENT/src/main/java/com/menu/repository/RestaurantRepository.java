package com.menu.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.menu.entity.Restaurant;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
	 
	 List<Restaurant> findByRestaurantPin(String restaurantPin);

	 Optional<Restaurant> findByRestaurantName(String restaurantName);
	 
	 boolean existsByRestaurantName(String restaurantName);
	 
	 Restaurant findByUserId(Long userId);

}
