package com.foodexpress.dto;

import com.foodexpress.model.Cart;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Max;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartResponseDTO {
	@NotNull                       
    private Long customerId;
	@Max(10)
    private List<Cart> items;
    @Positive
    private Integer totalItems;
    @Positive
    @NotNull
    private Double totalAmount;
}