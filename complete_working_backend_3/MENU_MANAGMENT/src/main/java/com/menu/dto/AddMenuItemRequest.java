package com.menu.dto;

import com.menu.enums.Category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddMenuItemRequest {

    @NotNull(message = "Restaurant ID cannot be null")
    private Long restaurantId; 

    @NotNull(message = "Category ID cannot be null")
    private Category categoryName;

    @NotBlank(message = "Item name cannot be empty")
    private String itemName;

    private String itemDescription;

    @NotNull(message = "Price cannot be null")
    @Positive(message = "Price must be positive")
    private double price;

    private boolean isAvailable = true; // Default to true on add

//    private String itemImage;
}
