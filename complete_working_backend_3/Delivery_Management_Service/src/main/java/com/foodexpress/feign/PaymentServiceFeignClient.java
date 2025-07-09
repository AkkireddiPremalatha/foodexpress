package com.foodexpress.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name="payment-management-service")
public interface PaymentServiceFeignClient {
	@PutMapping("/api/v1/payment/communication/methods/cod/confirm/{orderId}")
	public ResponseEntity<String> confirmCODPayment(@PathVariable Long orderId);
}
