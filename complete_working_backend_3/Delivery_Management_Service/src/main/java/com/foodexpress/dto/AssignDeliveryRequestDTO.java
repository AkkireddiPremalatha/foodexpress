package com.foodexpress.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AssignDeliveryRequestDTO {

	@NotNull(message = "Order ID cannot be null")
	private Long orderId;

	@NotNull(message= "Customer Pincode cannot be null")
	
	private String customerPincode;
	
	private String deliveryStatus;

}