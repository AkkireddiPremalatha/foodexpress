package com.menu.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.menu.dto.CustomerResponseDto;

@FeignClient(name="user-management-service")
public interface CustomerServiceFeignClient {
	
	@GetMapping("/api/v1/auth/customers/{customerId}")
    public CustomerResponseDto getCustomerById(@PathVariable Long customerId);
}
