package com.foodexpress.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.foodexpress.dto.PartnerDTO;

@FeignClient(name="delivery-management-service")
public interface DeliveryServiceFeignClient {
	@PostMapping("/api/v1/communication/createDeliveryPartner")
	public ResponseEntity<PartnerDTO> createDeliveryPartner(@RequestBody PartnerDTO partnerDTO);
}
