package com.foodexpress.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class DeliveryStatusDTO {

    @NotNull(message = "Order ID cannot be null")
    private Long orderId;

    @NotBlank(message = "Delivery status cannot be blank")
    @Size(min = 2, max = 50, message = "Delivery status must be between 2 and 50 characters")
    private String deliveryStatus;
}