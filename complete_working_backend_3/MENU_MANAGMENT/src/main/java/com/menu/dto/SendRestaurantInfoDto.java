package com.menu.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SendRestaurantInfoDto {
	
	
	private String restaurantName;

	
    private String restaurantPin;
	
	
    private String restaurantLocation;

    
    private String contactNumber;

    private String email;

}
