package com.menu.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero; // Price can be 0 or positive
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMenuItemRequest {

	@NotBlank(message = "Item name cannot be empty")
	private String itemName;

	private String itemDescription;

	@NotNull(message = "Price cannot be null")
	@PositiveOrZero(message = "Price must be positive or zero")
	private Double price;

	@NotNull(message = "Availability status cannot be null")
	private Boolean isAvailable;

//	private String itemImage;

//	@NotNull(message = "Category ID cannot be null")
//	private Long categoryId;

}
