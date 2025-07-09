package com.foodexpress.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantDTO {

	private int userId;
	private long id;
    private String restaurantName;
    private String restaurantLocation;
    private String restaurantPin;
    private String contactNumber;
    private String email;
}
