package com.foodexpress.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CustomerRegisterRequestDto {
	
	

	@NotNull(message = "First name is mandatory")
	private String firstName;

	@NotNull(message = "Last name is mandatory")
	private String lastName;

	@NotNull(message = "Email is mandatory")
	@Email(message = "Invalid email format")
	@Size(max = 50, message = "Email must be less than 50 characters")
	private String email;

	@NotNull(message = "Password is mandatory")
	@Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
	@Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$", message = "Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character")
	private String password;

	@NotNull(message = "Phone number is mandatory")
	@Pattern(regexp = "^\\d{10}$", message = "Phone number must be exactly 10 digits")
	private String phoneNo;

	@NotNull(message = "Address is mandatory")
	private String address;
	
	@NotNull(message="pincode is mandatory")
	private String customerPincode;
}