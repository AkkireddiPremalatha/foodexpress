//package com.foodexpress.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.foodexpress.dto.GenericPaymentRequestDTO;
//import com.foodexpress.entity.Payment;
//import com.foodexpress.exception.GlobalExceptionHandler;
//import com.foodexpress.service.PaymentService;
//import org.junit.jupiter.api.*;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.*;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.*;
//
//import java.util.*;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
//@ExtendWith(MockitoExtension.class)
//class PaymentControllerTest {
//
//    @Mock
//    private PaymentService paymentService;
//
//    @InjectMocks
//    private PaymentController paymentController;
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    private Payment payment;
//    private List<Payment> paymentList;
//
//    @BeforeEach
//    void setUp() {
//        payment = new Payment();
//        payment.setOrderId(1L);
//        payment.setUserId(2L);
//        payment.setAmount(100.0);
//        payment.setPaymentMethod("UPI");
//        payment.setStatus("SUCCESS");
//        paymentList = List.of(payment);
//        mockMvc = MockMvcBuilders
//            .standaloneSetup(paymentController)
//            .setControllerAdvice(new GlobalExceptionHandler()) // <-- add this line
//            .build();
//    }
//
//    @Test
//    void testGetAvailableMethods_Success() throws Exception {
//        when(paymentService.getAvailableMethods()).thenReturn(List.of("UPI", "CREDIT_CARD"));
//        mockMvc.perform(get("/api/v1/customer/payment/methods"))
//                .andExpect(status().isOk());
//        verify(paymentService, times(1)).getAvailableMethods();
//    }
//
//    @Test
//    void testProcessPayment_Success() throws Exception {
//        GenericPaymentRequestDTO dto = new GenericPaymentRequestDTO();
//        dto.setOrderId(1L);
//        dto.setUserId(2L);
//        dto.setAmount(100.0);
//        dto.setPaymentMethod("UPI");
//
//        when(paymentService.processPayment(any())).thenReturn(payment);
//
//        mockMvc.perform(post("/api/v1/customer/payment/methods/pay")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(asJsonString(dto)))
//                .andExpect(status().isCreated());
//        verify(paymentService, times(1)).processPayment(any());
//    }
//
//    @Test
//    void testConfirmCODPayment_Success() throws Exception {
//        doNothing().when(paymentService).updatePaymentStatusToSuccess(1L);
//        mockMvc.perform(put("/api/v1/customer/payment/methods/cod/confirm/1"))
//                .andExpect(status().isOk());
//        verify(paymentService, times(1)).updatePaymentStatusToSuccess(1L);
//    }
//
//    @Test
//    void testConfirmCODPayment_Failure() throws Exception {
//        doThrow(new IllegalArgumentException("No payment found")).when(paymentService).updatePaymentStatusToSuccess(1L);
//        mockMvc.perform(put("/api/v1/customer/payment/methods/cod/confirm/1"))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    void testGetPaymentsByUserId_Success() throws Exception {
//        when(paymentService.getPaymentsByUserId(2L)).thenReturn(paymentList);
//        mockMvc.perform(get("/api/v1/customer/payment/user/2"))
//                .andExpect(status().isOk());
//        verify(paymentService, times(1)).getPaymentsByUserId(2L);
//    }
//
//    @Test
//    void testGetPaymentByOrderId_Found() throws Exception {
//        when(paymentService.getPaymentByOrderId(1L)).thenReturn(Optional.of(payment));
//        mockMvc.perform(get("/api/v1/customer/payment/order/1"))
//                .andExpect(status().isOk());
//        verify(paymentService, times(1)).getPaymentByOrderId(1L);
//    }
//
//    @Test
//    void testGetPaymentByOrderId_NotFound() throws Exception {
//        when(paymentService.getPaymentByOrderId(1L)).thenReturn(Optional.empty());
//        mockMvc.perform(get("/api/v1/customer/payment/order/1"))
//                .andExpect(status().isNotFound());
//    }
//
//    // Utility for JSON conversion
//    public static String asJsonString(final Object obj) {
//        try {
//            return new ObjectMapper().writeValueAsString(obj);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//    
// // Add these to your PaymentControllerTest class
//
//    @Test
//    void testProcessPayment_BadRequestOnException() throws Exception {
//        GenericPaymentRequestDTO dto = new GenericPaymentRequestDTO();
//        dto.setOrderId(1L);
//        dto.setUserId(2L);
//        dto.setAmount(100.0);
//        dto.setPaymentMethod("UPI");
//
//        when(paymentService.processPayment(any())).thenThrow(new IllegalArgumentException("Invalid payment"));
//
//        mockMvc.perform(post("/api/v1/customer/payment/methods/pay")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(asJsonString(dto)))
//                .andExpect(status().isBadRequest());
//        verify(paymentService, times(1)).processPayment(any());
//    }
//
//    @Test
//    void testConfirmCODPayment_IllegalStateException() throws Exception {
//        doThrow(new IllegalStateException("Already confirmed")).when(paymentService).updatePaymentStatusToSuccess(1L);
//        mockMvc.perform(put("/api/v1/customer/payment/methods/cod/confirm/1"))
//                .andExpect(status().isBadRequest());
//        verify(paymentService, times(1)).updatePaymentStatusToSuccess(1L);
//    }
//
//    @Test
//    void testGetAvailableMethods_ServiceUnavailable() throws Exception {
//        // Simulate unavailable delivery/restaurant by using a spy and reflection to set the flags if possible.
//        // If not possible, just check the normal path is covered.
//        // This is a placeholder for branch coverage if you refactor the controller for testability.
//        // Otherwise, this test will be similar to testGetAvailableMethods_Success.
//        when(paymentService.getAvailableMethods()).thenReturn(Collections.emptyList());
//        mockMvc.perform(get("/api/v1/customer/payment/methods"))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    void testProcessPayment_InvalidJson() throws Exception {
//        // Send invalid JSON to trigger a 400 Bad Request
//        mockMvc.perform(post("/api/v1/customer/payment/methods/pay")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content("{invalidJson:}"))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    void testGetPaymentsByUserId_EmptyList() throws Exception {
//        when(paymentService.getPaymentsByUserId(999L)).thenReturn(Collections.emptyList());
//        mockMvc.perform(get("/api/v1/customer/payment/user/999"))
//                .andExpect(status().isOk());
//        verify(paymentService, times(1)).getPaymentsByUserId(999L);
//    }
//    
//    @Test
//    void testProcessPayment_NullBody() throws Exception {
//        mockMvc.perform(post("/api/v1/customer/payment/methods/pay")
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    void testUnsupportedHttpMethod() throws Exception {
//        mockMvc.perform(delete("/api/v1/customer/payment/methods/pay"))
//                .andExpect(status().isMethodNotAllowed());
//    }
//
//    @Test
//    void testProcessPayment_RuntimeException() throws Exception {
//        GenericPaymentRequestDTO dto = new GenericPaymentRequestDTO();
//        dto.setOrderId(1L);
//        dto.setUserId(2L);
//        dto.setAmount(100.0);
//        dto.setPaymentMethod("UPI");
//
//        when(paymentService.processPayment(any())).thenThrow(new RuntimeException("Unexpected error"));
//
//        mockMvc.perform(post("/api/v1/customer/payment/methods/pay")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(asJsonString(dto)))
//                .andExpect(status().isInternalServerError());
//    }
//
//    @Test
//    void testProcessPayment_ReturnsNull() throws Exception {
//        GenericPaymentRequestDTO dto = new GenericPaymentRequestDTO();
//        dto.setOrderId(1L);
//        dto.setUserId(2L);
//        dto.setAmount(100.0);
//        dto.setPaymentMethod("UPI");
//
//        when(paymentService.processPayment(any())).thenReturn(null);
//
//        mockMvc.perform(post("/api/v1/customer/payment/methods/pay")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(asJsonString(dto)))
//                .andExpect(status().isCreated())
//                .andExpect(content().string(""));
//    }
//
//    @Test
//    void testGetPaymentByOrderId_NullReturn() throws Exception {
//    	when(paymentService.getPaymentByOrderId(123L)).thenReturn(Optional.empty());
//        mockMvc.perform(get("/api/v1/customer/payment/order/123"))
//                .andExpect(status().isNotFound());
//    }
//    
//    @Test
//    void testGetAvailableMethods_InternalServerError() throws Exception {
//        when(paymentService.getAvailableMethods()).thenThrow(new RuntimeException("Service error"));
//        mockMvc.perform(get("/api/v1/customer/payment/methods"))
//                .andExpect(status().isInternalServerError());
//    }
//
//    @Test
//    void testGetPaymentsByUserId_ServiceThrowsException() throws Exception {
//        when(paymentService.getPaymentsByUserId(2L)).thenThrow(new IllegalArgumentException("Invalid user"));
//        mockMvc.perform(get("/api/v1/customer/payment/user/2"))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    void testGetPaymentByOrderId_ServiceThrowsException() throws Exception {
//        when(paymentService.getPaymentByOrderId(1L)).thenThrow(new IllegalArgumentException("Invalid order"));
//        mockMvc.perform(get("/api/v1/customer/payment/order/1"))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    void testConfirmCODPayment_RuntimeException() throws Exception {
//        doThrow(new RuntimeException("Unexpected error")).when(paymentService).updatePaymentStatusToSuccess(1L);
//        mockMvc.perform(put("/api/v1/customer/payment/methods/cod/confirm/1"))
//                .andExpect(status().isInternalServerError());
//    }
//
//    @Test
//    void testProcessPayment_PaymentTimeoutException() throws Exception {
//        GenericPaymentRequestDTO dto = new GenericPaymentRequestDTO();
//        dto.setOrderId(1L);
//        dto.setUserId(2L);
//        dto.setAmount(100.0);
//        dto.setPaymentMethod("UPI");
//
//        when(paymentService.processPayment(any())).thenThrow(new com.foodexpress.exception.PaymentTimeoutException("Timeout"));
//
//        mockMvc.perform(post("/api/v1/customer/payment/methods/pay")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(asJsonString(dto)))
//                .andExpect(status().isRequestTimeout());
//    }
//    
//    @Test
//    void testGetPaymentsByUserId_NullReturn() throws Exception {
//        when(paymentService.getPaymentsByUserId(2L)).thenReturn(null);
//        mockMvc.perform(get("/api/v1/customer/payment/user/2"))
//                .andExpect(status().isOk());
//    }
//    
//    @Test
//    void testGetAvailableMethods_NullReturn() throws Exception {
//        when(paymentService.getAvailableMethods()).thenReturn(null);
//        mockMvc.perform(get("/api/v1/customer/payment/methods"))
//                .andExpect(status().isOk());
//    }
//    
//    @Test
//    void testHeadRequestToGetAvailableMethods() throws Exception {
//        when(paymentService.getAvailableMethods()).thenReturn(List.of("UPI", "CREDIT_CARD"));
//        mockMvc.perform(head("/api/v1/customer/payment/methods"))
//                .andExpect(status().isOk());
//    }
//    
//    @Test
//    void testOptionsRequestToGetAvailableMethods() throws Exception {
//        mockMvc.perform(options("/api/v1/customer/payment/methods"))
//                .andExpect(status().isOk());
//    }
//    
//    @Test
//    void testProcessPayment_LargePayload() throws Exception {
//        GenericPaymentRequestDTO dto = new GenericPaymentRequestDTO();
//        dto.setOrderId(1L);
//        dto.setUserId(2L);
//        dto.setAmount(Double.MAX_VALUE);
//        dto.setPaymentMethod("UPI");
//
//        when(paymentService.processPayment(any())).thenReturn(payment);
//
//        mockMvc.perform(post("/api/v1/customer/payment/methods/pay")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(asJsonString(dto)))
//                .andExpect(status().isCreated());
//    }
//    
//    @Test
//    void testGetPaymentByOrderId_InvalidPathVariable() throws Exception {
//        mockMvc.perform(get("/api/v1/customer/payment/order/invalid"))
//                .andExpect(status().isBadRequest());
//    }
//}