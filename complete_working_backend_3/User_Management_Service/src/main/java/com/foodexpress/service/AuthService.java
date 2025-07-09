package com.foodexpress.service;

import java.util.List;
import java.util.Map;

import com.foodexpress.dto.CustomerProfileDto;
import com.foodexpress.dto.CustomerRegisterRequestDto;
import com.foodexpress.dto.CustomerResponseDto;
import com.foodexpress.dto.DeliveryAgentRequestDto;
import com.foodexpress.dto.DeliveryAgentResponseDto;
import com.foodexpress.dto.RestaurantOwnerRequestDto;

public interface AuthService {
	CustomerRegisterRequestDto registerCustomer(CustomerRegisterRequestDto customerDto);

	DeliveryAgentRequestDto registerDeliveryAgents(DeliveryAgentRequestDto deliveryAgentRequestDto);

	RestaurantOwnerRequestDto registerRestaurantOwner(RestaurantOwnerRequestDto restaurantOwner);

	Map<String, Object> login(String email, String password);
	// Method to validate email uniqueness
	boolean isEmailAlreadyRegistered(String email);

	CustomerResponseDto getCustomerById(Long customerId);

	CustomerProfileDto viewCustomerProfile(Long customerId);

	DeliveryAgentRequestDto viewDeliveryAgentProfile(int agentId);

	RestaurantOwnerRequestDto viewRestaurantOwnerProfile(int ownerId);

	void updateCustomerProfile(Long customerId, CustomerProfileDto customerProfileDto);

	void updateDeliveryAgentProfile(int agentId, DeliveryAgentRequestDto deliveryAgentProfileDto);

	void updateRestaurantOwnerProfile(int ownerId, RestaurantOwnerRequestDto restaurantOwnerProfileDto);

	List<DeliveryAgentResponseDto> getAllDeliveryPartners();
}