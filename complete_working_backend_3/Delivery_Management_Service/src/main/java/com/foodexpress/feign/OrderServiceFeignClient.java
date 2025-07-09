package com.foodexpress.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import com.foodexpress.dto.OrderResponseDTO;
import com.foodexpress.enums.Status;

@FeignClient(name = "order-management-service")
public interface OrderServiceFeignClient {
	@GetMapping("/api/v1/order/getOrdersBasedOnPaymentStatusAndMethod")
	List<OrderResponseDTO> getAllOrdersWithCODAndPaymentSuccessful();
	
	@PutMapping("/api/v1/order/communication/update/{orderId}/{status}")
	public String setOrderStatus(@PathVariable Long orderId,Status status);
}
