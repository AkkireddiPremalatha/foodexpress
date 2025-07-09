package com.foodexpress.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
public class CustomerResponseDto {
	private String firstName;
	private String email;
	private String phone;
	private String address;
	private String customerPincode;
}
