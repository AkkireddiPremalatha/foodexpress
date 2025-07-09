package com.foodexpress.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.util.Map;

@Data
@Schema(description = "Generic Payment Request for all payment methods")
public class GenericPaymentRequestDTO {

    @NotNull
    @Schema(description = "Payment method type", example = "CREDIT_CARD")
    private String paymentMethod;

    @NotNull
    @Schema(description = "Order ID for the payment", example = "12345")
    private Long orderId;

    @NotNull
    @Schema(description = "User ID making the payment", example = "67890")
    private Long userId;

    @Positive
    @Schema(description = "Payment amount", example = "500.00")
    private Double amount;

    @Schema(description = "Payment method specific details as key-value pairs")
    private Map<String, Object> paymentDetails;
}