package com.menu.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartDTO {
	@NotNull
    private Long customerId;
	@NotNull
    private Long menuItemId;
//	@NotNull
//	@Positive
//	@Max(5)
//    private Integer quantity;
	@NotNull
	@Positive
    private Double price; // Price of the menu item
	@NotNull
    private int restaurantId; // Restaurant ID of the menu item
	@NotBlank
	private String itemName;
 
	
}