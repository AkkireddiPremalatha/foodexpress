package com.foodexpress.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.foodexpress.dto.GenericPaymentRequestDTO;
import com.foodexpress.dto.OrderResponseDTO;
import com.foodexpress.enums.Status;
import com.foodexpress.model.Order;

public interface OrderService {
	OrderResponseDTO placeOrder(GenericPaymentRequestDTO genericPaymentRequestDTO);
    OrderResponseDTO getOrderStatus(Long orderId);
//    OrderResponseDTO cancelOrder(Long orderId);
//    OrderResponseDTO acceptOrder(Long customerId);
//    
//    OrderResponseDTO deliverOrder(Long customerId);
    List<OrderResponseDTO> getAllOrdersWithCODAndPaymentSuccessful();
    public String setOrderStatus(Long orderId,Status status);
	

	Order getOrderDetailsByCustomerId(Long customerId);
	
	public ResponseEntity<List<Order>> getOrdersByRestaurantId(Long restaurantId);
	
	public ResponseEntity<Order> getOrderById(Long orderId);
	
	public ResponseEntity<List<Order>> getPastOrders(Long customerId);
	
	public ResponseEntity<List<Order>> getOrdersByRestaurantIdAndStatus(Long restaurantId);
}