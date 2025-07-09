package com.foodexpress.exception;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PaymentException extends RuntimeException {
    public PaymentException(String message) {
        super(message);
    }
}

