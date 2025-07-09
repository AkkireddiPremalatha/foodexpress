package com.foodexpress.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryAgentResponseDto {
	private int partnerId;
	private String name;
	private String email;
	private String phoneNo;
	private String vehicleNumber;
	private String servicePincode;
	private Boolean deliveryPartnerStatus;
}
