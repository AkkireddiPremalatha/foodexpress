package com.foodexpress.dto;
 
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
 
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerProfileDto {
	
	
	private String firstName;
 
	
	private String lastName;
 
	
	@Pattern(regexp = "^\\d{10}$", message = "Phone number must be exactly 10 digits")
	private String phoneNo;
 
 
	private String address;
	
 
	private String customerPincode;
}
 