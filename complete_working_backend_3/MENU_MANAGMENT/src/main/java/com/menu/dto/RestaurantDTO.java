package com.menu.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantDTO {

	private Long userId;
	private long id;
    private String restaurantName;
    private String restaurantLocation;
    private String email;
    private String restaurantPin;
    private String contactNumber;
}
