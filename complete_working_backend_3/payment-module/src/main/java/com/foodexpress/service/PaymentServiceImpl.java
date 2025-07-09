package com.foodexpress.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.foodexpress.dto.GenericPaymentRequestDTO;
import com.foodexpress.entity.Payment;
import com.foodexpress.exception.PaymentException;
import com.foodexpress.exception.PaymentTimeoutException;
import com.foodexpress.repository.PaymentRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private static final String STATUS_PENDING = "PENDING";
    private static final String STATUS_SUCCESS = "SUCCESS";

    @Override
    @Transactional
    public String processPayment(GenericPaymentRequestDTO request) {
        log.info("Processing payment for orderId: {}", request.getOrderId());
        
        Optional<Payment> existingPayment = paymentRepository.findByOrderId(request.getOrderId());
        if (existingPayment.isPresent()) {
            throw new IllegalArgumentException("A payment already exists for this order ID: " + request.getOrderId());
        }
        
        boolean isTimeout = false; // Simulate timeout if needed

        if (isTimeout) {
            log.warn("Payment timeout occurred for orderId: {}", request.getOrderId());
            Payment timeoutPayment = new Payment();
            timeoutPayment.setOrderId(request.getOrderId());
            timeoutPayment.setUserId(request.getUserId());
            timeoutPayment.setPaymentMethod(request.getPaymentMethod());
            timeoutPayment.setAmount(request.getAmount());
            timeoutPayment.setTimestamp(LocalDateTime.now());
            timeoutPayment.setStatus(STATUS_PENDING);
            paymentRepository.save(timeoutPayment);
            throw new PaymentTimeoutException("Payment timeout occurred. Please retry.");
        }

        Payment payment = new Payment();
        payment.setOrderId(request.getOrderId());
        payment.setUserId(request.getUserId());
        payment.setPaymentMethod(request.getPaymentMethod());
        payment.setAmount(request.getAmount());
        payment.setTimestamp(LocalDateTime.now());

        Map<String, Object> details = request.getPaymentDetails();
        String method = request.getPaymentMethod();

        switch (method) {
            case "CREDIT_CARD": {
                String cardNumber = (String) details.get("cardNumber");
                String cvv = (String) details.get("cvv");
                String expiry = (String) details.get("expiry");
                String cardHolderName = (String) details.get("cardHolderName");

                if (cardNumber == null || !cardNumber.matches("\\d{16}")) {
                    throw new IllegalArgumentException("Invalid or missing card number");
                }
                if (cvv == null || !cvv.matches("\\d{3}")) {
                    throw new IllegalArgumentException("Invalid or missing CVV");
                }
                if (expiry == null || !expiry.matches("(0[1-9]|1[0-2])/\\d{2}") ) {
                    throw new IllegalArgumentException("Invalid or missing expiry date");
                }
                if (cardHolderName == null || cardHolderName.isBlank()) {
                    throw new IllegalArgumentException("Cardholder name is required");
                }
                payment.setStatus(STATUS_SUCCESS);
                // Mask card number: show only last 4 digits, mask rest
                String maskedCard = "XXXX-XXXX-XXXX-" + cardNumber.substring(cardNumber.length() - 4);
                payment.setPaymentDetails("Card: " + maskedCard + ", Name: " + maskName(cardHolderName) );
                break;
            }
            case "DEBIT_CARD": {
                String cardNumber = (String) details.get("cardNumber");
                String cvv = (String) details.get("cvv");
                String expiry = (String) details.get("expiry");
                String cardHolderName = (String) details.get("cardHolderName");

                if (cardNumber == null || !cardNumber.matches("\\d{16}")) {
                    throw new IllegalArgumentException("Invalid or missing card number");
                }
                if (cvv == null || !cvv.matches("\\d{3}")) {
                    throw new IllegalArgumentException("Invalid or missing CVV");
                }
                if (expiry == null || !expiry.matches("(0[1-9]|1[0-2])/\\d{2}")) {
                    throw new IllegalArgumentException("Invalid or missing expiry date");
                }
                if (cardHolderName == null || cardHolderName.isBlank()) {
                    throw new IllegalArgumentException("Cardholder name is required");
                }

                payment.setStatus(STATUS_SUCCESS);
                String maskedCard = "XXXX-XXXX-XXXX-" + cardNumber.substring(cardNumber.length() - 4);
                payment.setPaymentDetails("Card: " + maskedCard + ", Name: " + maskName(cardHolderName) + ", Expiry: " + expiry);
                break;
            }
            case "UPI": {
                String upiId = (String) details.get("upiId");
                if (upiId == null || !upiId.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+$")) {
                    throw new IllegalArgumentException("Invalid or missing UPI ID");
                }
                payment.setStatus(STATUS_SUCCESS);
                // Mask UPI ID (show only first 2 and domain)
                String maskedUpi = maskUpiId(upiId);
                payment.setPaymentDetails("UPI ID: " + maskedUpi);
                break;
            }
            case "WALLET": {
                String walletId = (String) details.get("walletId");
                if (walletId == null || walletId.isBlank()) {
                    throw new IllegalArgumentException("Wallet ID is required");
                }
                payment.setStatus(STATUS_SUCCESS);
                // Mask wallet ID (show only last 3)
                String maskedWallet = "****" + walletId.substring(Math.max(0, walletId.length() - 3));
                payment.setPaymentDetails("Wallet ID: " + maskedWallet);
                break;
            }
            case "NET_BANKING": {
                String bankName = (String) details.get("bankName");
                String accountNumber = (String) details.get("accountNumber");
                String ifscCode = (String) details.get("ifscCode");

                if (bankName == null || bankName.isBlank()) {
                    throw new IllegalArgumentException("Bank name is required");
                }
                if (accountNumber == null || !accountNumber.matches("\\d{9,18}")) {
                    throw new IllegalArgumentException("Invalid or missing account number");
                }
                if (ifscCode == null || !ifscCode.matches("[A-Z]{4}0[A-Z0-9]{6}")) {
                    throw new IllegalArgumentException("Invalid or missing IFSC code");
                }

                payment.setStatus(STATUS_SUCCESS);
                // Mask account number (show only last 4)
                String maskedAccount = "XXXXXX" + accountNumber.substring(accountNumber.length() - 4);
                payment.setPaymentDetails("Bank: " + bankName + ", Account: " + maskedAccount + ", IFSC: " + ifscCode);
                break;
            }
            case "COD": {
                if (request.getAmount() > 2000) {
                    throw new PaymentException("COD is not applicable for order value above Rs.1000.");
                }
                payment.setStatus(STATUS_PENDING);
                payment.setPaymentDetails("Cash on Delivery");
                break;
            }
            default:
                throw new IllegalArgumentException("Unsupported payment method: " + method);
        }

        log.info("Payment processed with status {} for orderId: {}", payment.getStatus(), payment.getOrderId());
        Payment payment1 = paymentRepository.save(payment);
        return payment1.getStatus();
    }

    // Helper to mask cardholder name (show only first letter)
    private String maskName(String name) {
        if (name == null || name.isBlank()) return "";
        String[] parts = name.split(" ");
        StringBuilder masked = new StringBuilder();
        for (String part : parts) {
            if (!part.isBlank()) {
                masked.append(part.charAt(0));
                for (int i = 1; i < part.length(); i++) masked.append("*");
                masked.append(" ");
            }
        }
        return masked.toString().trim();
    }

    // Helper to mask UPI ID (show first 2 chars, then ***, then domain)
    private String maskUpiId(String upiId) {
        if (upiId == null || !upiId.contains("@")) return "****";
        String[] parts = upiId.split("@", 2);
        String user = parts[0];
        String domain = parts[1];
        String maskedUser = user.length() <= 2 ? user.charAt(0) + "*" : user.substring(0, 2) + "***";
        return maskedUser + "@" + domain;
    }

    @Override
    @Transactional
    public void updatePaymentStatusToSuccess(Long orderId) {
        Optional<Payment> paymentOpt = paymentRepository.findByOrderId(orderId);
        if (paymentOpt.isEmpty()) {
            throw new IllegalArgumentException("No payment found for this order.");
        }

        Payment payment = paymentOpt.get();
        if (!STATUS_PENDING.equalsIgnoreCase(payment.getStatus())) {
            throw new IllegalStateException("Payment is not in PENDING state.");
        }

        payment.setStatus(STATUS_SUCCESS);
        payment.setTimestamp(LocalDateTime.now());
        paymentRepository.save(payment);
        log.info("Payment status updated to SUCCESS for orderId: {}", orderId);
    }

    @Override
    public void checkIfPaymentExists(Long orderId) {
        log.info("Checking if payment exists for orderId: {}", orderId);
        Optional<Payment> existingPayment = paymentRepository.findByOrderId(orderId);
        if (existingPayment.isPresent()) {
            log.warn("Payment already exists for orderId: {}", orderId);
            throw new IllegalArgumentException("A payment already exists for this order ID.");
        }
        log.info("No existing payment found for orderId: {}", orderId);
    }

    @Override
    public List<String> getAvailableMethods() {
        log.info("Fetching available payment methods");
        return List.of("CREDIT_CARD", "DEBIT_CARD", "UPI", "WALLET", "NET_BANKING", "COD");
    }

    @Override
    public Optional<Payment> getPaymentByOrderId(Long orderId) {
        log.info("Fetching payment details for orderId: {}", orderId);
        return paymentRepository.findByOrderId(orderId);
    }

    @Override
    public List<Payment> getPaymentsByUserId(Long userId) {
        log.info("Fetching payment details for userId: {}", userId);
        return paymentRepository.findByUserId(userId);
    }
}