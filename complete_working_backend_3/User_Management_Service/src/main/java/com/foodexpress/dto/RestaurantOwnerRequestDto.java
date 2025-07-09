package com.foodexpress.dto;

import com.foodexpress.entity.Roles;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantOwnerRequestDto {

	@NotNull(message = "First name is mandatory")
	private String firstName;
	@NotNull(message = "Last name is mandatory")
	private String lastName;

	@NotNull(message = "Email is mandatory")
	@Size(max = 50, message = "Email must be less than 50 characters")
	private String email;
	@NotNull(message="password  is mandatory")
	private String password;
	@NotNull(message = "Phone number is mandatory")
	private String phoneNo;
	@NotNull(message = "Address is mandatory")
	private String address;
	
	
	private String restaurantName;

	
    private String restaurantPin;
	
	
    private String restaurantLocation;
	

}
