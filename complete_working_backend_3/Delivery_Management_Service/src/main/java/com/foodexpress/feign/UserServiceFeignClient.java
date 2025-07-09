package com.foodexpress.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.foodexpress.dto.CustomerResponseDto;
import com.foodexpress.dto.DeliveryAgentResponseDto;

@FeignClient(name = "user-management-service")
public interface UserServiceFeignClient {
	
	@GetMapping("/api/v1/auth/delivery-partners")
	List<DeliveryAgentResponseDto> getAllDeliveryAgents();
	
	@GetMapping("/api/v1/auth/customers/{customerId}")
    CustomerResponseDto getCustomerById(@PathVariable Long customerId);
}
