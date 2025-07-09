package com.foodexpress.dto;

import com.foodexpress.enums.Status;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDTO {
	@NotNull
    private Long customerId;
    private Long orderId;
    @Enumerated(EnumType.STRING)
    @NotNull
    private Status status;
    @Positive
    @NotNull
    private Double totalAmount;
    private CustomerResponseDto customer;
}