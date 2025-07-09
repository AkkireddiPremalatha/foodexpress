package com.menu.service;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.menu.dto.RestaurantDTO;
import com.menu.entity.Restaurant;
import com.menu.enums.Status;
import com.menu.feign.OrderServiceFeignClient;
import com.menu.repository.RestaurantRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {

	private final RestaurantRepository restaurantRepository;
	
	private final OrderServiceFeignClient orderServiceFeignClient;
	

    // to map Entity to DTO (can reused from CustomerMenuServiceImpl)
    private RestaurantDTO mapToRestaurantDTO(Restaurant restaurant) {
        if (restaurant == null) {
            return null;
        }
        return new RestaurantDTO(
                restaurant.getUserId(),
                restaurant.getId(),
                restaurant.getRestaurantName(),
                restaurant.getRestaurantLocation(),
                restaurant.getEmail(),
                restaurant.getRestaurantPin(),
                restaurant.getContactNumber()
        );
    }

    // to map DTO to Entity (for creation)
    private Restaurant mapToRestaurantEntity(RestaurantDTO restaurantDTO) {
        if (restaurantDTO == null) return null;
        Restaurant restaurant = new Restaurant();
        restaurant.setUserId(restaurantDTO.getUserId());
        restaurant.setRestaurantName(restaurantDTO.getRestaurantName());
        restaurant.setRestaurantPin(restaurantDTO.getRestaurantPin());
        restaurant.setRestaurantLocation(restaurantDTO.getRestaurantLocation());
        restaurant.setContactNumber(restaurantDTO.getContactNumber());
        restaurant.setEmail(restaurantDTO.getEmail()); 
        restaurant.setCreatedBy("Admin"); 
        restaurant.setOpen(true);
        return restaurant;
    }
	
	
	@Override
	public RestaurantDTO createRestaurant(RestaurantDTO restaurantDTO) {
		//  Check if restaurant with same name already exists
        if (restaurantRepository.existsByRestaurantName(restaurantDTO.getRestaurantName())) {
            throw new IllegalArgumentException("Restaurant with name '" + restaurantDTO.getRestaurantName() + "' already exists.");
        }
        Restaurant restaurant = mapToRestaurantEntity(restaurantDTO);
        Restaurant savedRestaurant = restaurantRepository.save(restaurant);
        return mapToRestaurantDTO(savedRestaurant);
    }
//	@Override
//	public RestaurantDTO createRestaurant(RestaurantDTO restaurantDTO) {
//		// Check if restaurant with same name already exists
//		if (restaurantRepository.findByRestaurantName(restaurantDTO.getRestaurantName()).isPresent()) {
//			throw new IllegalArgumentException("Restaurant with name '" + restaurantDTO.getRestaurantName() + "' already exists.");
//		}
//		Restaurant restaurant = mapToRestaurantEntity(restaurantDTO);
//		Restaurant savedRestaurant = restaurantRepository.save(restaurant);
//		return mapToRestaurantDTO(savedRestaurant);
//		}
	
	@Override
	public String updateRestaurantStatus(Long userId,boolean status){
		Restaurant restaurant = restaurantRepository.findByUserId(userId);
		
		restaurant.setOpen(status);
		
		restaurantRepository.save(restaurant);
		
		return "restaurant status updated successfully";
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
    /* // Example: If you wanted a get by ID here too
    public RestaurantDTO getRestaurantById(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with ID: " + id));
        return mapToRestaurantDTO(restaurant);
    }
    */
	@Override
	public boolean getRestaurantStatus(int restaurantId) {
		Long lng = (long)restaurantId;
		boolean status = false;
		Optional<Restaurant> restaurant = restaurantRepository.findById(lng);
		if(restaurant.isPresent()) {
			status =  restaurant.get().isOpen();
		}
		return status;
	}
	
	@Override
	public ResponseEntity<Restaurant> getRestaurantById(Long userId){
		Restaurant restaurant = restaurantRepository.findByUserId(userId);
		return 	ResponseEntity.ok(restaurant);
	}
	
	@Override
	public String updateOrderStatus(Long orderId,Status status) throws RuntimeException{
		
			orderServiceFeignClient.setOrderStatus(orderId,status);
			return "Order updated successfully!";
	}	
}
