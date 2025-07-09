package com.foodexpress.controller;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.foodexpress.dto.GenericPaymentRequestDTO;
import com.foodexpress.entity.Payment;
import com.foodexpress.service.PaymentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Payment API", description = "APIs for managing payments")
@CrossOrigin("*")
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping("customer/methods")
    @Operation(
        summary = "Get available payment methods",
        description = "Returns a list of all available payment methods."
    )
    public ResponseEntity<List<String>> getAvailableMethods() {
        log.info("Checking delivery and restaurant status before showing payment methods");

        boolean isDeliveryAvailable = true;
        boolean isRestaurantAccepted = true;

        if (isDeliveryAvailable && isRestaurantAccepted) {
            log.info("Fetching available payment methods");
            List<String> methods = paymentService.getAvailableMethods();
            log.debug("Available payment methods: {}", methods);
            return new ResponseEntity<>(methods, HttpStatus.OK);
        } else {
            String message = "Cannot accept order at the moment due to ";
            if (!isDeliveryAvailable && !isRestaurantAccepted) {
                message += "unavailable delivery and restaurant rejection.";
            } else if (!isDeliveryAvailable) {
                message += "unavailable delivery.";
            } else {
                message += "restaurant rejection.";
            }
            log.warn(message);
            return new ResponseEntity<>(List.of(message), HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    @PutMapping("/methods/cod/confirm/{orderId}")
    @Operation(summary = "Confirm COD payment after delivery", description = "Updates payment status to SUCCESS after delivery confirmation.")
    public ResponseEntity<String> confirmCODPayment(@PathVariable Long orderId) {
        try {
            paymentService.updatePaymentStatusToSuccess(orderId);
            return ResponseEntity.ok("COD payment confirmed and status updated to SUCCESS.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Unified endpoint for all payment methods
    @PostMapping("/communication/methods/pay")
    @Operation(
        summary = "Process payment (all methods)",
        description = "Processes a payment using the specified payment method and details."
    )
    public ResponseEntity<String> processPayment(
            @RequestBody @Parameter(description = "Generic payment request details") GenericPaymentRequestDTO paymentRequestDTO
    ) {
        log.info("Processing {} payment for orderId: {}", paymentRequestDTO.getPaymentMethod(), paymentRequestDTO.getOrderId());
        String result = paymentService.processPayment(paymentRequestDTO);
        log.info("{} payment processed successfully for orderId: {}", paymentRequestDTO.getPaymentMethod(), paymentRequestDTO.getOrderId());
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @GetMapping("/customer/user/{userId}")
    @Operation(
        summary = "Get payments by user ID",
        description = "Fetches all payments made by a specific user."
    )
    public ResponseEntity<List<Payment>> getPaymentsByUserId(
        @PathVariable @Parameter(description = "ID of the user") Long userId
    ) {
        log.info("Fetching payment details for userId: {}", userId);
        List<Payment> payments = paymentService.getPaymentsByUserId(userId);
        if (payments == null) payments = Collections.emptyList();
        log.debug("Fetched {} payments for userId: {}", payments.size());
        return new ResponseEntity<>(payments, HttpStatus.OK);
    }
    

    @GetMapping("/customer/{orderId}")
    @Operation(
        summary = "Get payment by order ID",
        description = "Fetches payment details for a specific order."
    )
    public ResponseEntity<Payment> getPaymentByOrderId(
        @PathVariable @Parameter(description = "ID of the order") Long orderId
    ) {
        log.info("Fetching payment details for orderId: {}", orderId);
        Optional<Payment> payment = paymentService.getPaymentByOrderId(orderId);
        return payment.map(p -> {
            log.info("Payment found for orderId: {}", orderId);
            return new ResponseEntity<>(p, HttpStatus.OK);
        }).orElseGet(() -> {
            log.warn("Payment not found for orderId: {}", orderId);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        });
    }
}