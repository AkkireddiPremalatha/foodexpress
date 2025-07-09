	package com.foodexpress.dto;
	
	import jakarta.validation.constraints.NotNull;
	import lombok.Data;
	
	@Data
	public class DeliveryDTO {
		@NotNull(message="Order ID cannot be null.")
	    private Long orderId;
	}