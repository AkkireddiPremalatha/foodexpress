	package com.foodexpress.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryAgentRequestDto {
	
	

	@NotNull(message = "Name is mandatory")
	private String name;

	@NotNull(message = "Email is mandatory")
	@Size(max = 50, message = "Email must be less than 50 characters")
	private String email;
	@NotNull(message = "Phone number is mandatory")
	private String phoneNo;
	@NotNull(message = "A Vehicle with registered number is mandatory")
	private String vehicleNumber;
	@NotNull(message="Pincode cannot be null")
	private String servicePincode;
	private Boolean deliveryPartnerStatus = true;
	private String address;
	@NotNull(message="Password cannot be null")
	@Size(max=15, message="Password must be less than 15 characters")
	private String password;

}
