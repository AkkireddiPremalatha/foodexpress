package com.foodexpress.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerResponseDto {
	private Long userId;
    private String firstName;
    private String email;
    private String phone;
    private String address;
    private String customerPincode;
}
