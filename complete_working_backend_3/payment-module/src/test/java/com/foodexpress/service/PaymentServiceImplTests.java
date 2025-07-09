//package com.foodexpress.service;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//import com.foodexpress.dto.GenericPaymentRequestDTO;
//import com.foodexpress.entity.Payment;
//import com.foodexpress.exception.PaymentException;
//import com.foodexpress.repository.PaymentRepository;
//import org.junit.jupiter.api.*;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.*;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.time.LocalDateTime;
//import java.util.*;
//
//@ExtendWith(MockitoExtension.class)
//class PaymentServiceImplTests {
//
//    @Mock
//    private PaymentRepository paymentRepository;
//
//    @InjectMocks
//    private PaymentServiceImpl paymentService;
//
//    private Payment payment;
//    private GenericPaymentRequestDTO request;
//
//    @BeforeEach
//    void setUp() {
//        payment = new Payment();
//        payment.setId(1L);
//        payment.setOrderId(101L);
//        payment.setUserId(201L);
//        payment.setAmount(500.0);
//        payment.setPaymentMethod("UPI");
//        payment.setStatus("SUCCESS");
//        payment.setTimestamp(LocalDateTime.now());
//        payment.setPaymentDetails("UPI ID: jo***@upi");
//
//        request = new GenericPaymentRequestDTO();
//        request.setOrderId(101L);
//        request.setUserId(201L);
//        request.setAmount(500.0);
//        request.setPaymentMethod("UPI");
//        Map<String, Object> details = new HashMap<>();
//        details.put("upiId", "john@upi");
//        request.setPaymentDetails(details);
//    }
//
//    @AfterEach
//    void tearDown() {
//        payment = null;
//        request = null;
//    }
//
//    @Test
//    void processPayment_ShouldReturnSavedPayment_WhenValidUPI() {
//        when(paymentRepository.findByOrderId(101L)).thenReturn(Optional.empty());
//        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);
//
//        Payment result = paymentService.processPayment(request);
//
//        assertThat(result).isNotNull();
//        assertEquals("SUCCESS", result.getStatus());
//        verify(paymentRepository, times(1)).save(any(Payment.class));
//    }
//
//    @Test
//    void processPayment_ShouldThrowException_WhenPaymentAlreadyExists() {
//        when(paymentRepository.findByOrderId(101L)).thenReturn(Optional.of(payment));
//        Exception ex = assertThrows(IllegalArgumentException.class, () -> paymentService.processPayment(request));
//        assertTrue(ex.getMessage().contains("A payment already exists for this order ID"));
//    }
//
//    @Test
//    void processPayment_ShouldThrowException_WhenInvalidUPI() {
//        when(paymentRepository.findByOrderId(101L)).thenReturn(Optional.empty());
//        request.getPaymentDetails().put("upiId", "invalidupi");
//        Exception ex = assertThrows(IllegalArgumentException.class, () -> paymentService.processPayment(request));
//        assertTrue(ex.getMessage().contains("Invalid or missing UPI ID"));
//    }
//
//    @Test
//    void processPayment_ShouldReturnSavedPayment_WhenCreditCard() {
//        when(paymentRepository.findByOrderId(101L)).thenReturn(Optional.empty());
//        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);
//
//        request.setPaymentMethod("CREDIT_CARD");
//        Map<String, Object> details = new HashMap<>();
//        details.put("cardNumber", "1234567812345678");
//        details.put("cvv", "123");
//        details.put("expiry", "12/25");
//        details.put("cardHolderName", "John Doe");
//        request.setPaymentDetails(details);
//
//        Payment result = paymentService.processPayment(request);
//
//        assertThat(result).isNotNull();
//        assertEquals("SUCCESS", result.getStatus());
//        verify(paymentRepository, times(1)).save(any(Payment.class));
//    }
//
//    @Test
//    void processPayment_ShouldThrowException_WhenCreditCardMissingField() {
//        when(paymentRepository.findByOrderId(101L)).thenReturn(Optional.empty());
//        request.setPaymentMethod("CREDIT_CARD");
//        Map<String, Object> details = new HashMap<>();
//        details.put("cardNumber", "1234567812345678");
//        // missing cvv
//        details.put("expiry", "12/25");
//        details.put("cardHolderName", "John Doe");
//        request.setPaymentDetails(details);
//
//        Exception ex = assertThrows(IllegalArgumentException.class, () -> paymentService.processPayment(request));
//        assertTrue(ex.getMessage().contains("Invalid or missing CVV"));
//    }
//
//    @Test
//    void processPayment_ShouldReturnSavedPayment_WhenDebitCard() {
//        when(paymentRepository.findByOrderId(101L)).thenReturn(Optional.empty());
//        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);
//
//        request.setPaymentMethod("DEBIT_CARD");
//        Map<String, Object> details = new HashMap<>();
//        details.put("cardNumber", "1234567812345678");
//        details.put("cvv", "123");
//        details.put("expiry", "12/25");
//        details.put("cardHolderName", "Jane Doe");
//        request.setPaymentDetails(details);
//
//        Payment result = paymentService.processPayment(request);
//
//        assertThat(result).isNotNull();
//        assertEquals("SUCCESS", result.getStatus());
//        verify(paymentRepository, times(1)).save(any(Payment.class));
//    }
//
//    @Test
//    void processPayment_ShouldReturnSavedPayment_WhenWallet() {
//        when(paymentRepository.findByOrderId(101L)).thenReturn(Optional.empty());
//        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);
//
//        request.setPaymentMethod("WALLET");
//        Map<String, Object> details = new HashMap<>();
//        details.put("walletId", "WALLET123");
//        request.setPaymentDetails(details);
//
//        Payment result = paymentService.processPayment(request);
//
//        assertThat(result).isNotNull();
//        assertEquals("SUCCESS", result.getStatus());
//        verify(paymentRepository, times(1)).save(any(Payment.class));
//    }
//
//    @Test
//    void processPayment_ShouldThrowException_WhenWalletMissingField() {
//        when(paymentRepository.findByOrderId(101L)).thenReturn(Optional.empty());
//        request.setPaymentMethod("WALLET");
//        Map<String, Object> details = new HashMap<>();
//        // missing walletId
//        request.setPaymentDetails(details);
//
//        Exception ex = assertThrows(IllegalArgumentException.class, () -> paymentService.processPayment(request));
//        assertTrue(ex.getMessage().contains("Wallet ID is required"));
//    }
//
//    @Test
//    void processPayment_ShouldReturnSavedPayment_WhenNetBanking() {
//        when(paymentRepository.findByOrderId(101L)).thenReturn(Optional.empty());
//        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);
//
//        request.setPaymentMethod("NET_BANKING");
//        Map<String, Object> details = new HashMap<>();
//        details.put("bankName", "HDFC");
//        details.put("accountNumber", "123456789012");
//        details.put("ifscCode", "HDFC0001234");
//        request.setPaymentDetails(details);
//
//        Payment result = paymentService.processPayment(request);
//
//        assertThat(result).isNotNull();
//        assertEquals("SUCCESS", result.getStatus());
//        verify(paymentRepository, times(1)).save(any(Payment.class));
//    }
//
//    @Test
//    void processPayment_ShouldThrowException_WhenNetBankingMissingField() {
//        when(paymentRepository.findByOrderId(101L)).thenReturn(Optional.empty());
//        request.setPaymentMethod("NET_BANKING");
//        Map<String, Object> details = new HashMap<>();
//        details.put("bankName", "HDFC");
//        // missing accountNumber and ifscCode
//        request.setPaymentDetails(details);
//
//        Exception ex = assertThrows(IllegalArgumentException.class, () -> paymentService.processPayment(request));
//        assertTrue(ex.getMessage().contains("Invalid or missing account number"));
//    }
//
//    @Test
//    void processPayment_ShouldReturnSavedPayment_WhenCOD() {
//        when(paymentRepository.findByOrderId(101L)).thenReturn(Optional.empty());
//        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));
//        
//
//        request.setPaymentMethod("COD");
//        request.setAmount(500.0);
//        request.setPaymentDetails(new HashMap<>());
//
//        Payment result = paymentService.processPayment(request);
//
//        assertThat(result).isNotNull();
//        assertEquals("PENDING", result.getStatus());
//        verify(paymentRepository, times(1)).save(any(Payment.class));
//    }
//
//    @Test
//    void processPayment_ShouldThrowException_WhenCODAmountTooHigh() {
//        when(paymentRepository.findByOrderId(101L)).thenReturn(Optional.empty());
//        request.setPaymentMethod("COD");
//        request.setAmount(2000.0);
//        request.setPaymentDetails(new HashMap<>());
//        Exception ex = assertThrows(PaymentException.class, () -> paymentService.processPayment(request));
//        assertTrue(ex.getMessage().contains("COD is not applicable"));
//    }
//
//    @Test
//    void processPayment_ShouldThrowException_WhenUnsupportedMethod() {
//        when(paymentRepository.findByOrderId(101L)).thenReturn(Optional.empty());
//        request.setPaymentMethod("BITCOIN");
//        request.setPaymentDetails(new HashMap<>());
//        Exception ex = assertThrows(IllegalArgumentException.class, () -> paymentService.processPayment(request));
//        assertTrue(ex.getMessage().contains("Unsupported payment method"));
//    }
//
//    @Test
//    void updatePaymentStatusToSuccess_ShouldUpdateStatus_WhenPending() {
//        payment.setStatus("PENDING");
//        when(paymentRepository.findByOrderId(101L)).thenReturn(Optional.of(payment));
//        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);
//
//        paymentService.updatePaymentStatusToSuccess(101L);
//
//        assertEquals("SUCCESS", payment.getStatus());
//        verify(paymentRepository, times(1)).save(payment);
//    }
//
//    @Test
//    void updatePaymentStatusToSuccess_ShouldThrowException_WhenNotFound() {
//        when(paymentRepository.findByOrderId(101L)).thenReturn(Optional.empty());
//        Exception ex = assertThrows(IllegalArgumentException.class, () -> paymentService.updatePaymentStatusToSuccess(101L));
//        assertTrue(ex.getMessage().contains("No payment found for this order."));
//    }
//
//    @Test
//    void updatePaymentStatusToSuccess_ShouldThrowException_WhenNotPending() {
//        payment.setStatus("SUCCESS");
//        when(paymentRepository.findByOrderId(101L)).thenReturn(Optional.of(payment));
//        Exception ex = assertThrows(IllegalStateException.class, () -> paymentService.updatePaymentStatusToSuccess(101L));
//        assertTrue(ex.getMessage().contains("Payment is not in PENDING state."));
//    }
//
//    @Test
//    void checkIfPaymentExists_ShouldThrowException_WhenExists() {
//        when(paymentRepository.findByOrderId(101L)).thenReturn(Optional.of(payment));
//        Exception ex = assertThrows(IllegalArgumentException.class, () -> paymentService.checkIfPaymentExists(101L));
//        assertTrue(ex.getMessage().contains("A payment already exists for this order ID."));
//    }
//
//    @Test
//    void checkIfPaymentExists_ShouldNotThrow_WhenNotExists() {
//        when(paymentRepository.findByOrderId(101L)).thenReturn(Optional.empty());
//        assertDoesNotThrow(() -> paymentService.checkIfPaymentExists(101L));
//    }
//
//    @Test
//    void getAvailableMethods_ShouldReturnList() {
//        List<String> methods = paymentService.getAvailableMethods();
//        assertThat(methods).contains("CREDIT_CARD", "DEBIT_CARD", "UPI", "WALLET", "NET_BANKING", "COD");
//    }
//
//    @Test
//    void getPaymentByOrderId_ShouldReturnOptional() {
//        when(paymentRepository.findByOrderId(101L)).thenReturn(Optional.of(payment));
//        Optional<Payment> result = paymentService.getPaymentByOrderId(101L);
//        assertTrue(result.isPresent());
//        assertEquals(101L, result.get().getOrderId());
//    }
//
//    @Test
//    void getPaymentsByUserId_ShouldReturnList() {
//        when(paymentRepository.findByUserId(201L)).thenReturn(List.of(payment));
//        List<Payment> result = paymentService.getPaymentsByUserId(201L);
//        assertEquals(1, result.size());
//        assertEquals(201L, result.get(0).getUserId());
//    }
//    
//    @Test
//    void processPayment_ShouldThrowException_WhenCreditCardInvalidExpiry() {
//        when(paymentRepository.findByOrderId(101L)).thenReturn(Optional.empty());
//        request.setPaymentMethod("CREDIT_CARD");
//        Map<String, Object> details = new HashMap<>();
//        details.put("cardNumber", "1234567812345678");
//        details.put("cvv", "123");
//        details.put("expiry", "13/25"); // invalid month
//        details.put("cardHolderName", "John Doe");
//        request.setPaymentDetails(details);
//
//        Exception ex = assertThrows(IllegalArgumentException.class, () -> paymentService.processPayment(request));
//        assertTrue(ex.getMessage().contains("Invalid or missing expiry date"));
//    }
//
//    @Test
//    void processPayment_ShouldThrowException_WhenCreditCardMissingName() {
//        when(paymentRepository.findByOrderId(101L)).thenReturn(Optional.empty());
//        request.setPaymentMethod("CREDIT_CARD");
//        Map<String, Object> details = new HashMap<>();
//        details.put("cardNumber", "1234567812345678");
//        details.put("cvv", "123");
//        details.put("expiry", "12/25");
//        details.put("cardHolderName", "");
//        request.setPaymentDetails(details);
//
//        Exception ex = assertThrows(IllegalArgumentException.class, () -> paymentService.processPayment(request));
//        assertTrue(ex.getMessage().contains("Cardholder name is required"));
//    }
//
//    @Test
//    void processPayment_ShouldThrowException_WhenNetBankingInvalidIFSC() {
//        when(paymentRepository.findByOrderId(101L)).thenReturn(Optional.empty());
//        request.setPaymentMethod("NET_BANKING");
//        Map<String, Object> details = new HashMap<>();
//        details.put("bankName", "HDFC");
//        details.put("accountNumber", "123456789012");
//        details.put("ifscCode", "INVALID123"); // invalid IFSC
//        request.setPaymentDetails(details);
//
//        Exception ex = assertThrows(IllegalArgumentException.class, () -> paymentService.processPayment(request));
//        assertTrue(ex.getMessage().contains("Invalid or missing IFSC code"));
//    }
//
//    @Test
//    void maskName_ShouldHandleNullAndBlank() throws Exception {
//        // Use reflection to call private method
//        var method = PaymentServiceImpl.class.getDeclaredMethod("maskName", String.class);
//        method.setAccessible(true);
//        assertEquals("", method.invoke(paymentService, (String) null));
//        assertEquals("", method.invoke(paymentService, ""));
//        assertEquals("J***", method.invoke(paymentService, "John"));
//        assertEquals("J*** D**", method.invoke(paymentService, "John Doe"));
//    }
//
//    @Test
//    void maskUpiId_ShouldHandleNullAndShort() throws Exception {
//        var method = PaymentServiceImpl.class.getDeclaredMethod("maskUpiId", String.class);
//        method.setAccessible(true);
//        assertEquals("****", method.invoke(paymentService, (String) null));
//        assertEquals("****", method.invoke(paymentService, "invalidupi"));
//        assertEquals("jo***@upi", method.invoke(paymentService, "john@upi"));
//        assertEquals("j*@upi", method.invoke(paymentService, "j@upi"));
//    }
//    
//    @Test
//    void processPayment_ShouldThrowException_WhenCreditCardInvalidCardNumber() {
//        when(paymentRepository.findByOrderId(101L)).thenReturn(Optional.empty());
//        request.setPaymentMethod("CREDIT_CARD");
//        Map<String, Object> details = new HashMap<>();
//        details.put("cardNumber", "1234"); // invalid
//        details.put("cvv", "123");
//        details.put("expiry", "12/25");
//        details.put("cardHolderName", "John Doe");
//        request.setPaymentDetails(details);
//
//        Exception ex = assertThrows(IllegalArgumentException.class, () -> paymentService.processPayment(request));
//        assertTrue(ex.getMessage().contains("Invalid or missing card number"));
//    }
//
//    @Test
//    void processPayment_ShouldThrowException_WhenCreditCardInvalidCVV() {
//        when(paymentRepository.findByOrderId(101L)).thenReturn(Optional.empty());
//        request.setPaymentMethod("CREDIT_CARD");
//        Map<String, Object> details = new HashMap<>();
//        details.put("cardNumber", "1234567812345678");
//        details.put("cvv", "12"); // invalid
//        details.put("expiry", "12/25");
//        details.put("cardHolderName", "John Doe");
//        request.setPaymentDetails(details);
//
//        Exception ex = assertThrows(IllegalArgumentException.class, () -> paymentService.processPayment(request));
//        assertTrue(ex.getMessage().contains("Invalid or missing CVV"));
//    }
//    
//    @Test
//    void processPayment_ShouldThrowException_WhenNetBankingInvalidAccountNumber() {
//        when(paymentRepository.findByOrderId(101L)).thenReturn(Optional.empty());
//        request.setPaymentMethod("NET_BANKING");
//        Map<String, Object> details = new HashMap<>();
//        details.put("bankName", "HDFC");
//        details.put("accountNumber", "123"); // invalid
//        details.put("ifscCode", "HDFC0001234");
//        request.setPaymentDetails(details);
//
//        Exception ex = assertThrows(IllegalArgumentException.class, () -> paymentService.processPayment(request));
//        assertTrue(ex.getMessage().contains("Invalid or missing account number"));
//    }
//    
//    @Test
//    void processPayment_ShouldThrowException_WhenNetBankingMissingBankName() {
//        when(paymentRepository.findByOrderId(101L)).thenReturn(Optional.empty());
//        request.setPaymentMethod("NET_BANKING");
//        Map<String, Object> details = new HashMap<>();
//        // missing bankName
//        details.put("accountNumber", "123456789012");
//        details.put("ifscCode", "HDFC0001234");
//        request.setPaymentDetails(details);
//
//        Exception ex = assertThrows(IllegalArgumentException.class, () -> paymentService.processPayment(request));
//        assertTrue(ex.getMessage().contains("Bank name is required"));
//    }
//    
//    @Test
//    void processPayment_ShouldThrowException_WhenWalletBlankField() {
//        when(paymentRepository.findByOrderId(101L)).thenReturn(Optional.empty());
//        request.setPaymentMethod("WALLET");
//        Map<String, Object> details = new HashMap<>();
//        details.put("walletId", "   "); // blank
//        request.setPaymentDetails(details);
//
//        Exception ex = assertThrows(IllegalArgumentException.class, () -> paymentService.processPayment(request));
//        assertTrue(ex.getMessage().contains("Wallet ID is required"));
//    }
//    
//    @Test
//    void processPayment_ShouldThrowException_WhenUPINull() {
//        when(paymentRepository.findByOrderId(101L)).thenReturn(Optional.empty());
//        request.setPaymentMethod("UPI");
//        Map<String, Object> details = new HashMap<>();
//        details.put("upiId", null);
//        request.setPaymentDetails(details);
//
//        Exception ex = assertThrows(IllegalArgumentException.class, () -> paymentService.processPayment(request));
//        assertTrue(ex.getMessage().contains("Invalid or missing UPI ID"));
//    }
//    
//    @Test
//    void maskName_ShouldHandleMultipleSpaces() throws Exception {
//        var method = PaymentServiceImpl.class.getDeclaredMethod("maskName", String.class);
//        method.setAccessible(true);
//        assertEquals("J*** D**", method.invoke(paymentService, " John   Doe "));
//    }
//
//    
//    @Test
//    void maskUpiId_ShouldHandleShortUser() throws Exception {
//        var method = PaymentServiceImpl.class.getDeclaredMethod("maskUpiId", String.class);
//        method.setAccessible(true);
//        assertEquals("a*@upi", method.invoke(paymentService, "a@upi"));
//        assertEquals("a*@upi", method.invoke(paymentService, "ab@upi"));
//        assertEquals("jo***@upi", method.invoke(paymentService, "john@upi"));
//    }
//    @Test
//    void getPaymentsByUserId_ShouldReturnEmptyList() {
//        when(paymentRepository.findByUserId(999L)).thenReturn(Collections.emptyList());
//        List<Payment> result = paymentService.getPaymentsByUserId(999L);
//        assertTrue(result.isEmpty());
//    }
//}