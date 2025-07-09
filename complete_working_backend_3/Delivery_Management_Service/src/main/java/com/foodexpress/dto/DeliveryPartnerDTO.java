package com.foodexpress.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class DeliveryPartnerDTO {

	@NotBlank(message = "Name cannot be blank")
	@Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
	private String name;

	@NotBlank(message = "Phone number cannot be blank")
	@Pattern(regexp = "^\\d{10}$", message = "Phone number must be a valid 10-digit number")
	private String phoneNumber;

	@NotBlank(message = "Vehicle number cannot be blank")
	@Size(min = 5, max = 15, message = "Vehicle number must be between 5 and 15 characters")
	private String vehicleNumber;

	@NotBlank(message = "Email cannot be blank")
	@Email(message = "Email must be a valid email address")
	private String email;

	private Boolean deliveryPartnerStatus = true;
}