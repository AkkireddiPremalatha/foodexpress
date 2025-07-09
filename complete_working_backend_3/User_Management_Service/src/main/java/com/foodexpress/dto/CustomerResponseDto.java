package com.foodexpress.dto;

import lombok.Data;

@Data
public class CustomerResponseDto {
	private Long userId;
    private String firstName;
    private String email;
    private String phone;
    private String address;
    private String customerPincode;
}
