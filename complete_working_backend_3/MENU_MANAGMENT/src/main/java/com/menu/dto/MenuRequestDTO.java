package com.menu.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MenuRequestDTO {
	private Long menuItemId;
	private Long restaurantId;
	private double price;
	private String itemName;
}
