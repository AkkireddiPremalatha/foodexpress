package com.foodexpress.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.foodexpress.dto.GenericPaymentRequestDTO;
import com.foodexpress.dto.OrderResponseDTO;
import com.foodexpress.enums.Status;
import com.foodexpress.model.Order;
import com.foodexpress.service.OrderService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin("*")
public class OrderController {

	private final OrderService orderService;

	@GetMapping("communication/getOrdersBasedOnPaymentStatusAndMethod")
	@Operation(summary = "List of orders", description = "Retrieves all orders with payment method as COD or Payment status is successful")

	public ResponseEntity<List<OrderResponseDTO>> getAllOrdersWithCODAndPaymentSuccessful() {
		List<OrderResponseDTO> orders = orderService.getAllOrdersWithCODAndPaymentSuccessful();
		return ResponseEntity.ok(orders);
	}

	@PostMapping("customer/place")
	@Transactional
	@Operation(summary = "Place an order", description = "Places an order for the given customer ID. The cart is cleared after the order is placed.")

	public OrderResponseDTO placeOrder(@Valid @RequestBody GenericPaymentRequestDTO genericPaymentRequestDTO) {
		log.info("Placing order for customer ID: {}", genericPaymentRequestDTO.getUserId());
		OrderResponseDTO response = orderService.placeOrder(genericPaymentRequestDTO);
		log.info("Order placed successfully for customer ID: {}", genericPaymentRequestDTO.getUserId());
		return response;
	}

	@GetMapping("customer/status")
	@Operation(summary = "Get order status", description = "Fetches the status of an order for the given order ID.")

	public OrderResponseDTO getOrderStatus(@RequestParam Long customerId) {
		log.info("Fetching order status for order ID: {}", customerId);
		OrderResponseDTO response = orderService.getOrderStatus(customerId);
		log.info("Order status fetched successfully for order ID: {}", customerId);
		return response;
	}

	@PutMapping("communication/update/{orderId}/{nextStatus}")
	public String setOrderStatus(@PathVariable Long orderId,@PathVariable Status nextStatus){
		return orderService.setOrderStatus(orderId,nextStatus);
	}
	
	@GetMapping("customer/getOrders/{customerId}")
	public ResponseEntity<Order> getOrderDetailsByCustomerId(@PathVariable Long customerId) {
		return ResponseEntity.ok(orderService.getOrderDetailsByCustomerId(customerId));
	}
	
	@GetMapping("restaurants/getAllOrders/{restaurantId}")
	public ResponseEntity<List<Order>> getOrdersByRestaurantId(@PathVariable Long restaurantId){
		return orderService.getOrdersByRestaurantId(restaurantId);
	}
	
	@GetMapping("deliveryAgent/{orderId}")
	public ResponseEntity<Order> getOrderById(@PathVariable Long orderId){
		return orderService.getOrderById(orderId);
	}
	
	@GetMapping("customer/getPastOrders/{customerId}")
	public ResponseEntity<List<Order>> getPastOrders(@PathVariable Long customerId){
		return orderService.getPastOrders(customerId);
	}
	
	@GetMapping("restaurants/getDeliveredOrders/{restaurantId}")
	public ResponseEntity<List<Order>> getOrdersByRestaurantIdAndStatus(@PathVariable Long restaurantId){
		return orderService.getOrdersByRestaurantIdAndStatus(restaurantId);
	}

}