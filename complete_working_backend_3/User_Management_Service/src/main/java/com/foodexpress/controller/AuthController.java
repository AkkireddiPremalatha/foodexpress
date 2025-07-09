package com.foodexpress.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.foodexpress.dto.CustomerProfileDto;
import com.foodexpress.dto.CustomerRegisterRequestDto;
import com.foodexpress.dto.CustomerResponseDto;
import com.foodexpress.dto.DeliveryAgentRequestDto;
import com.foodexpress.dto.DeliveryAgentResponseDto;
import com.foodexpress.dto.LoginRequest;
import com.foodexpress.dto.RestaurantOwnerRequestDto;
import com.foodexpress.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {


    private final AuthService authService;
    

    @Operation(summary = "Register a new customer", description = "Registers a new customer with the provided details.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Customer registered successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/register/customer")
    public ResponseEntity<CustomerRegisterRequestDto> registerCustomer(@Valid @RequestBody CustomerRegisterRequestDto customerDto) {
        log.info("Registering customer with email: {}", customerDto.getEmail());
        CustomerRegisterRequestDto responseDto = authService.registerCustomer(customerDto);
        return ResponseEntity.ok(responseDto);
    }

    @Operation(summary = "Register a new delivery agent", description = "Registers a new delivery agent with the provided details.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Delivery agent registered successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/register/deliveryAgent")
    public ResponseEntity<DeliveryAgentRequestDto> registerDeliveryAgents(@Valid @RequestBody DeliveryAgentRequestDto deliveryAgentDto) {
        log.info("Registering delivery agent with email: {}", deliveryAgentDto.getEmail());
        DeliveryAgentRequestDto responseDto = authService.registerDeliveryAgents(deliveryAgentDto);
        return ResponseEntity.ok(responseDto);
    }

    @Operation(summary = "Register a new restaurant owner", description = "Registers a new restaurant owner with the provided details.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Restaurant owner registered successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/register/restaurantOwner")
    public ResponseEntity<RestaurantOwnerRequestDto> registerRestaurantOwner(@Valid @RequestBody RestaurantOwnerRequestDto restaurantOwnerDto) {
        log.info("Registering restaurant owner with email: {}", restaurantOwnerDto.getEmail());
        return ResponseEntity.ok(authService.registerRestaurantOwner(restaurantOwnerDto));
    }

    @Operation(summary = "login", description = "Allows any role to log in using their email and password.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Admin logged in successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid email or password"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequest loginRequest) {
        Map<String, Object> loginData = authService.login(loginRequest.getEmail(), loginRequest.getPassword());
        return ResponseEntity.ok(loginData);
    }
    @Operation(summary = "Test endpoint", description = "A test endpoint accessible by anyone.")
    @GetMapping("/test")
    public String test() {
        log.info("Test endpoint accessed");
        return "hello";
    }

    @Operation(summary = "Admin test endpoint", description = "A test endpoint accessible only by admins.")
    @GetMapping("/admin/test")
    public String test1() {
        log.info("Admin test endpoint accessed");
        return "Hello Admin";
    }

    @Operation(summary = "Customer test endpoint", description = "A test endpoint accessible only by customers.")
    @GetMapping("/customer/test")
    public String test3() {
        log.info("Customer test endpoint accessed");
        return "Hello Customer";
    }

    @Operation(summary = "Delivery agent test endpoint", description = "A test endpoint accessible only by delivery agents.")
    @GetMapping("/deliveryAgent/test")
    public String test4() {
        log.info("Delivery Agent test endpoint accessed");
        return "Hello Delivery Agent";
    }

    @Operation(summary = "Public test endpoint", description = "A test endpoint accessible by anyone.")
    @GetMapping("/test2")
    public String test2() {
        log.info("Public test endpoint accessed");
        return "Hello All";
    }
    
    
    @GetMapping("/customer/viewProfile/{customerId}")
    public ResponseEntity<CustomerProfileDto> viewCustomerProfile(@Valid @PathVariable Long customerId) {
    	CustomerProfileDto customerProfile = authService.viewCustomerProfile(customerId);
        return ResponseEntity.ok(customerProfile);
    }

    @PutMapping("/customer/updateProfile/{customerId}")
    public ResponseEntity<String> updateCustomerProfile(@Valid @PathVariable Long customerId,@Valid @RequestBody CustomerProfileDto customerProfileDto) {
    	authService.updateCustomerProfile(customerId, customerProfileDto);
        return ResponseEntity.ok("Customer profile updated successfully");
    }


    @GetMapping("/deliveryAgent/viewProfile/{agentId}")
    public ResponseEntity<DeliveryAgentRequestDto> viewDeliveryAgentProfile(@Valid @PathVariable int agentId) {
    	DeliveryAgentRequestDto deliveryAgentProfile = authService.viewDeliveryAgentProfile(agentId);
        return ResponseEntity.ok(deliveryAgentProfile);
    }

    @PutMapping("/deliveryAgent/updateProfile/{agentId}")
    public ResponseEntity<String> updateDeliveryAgentProfile(@Valid @PathVariable int agentId, @Valid @RequestBody DeliveryAgentRequestDto deliveryAgentProfileDto) {
    	authService.updateDeliveryAgentProfile(agentId, deliveryAgentProfileDto);
        return ResponseEntity.ok("Delivery Agent profile updated successfully");
    }

    @GetMapping("/restaurantOwner/viewProfile/{ownerId}")
    public ResponseEntity<RestaurantOwnerRequestDto> viewRestaurantOwnerProfile(@Valid @PathVariable int ownerId) {
    	RestaurantOwnerRequestDto restaurantOwnerProfile = authService.viewRestaurantOwnerProfile(ownerId);
        return ResponseEntity.ok(restaurantOwnerProfile);
    }

    @PutMapping("/restaurantOwner/updateProfile/{ownerId}")
    public ResponseEntity<String> updateRestaurantOwnerProfile(@Valid @PathVariable int ownerId, @Valid @RequestBody RestaurantOwnerRequestDto restaurantOwnerProfileDto) {
    	authService.updateRestaurantOwnerProfile(ownerId, restaurantOwnerProfileDto);
        return ResponseEntity.ok("Restaurant Owner profile updated successfully");
    }
    
    @GetMapping("/delivery-partners")
    public ResponseEntity<List<DeliveryAgentResponseDto>> getAllDeliveryAgents() {
        List<DeliveryAgentResponseDto> agents = authService.getAllDeliveryPartners();
        return ResponseEntity.ok(agents);
    }
    
    @GetMapping("/customers/{customerId}")
    public CustomerResponseDto getCustomerById(@Valid @PathVariable Long customerId) {
        return authService.getCustomerById(customerId);
    }
}