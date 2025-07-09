package com.foodexpress.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

import com.foodexpress.dto.GenericPaymentRequestDTO;

@FeignClient(name="payment-management-service")
public interface PaymentServiceFeignClient {
	
	@PostMapping("api/v1/payment/communication/methods/pay")
	ResponseEntity<String> processPayment(GenericPaymentRequestDTO genericPaymentRequestDTO);
	

}
