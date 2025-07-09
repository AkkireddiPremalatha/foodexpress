package com.menu.dto;

import com.menu.enums.Category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuItemDTO {

	private Long menuItemId;
    private Long restaurantId; 
    private String itemName;
    private String itemDescription;
    private double price;
    private Category category;
    //private String categoryName;
    private boolean isAvailable;
    private byte[] itemImage;
}


