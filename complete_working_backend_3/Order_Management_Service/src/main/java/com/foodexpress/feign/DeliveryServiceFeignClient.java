package com.foodexpress.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.foodexpress.dto.AssignDeliveryRequestDTO;

@FeignClient(name="delivery-management-service")
public interface DeliveryServiceFeignClient {
	
	@GetMapping("/api/v1/communication/getDeliveryAgentStatus/{customerPincode}")
	public boolean getDeliveryAgentStatus(@PathVariable String customerPincode);
	
	@PostMapping("/api/v1/communication/assign-delivery")
	public ResponseEntity<String> assignDelivery(@RequestBody AssignDeliveryRequestDTO assignDeliveryRequestDTO);
	
	@GetMapping("/api/v1/deliveryAgent/delivery-status")
	public ResponseEntity<String> getDeliveryStatus(AssignDeliveryRequestDTO assignDeliveryRequestDTO);
	        
	
}