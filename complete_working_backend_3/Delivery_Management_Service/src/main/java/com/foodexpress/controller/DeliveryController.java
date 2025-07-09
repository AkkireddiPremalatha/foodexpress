package com.foodexpress.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.foodexpress.dto.AssignDeliveryRequestDTO;
import com.foodexpress.dto.DeliveryStatusDTO;
import com.foodexpress.dto.PartnerDTO;
import com.foodexpress.entity.AssignedDeliveryPartner;
import com.foodexpress.entity.Delivery;
import com.foodexpress.enums.Status;
import com.foodexpress.service.DeliveryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@CrossOrigin("*")
public class DeliveryController {

    
    private final DeliveryService deliveryService;

    @Operation(summary = "Assign a delivery to a delivery agent", description = "Assigns a delivery agent to a specific order.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Delivery agent assigned successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/communication/assign-delivery")
    public ResponseEntity<String> assignDelivery(@RequestBody AssignDeliveryRequestDTO assignDeliveryRequestDTO) {
        deliveryService.assignDelivery(assignDeliveryRequestDTO);
        return ResponseEntity.ok("Delivery agent assigned successfully.");
    }

    @Operation(summary = "Get delivery status", description = "Retrieves the current status of a delivery.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Delivery status retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "404", description = "Delivery not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/deliveryAgent/delivery-status")
    public ResponseEntity<String> getDeliveryStatus(@Valid @RequestBody AssignDeliveryRequestDTO request) {
        String deliveryStatusDTO = deliveryService.getDeliveryStatus(request);
        return ResponseEntity.ok(deliveryStatusDTO);
    }

    @Operation(summary = "Update delivery status", description = "Updates the status of a delivery.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Delivery status updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "404", description = "Delivery not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/deliveryAgent/update-delivery-status")
    public ResponseEntity<String> updateDeliveryStatus(@Valid @RequestBody DeliveryStatusDTO request) {
        deliveryService.updateDeliveryStatus(request);
        return ResponseEntity.ok("Delivery status updated successfully.");
    }

    @Operation(summary = "Notify customer about delivery", description = "Sends a notification to the customer regarding the delivery status.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Customer notified successfully"),
        @ApiResponse(responseCode = "404", description = "Order not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/deliveryAgent/notify-customer/{orderId}")
    public ResponseEntity<String> notifyCustomer(@PathVariable Long orderId) {
        deliveryService.notifyCustomer(orderId);
        return ResponseEntity.ok("Customer notified successfully.");
    }
    
    
    @GetMapping("/communication/getDeliveryAgentStatus/{customerPincode}")
    public boolean getDeliveryAgentStatus(@PathVariable String customerPincode) {
    	return deliveryService.getDeliveryAgentStatus(customerPincode);
    	
    }
    
    @PostMapping("/communication/createDeliveryPartner")
    public ResponseEntity<PartnerDTO> createDeliveryPartner(@RequestBody PartnerDTO partnerDTO){
    	return deliveryService.createDeliveryPartner(partnerDTO);
    }
    
    @PutMapping("/communication/updateOrderStatus/{deliveryId}/{status}")
    public void updateOrderStatus(@PathVariable int deliveryId,@PathVariable Status status) {
    	 deliveryService.updateOrderStatus(deliveryId,status);
    }
    
    @GetMapping("/customer/deliveryDetails/{orderId}")
    public ResponseEntity<Delivery> getDeliveryDetails(@PathVariable Long orderId) {
    	return ResponseEntity.ok(deliveryService.getDeliveryDetails(orderId));
    }
    
    @GetMapping("/deliveryAgent/currentOrder/{partnerId}")
    public ResponseEntity<Delivery> getCurrentOrder(@PathVariable int partnerId) {
        return ResponseEntity.ok(deliveryService.getDeliveryByPartnerId(partnerId));
    }
    
    @GetMapping("/deliveryAgent/partnerId/{userId}")
    public ResponseEntity<AssignedDeliveryPartner> getPartnersByUserId(@PathVariable int userId) {
    	return ResponseEntity.ok(deliveryService.getPartnersByUserId(userId));
    }
    
    @GetMapping("/deliveryAgent/getDeliveries/{partnerId}")
    public ResponseEntity< List<Delivery>> getPreviousDeliveries(@PathVariable int partnerId) {
        return ResponseEntity.ok(deliveryService.getPreviousDeliveries(partnerId));
    }

}