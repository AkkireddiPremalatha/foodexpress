package com.foodexpress.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.foodexpress.dto.CustomerResponseDto;

@FeignClient(name = "user-management-service", contextId = "customerClient")
public interface CustomerServiceFeignClient {
	@GetMapping("/api/v1/auth/customers/{customerId}")
    CustomerResponseDto getCustomerById(@PathVariable Long customerId);
}
