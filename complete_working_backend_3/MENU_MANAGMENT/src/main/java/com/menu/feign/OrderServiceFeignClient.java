package com.menu.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.menu.dto.CartDTO;
import com.menu.enums.Status;

@FeignClient(name="order-management-service")
public interface OrderServiceFeignClient {
	@PostMapping("/api/v1/cart/customer/add")
	public String addItemToCart( @RequestBody CartDTO cartDTO);
	
	@DeleteMapping("/api/v1/cart/customer/remove")
	public void removeItemFromCart(@RequestParam Long customerId, @RequestParam Long menuItemId);
	
	@PutMapping("/api/v1/order/communication/update/{orderId}/{status}")
	public String setOrderStatus(@PathVariable Long orderId,Status status);
}
