package com.foodexpress.service;

import com.foodexpress.dto.GenericPaymentRequestDTO;
import com.foodexpress.entity.Payment;

import java.util.List;
import java.util.Optional;

public interface PaymentService {
    String processPayment(GenericPaymentRequestDTO request);
    void updatePaymentStatusToSuccess(Long orderId);
    List<String> getAvailableMethods();
    Optional<Payment> getPaymentByOrderId(Long orderId);
    List<Payment> getPaymentsByUserId(Long userId);
    void checkIfPaymentExists(Long orderId);
}