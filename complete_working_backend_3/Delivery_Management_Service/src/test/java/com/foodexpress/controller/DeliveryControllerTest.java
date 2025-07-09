//package com.foodexpress.controller;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.Mockito.doNothing;
//import static org.mockito.Mockito.doThrow;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.http.ResponseEntity;
//
//import com.foodexpress.dto.AssignDeliveryRequestDTO;
//import com.foodexpress.dto.DeliveryDTO;
//import com.foodexpress.dto.DeliveryStatusDTO;
//import com.foodexpress.exception.ResourceNotFoundException;
//import com.foodexpress.service.DeliveryService;
//
//class DeliveryControllerTest {
//
//    @InjectMocks
//    private DeliveryController deliveryController;
//
//    @Mock
//    private DeliveryService deliveryService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    // Test case for assignDelivery
//    @Test
//    void testAssignDelivery_Success() {
//        AssignDeliveryRequestDTO request = new AssignDeliveryRequestDTO();
//        request.setOrderId(1);
//        request.setCustomerPincode("560001");
//
//        doNothing().when(deliveryService).assignDelivery(request);
//
//        ResponseEntity<String> response = deliveryController.assignDelivery(request);
//
//        assertEquals(200, response.getStatusCodeValue());
//        assertEquals("Delivery agent assigned successfully.", response.getBody());
//        verify(deliveryService, times(1)).assignDelivery(request);
//    }
//
//    @Test
//    void testAssignDelivery_NoAgentsAvailable() {
//        AssignDeliveryRequestDTO request = new AssignDeliveryRequestDTO();
//        request.setOrderId(1);
//        request.setCustomerPincode("560001");
//
//        doThrow(new ResourceNotFoundException("No delivery agents available"))
//                .when(deliveryService).assignDelivery(request);
//
//        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
//            deliveryController.assignDelivery(request);
//        });
//
//        assertEquals("No delivery agents available", exception.getMessage());
//        verify(deliveryService, times(1)).assignDelivery(request);
//    }
//
//    // Test case for getDeliveryStatus
////    @Test
////    void testGetDeliveryStatus_Success() {
////        DeliveryDTO request = new DeliveryDTO();
////        request.setOrderId(1);
////
////        AssignDeliveryRequestDTO statusDTO = new AssignDeliveryRequestDTO();
////        statusDTO.setOrderId(1);
////        statusDTO.setDeliveryStatus("In Progress");
////
////        when(deliveryService.getDeliveryStatus(request)).thenReturn(statusDTO);
////
////        ResponseEntity<DeliveryStatusDTO> response = deliveryController.getDeliveryStatus(request);
////
////        assertEquals(200, response.getStatusCodeValue());
////        assertEquals("In Progress", response.getBody().getDeliveryStatus());
////        verify(deliveryService, times(1)).getDeliveryStatus(request);
////    }
////
////    @Test
////    void testGetDeliveryStatus_OrderNotFound() {
////        DeliveryDTO request = new DeliveryDTO();
////        request.setOrderId(1);
////
////        when(deliveryService.getDeliveryStatus(request))
////                .thenThrow(new ResourceNotFoundException("Order not found"));
////
////        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
////            deliveryController.getDeliveryStatus(request);
////        });
////
////        assertEquals("Order not found", exception.getMessage());
////        verify(deliveryService, times(1)).getDeliveryStatus(request);
////    }
//
//    // Test case for updateDeliveryStatus
//    @Test
//    void testUpdateDeliveryStatus_Success() {
//        DeliveryStatusDTO request = new DeliveryStatusDTO();
//        request.setOrderId(1);
//        request.setDeliveryStatus("Delivered");
//
//        doNothing().when(deliveryService).updateDeliveryStatus(request);
//
//        ResponseEntity<String> response = deliveryController.updateDeliveryStatus(request);
//
//        assertEquals(200, response.getStatusCodeValue());
//        assertEquals("Delivery status updated successfully.", response.getBody());
//        verify(deliveryService, times(1)).updateDeliveryStatus(request);
//    }
//
//    @Test
//    void testUpdateDeliveryStatus_InvalidStatus() {
//        DeliveryStatusDTO request = new DeliveryStatusDTO();
//        request.setOrderId(1);
//        request.setDeliveryStatus("InvalidStatus");
//
//        doThrow(new IllegalArgumentException("Invalid delivery status"))
//                .when(deliveryService).updateDeliveryStatus(request);
//
//        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
//            deliveryController.updateDeliveryStatus(request);
//        });
//
//        assertEquals("Invalid delivery status", exception.getMessage());
//        verify(deliveryService, times(1)).updateDeliveryStatus(request);
//    }
//
//    // Test case for notifyCustomer
//    @Test
//    void testNotifyCustomer_Success() {
//        Integer orderId = 1;
//
//        doNothing().when(deliveryService).notifyCustomer(orderId);
//
//        ResponseEntity<String> response = deliveryController.notifyCustomer(orderId);
//
//        assertEquals(200, response.getStatusCodeValue());
//        assertEquals("Customer notified successfully.", response.getBody());
//        verify(deliveryService, times(1)).notifyCustomer(orderId);
//    }
//
//    @Test
//    void testNotifyCustomer_OrderNotFound() {
//        Integer orderId = 1;
//
//        doThrow(new ResourceNotFoundException("Order not found"))
//                .when(deliveryService).notifyCustomer(orderId);
//
//        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
//            deliveryController.notifyCustomer(orderId);
//        });
//
//        assertEquals("Order not found", exception.getMessage());
//        verify(deliveryService, times(1)).notifyCustomer(orderId);
//    }
//}