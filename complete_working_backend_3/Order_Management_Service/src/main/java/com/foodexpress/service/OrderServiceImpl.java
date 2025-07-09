package com.foodexpress.service;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.foodexpress.dto.AssignDeliveryRequestDTO;
import com.foodexpress.dto.CustomerResponseDto;
import com.foodexpress.dto.GenericPaymentRequestDTO;
import com.foodexpress.dto.OrderResponseDTO;
import com.foodexpress.enums.Status;
import com.foodexpress.feign.CustomerServiceFeignClient;
import com.foodexpress.feign.DeliveryServiceFeignClient;
import com.foodexpress.feign.MenuServiceFeignClient;
import com.foodexpress.feign.PaymentServiceFeignClient;
import com.foodexpress.model.Cart;
import com.foodexpress.model.Order;
import com.foodexpress.repository.CartRepository;
import com.foodexpress.repository.OrderRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

	private final OrderRepository orderRepository;
	private final CustomerServiceFeignClient customerFeignClient;
	private final MenuServiceFeignClient menuServiceFeignClient;
	private final DeliveryServiceFeignClient deliveryServiceFeignClient;
	private final PaymentServiceFeignClient paymentServiceFeignClient;
	private final CartRepository cartRepository;

	@Override
	public OrderResponseDTO placeOrder(GenericPaymentRequestDTO genericPaymentRequestDTO) {

		log.info("Placing order for customer ID: {}", genericPaymentRequestDTO.getUserId());
		
		Long customerId = genericPaymentRequestDTO.getUserId();

		List<Cart> cartItems = cartRepository.findByCustomerId(customerId);
		if (cartItems.isEmpty()) {
			log.error("Cart is empty for customer ID: {}", customerId);
			throw new IllegalArgumentException("Cart is empty. Cannot place order.");
		}

		// Check if an order already exists for the customer
		Optional<Order> existingOrder = orderRepository.findTopByCustomerIdOrderByCreatedAtDesc(customerId);
		if (existingOrder.isPresent()) {
			Order lastOrder = existingOrder.get();
			if (lastOrder.getStatus() == Status.DELIVERED) {
				log.info("Previous order for customer ID: {} is delivered. Proceeding with new order.", customerId);
			} else {
				log.error("An order already exists for customer ID: {} with status: {}", customerId,
						lastOrder.getStatus());
				throw new IllegalStateException(
						"An order already exists for this customer and is not yet delivered. Cannot place a new order.");
			}
		}
		double totalAmount = cartItems.stream().mapToDouble(item -> item.getQuantity() * item.getPrice()).sum();
		StringBuilder itemNames = new StringBuilder();

		for (int i = 0; i < cartItems.size(); i++) {
		    Cart cart = cartItems.get(i);
		    itemNames.append(cart.getItemName());
		    if (i < cartItems.size() - 1) {
		    	itemNames.append(", ");
		    }
		}
		int restaurantId = cartItems.get(0).getRestaurantId();
		CustomerResponseDto customer = customerFeignClient.getCustomerById(customerId);
		Order order = new Order();
		order.setCustomerId(genericPaymentRequestDTO.getUserId());
		order.setRestaurantId(restaurantId);
		order.setFirstName(customer.getFirstName());
		order.setTotalAmount(totalAmount);
		order.setStatus(Status.PLACED);
		order.setPaymentMethod(genericPaymentRequestDTO.getPaymentMethod());
		order.setItems(itemNames.toString());
		log.debug("Before save: {}", order.getStatus());
		Order savedOrder = orderRepository.save(order);
		genericPaymentRequestDTO.setOrderId(savedOrder.getOrderId());
		genericPaymentRequestDTO.setAmount(totalAmount);
		log.debug("After save: {}", savedOrder.getStatus());
		log.info("Order placed successfully with ID: {}", savedOrder.getOrderId());
		
		boolean resStatus = menuServiceFeignClient.getRestaurantStatus(restaurantId);
		boolean agentStatus = deliveryServiceFeignClient.getDeliveryAgentStatus(customer.getCustomerPincode());
		
		if(resStatus == true && agentStatus== true) {
			
			savedOrder.setStatus(Status.ACCEPTED);
			log.info("Processing payment confirmation for customer ID: {}", customerId);
			
			String paymentStatus = paymentServiceFeignClient.processPayment(genericPaymentRequestDTO).getBody();
			
			
			savedOrder.setPaymentStatus(paymentStatus);	
			savedOrder = orderRepository.save(savedOrder);
			AssignDeliveryRequestDTO assignDeliveryRequestDTO = new AssignDeliveryRequestDTO();
			int intValue = savedOrder.getOrderId().intValue();
			assignDeliveryRequestDTO.setOrderId(intValue);
			assignDeliveryRequestDTO.setCustomerPincode(customer.getCustomerPincode());
			
			
			deliveryServiceFeignClient.assignDelivery(assignDeliveryRequestDTO);
//			String deliveryStatus = deliveryServiceFeignClient.getDeliveryStatus(assignDeliveryRequestDTO).getBody();
			
		}
		
		
		
		
		
		
		

		// Clear the cart after placing the order
		cartRepository.deleteByCustomerId(genericPaymentRequestDTO.getUserId());
		log.warn("Cart cleared for customer ID: {}", customerId);
		CustomerResponseDto customer1 = customerFeignClient.getCustomerById(savedOrder.getCustomerId());
		OrderResponseDTO response = new OrderResponseDTO();
		response.setOrderId(savedOrder.getOrderId());
		response.setStatus(savedOrder.getStatus());
		response.setCustomerId(savedOrder.getCustomerId());
		response.setTotalAmount(savedOrder.getTotalAmount());
		response.setCustomer(customer1);
		return response;
	}

	@Override
	public OrderResponseDTO getOrderStatus(Long customerId) {
		log.debug("Fetching the most recent order status for customer ID: {}", customerId);

		// Fetch the most recent order for the given customer ID
		Order order = orderRepository.findTopByCustomerIdOrderByCreatedAtDesc(customerId).orElseThrow(() -> {
			log.error("No orders found for customer ID: {}", customerId);
			return new IllegalArgumentException("No orders found for customer ID: " + customerId);
		});

		log.info("Order status fetched successfully for customer ID: {} and order ID: {}", customerId,
				order.getOrderId());

		// Prepare the response
		OrderResponseDTO response = new OrderResponseDTO();
		response.setOrderId(order.getOrderId());
		response.setStatus(order.getStatus());
		response.setTotalAmount(order.getTotalAmount());

		return response;
	}

//	@Override
//	public OrderResponseDTO cancelOrder(Long customerId) {
//		log.info("Attempting to cancel the most recent order for customer ID: {}", customerId);
//
//		// Fetch the most recent order for the given customer ID
//		Order order = orderRepository.findTopByCustomerIdOrderByCreatedAtDesc(customerId).orElseThrow(() -> {
//			log.error("No orders found for customer ID: {}", customerId);
//			return new IllegalArgumentException("No orders found for customer ID: " + customerId);
//		});
//
//		Long orderId = order.getOrderId();
//		log.info("Most recent order ID for customer ID {} is {}", customerId, orderId);
//
//		// Check if the order is in a valid state to be canceled
//		if (order.getStatus() == Order.Status.PENDING || order.getStatus() == Order.Status.ACCEPTED) {
//			// Update the order status to CANCELED
////			order.setStatus(Order.Status.CANCELLED);
//			orderRepository.save(order);
//			log.info("Order canceled successfully for order ID: {}", orderId);
//
//		} else {
//			log.error("Order cannot be canceled at this stage for order ID: {}", orderId);
//			throw new IllegalStateException(
//					"Order cannot be canceled at this stage. Current status: " + order.getStatus());
//		}
//		return getOrderStatus(customerId);
//	}

//	// Accept State
//	@Override
//	public OrderResponseDTO acceptOrder(Long customerId) {
//		log.info("Attempting to accept the most recent order for customer ID: {}", customerId);
//
//		// Fetch the most recent order for the given customer ID
//		Order order = orderRepository.findTopByCustomerIdOrderByCreatedAtDesc(customerId).orElseThrow(() -> {
//			log.error("No orders found for customer ID: {}", customerId);
//			return new IllegalArgumentException("No orders found for customer ID: " + customerId);
//		});
//
//		Long orderId = order.getOrderId();
//		log.info("Most recent order ID for customer ID {} is {}", customerId, orderId);
//
//		// Check if the order is in a valid state to be accepted
//		if (order.getStatus() != Order.Status.PENDING) {
//			log.error("Order cannot be accepted at this stage for order ID: {}", orderId);
//			throw new IllegalStateException(
//					"Order cannot be accepted at this stage. Current status: " + order.getStatus());
//		}
//
//		// Communicate with the Restaurant Module to verify acceptance
////        boolean isAcceptedByRestaurant = restaurantService.acceptOrderByRestaurant(orderId);
////        if (!isAcceptedByRestaurant) {
////            log.error("Order with ID: {} was not accepted by the restaurant.", orderId);
////            throw new IllegalStateException("Order was not accepted by the restaurant.");
////        }
//
//		// Update the order status to ACCEPTED
//		order.setStatus(Order.Status.ACCEPTED);
//		orderRepository.save(order);
//		log.info("Order accepted successfully with ID: {}", orderId);
//		return getOrderStatus(customerId);
//	}

	/*
	 * @Override public OrderResponseDTO successPayment(Long customerId) {
	 * log.info("Processing payment confirmation for customer ID: {}", customerId);
	 * 
	 * // Fetch the most recent order for the given customer ID Order order =
	 * orderRepository.findTopByCustomerIdOrderByCreatedAtDesc(customerId).
	 * orElseThrow(() -> { log.error("No orders found for customer ID: {}",
	 * customerId); return new
	 * IllegalArgumentException("No orders found for customer ID: " + customerId);
	 * });
	 * 
	 * Long orderId = order.getOrderId();
	 * log.info("Most recent order ID for customer ID {} is {}", customerId,
	 * orderId);
	 * 
	 * // Check if the order is in a valid state to process payment if
	 * (order.getStatus() != Order.Status.ACCEPTED) { log.
	 * error("Order cannot be marked as SUCCESSPAYMENT at this stage for order ID: {}"
	 * , orderId); throw new IllegalStateException(
	 * "Order cannot be marked as SUCCESSPAYMENT at this stage. Current status: " +
	 * order.getStatus()); }
	 * 
	 * // Communicate with the Payment Module to verify payment success // boolean
	 * isPaymentSuccessful = paymentService.verifyPayment(orderId); // if
	 * (!isPaymentSuccessful) { //
	 * log.error("Payment for order ID: {} was not successful.", orderId); // throw
	 * new IllegalStateException("Payment was not successful for the order."); // }
	 * 
	 * // Update the order status to SUCCESSPAYMENT
	 * order.setStatus(Order.Status.SUCCESS); orderRepository.save(order);
	 * log.info("Order marked as SUCCESSPAYMENT successfully for order ID: {}",
	 * orderId);
	 * 
	 * return getOrderStatus(customerId);
	 * 
	 * }
	 */

	/*
	 * @Override public OrderResponseDTO deliverOrder(Long customerId) {
	 * log.info("Processing delivery confirmation for customer ID: {}", customerId);
	 * 
	 * // Fetch the most recent order for the given customer ID Order order =
	 * orderRepository.findTopByCustomerIdOrderByCreatedAtDesc(customerId).
	 * orElseThrow(() -> { log.error("No orders found for customer ID: {}",
	 * customerId); return new
	 * IllegalArgumentException("No orders found for customer ID: " + customerId);
	 * });
	 * 
	 * Long orderId = order.getOrderId();
	 * log.info("Most recent order ID for customer ID {} is {}", customerId,
	 * orderId);
	 * 
	 * // Check if the order is in a valid state to be marked as DELIVERED if
	 * (order.getStatus() != Order.Status.SUCCESS) { log.
	 * error("Order cannot be marked as DELIVERED at this stage for order ID: {}",
	 * orderId); throw new IllegalStateException(
	 * "Order cannot be marked as DELIVERED at this stage. Current status: " +
	 * order.getStatus()); }
	 * 
	 * // Update the order status to DELIVERED
	 * order.setStatus(Order.Status.DELIVERED); orderRepository.save(order);
	 * log.info("Order marked as DELIVERED successfully for order ID: {}", orderId);
	 * return getOrderStatus(customerId);
	 * 
	 * }
	 */

	@Override
	public List<OrderResponseDTO> getAllOrdersWithCODAndPaymentSuccessful() {
		List<Order> orderList = orderRepository.findOrdersWithSuccessfulStatusOrCOD();

		return orderList.stream().map(order -> {
			CustomerResponseDto customer = customerFeignClient.getCustomerById(order.getCustomerId());

			OrderResponseDTO dto = new OrderResponseDTO();
			dto.setOrderId(order.getOrderId());
			dto.setCustomerId(order.getCustomerId());
			dto.setStatus(order.getStatus());
			dto.setTotalAmount(order.getTotalAmount());
			dto.setCustomer(customer);

			return dto;
		}).toList();
	}
	
	@Override
	@Transactional
	public String setOrderStatus(Long orderId,Status status) {
		System.out.println(status);
		Order order=orderRepository.findById(orderId).orElse(null);
		if(order!=null) {
			order.setStatus(status);
			orderRepository.save(order);
		}
		else {
			throw new RuntimeException("Order with order id not found!");
		}
		return "Status updated successfully";
	}
	
	@Override
	public Order getOrderDetailsByCustomerId(Long customerId) {
		Order order = orderRepository.findOrderDetailsByCustomerId(customerId);
		return order;
	}
	
	@Override
	public ResponseEntity<List<Order>> getOrdersByRestaurantId(Long restaurantId){
		List<Order> orders = orderRepository.findByRestaurantIdAndStatus(restaurantId);
		return ResponseEntity.ok(orders);
	}
	
	@Override
	public ResponseEntity<Order> getOrderById(Long orderId){
		Order order = orderRepository.findById(orderId).orElse(null);
		return ResponseEntity.ok(order);
	}
	
	@Override
	public ResponseEntity<List<Order>> getPastOrders(Long customerId){
		List<Order> order = orderRepository.findByPastOrders(customerId);
		return ResponseEntity.ok(order);
		
	}
	
	@Override 
	public ResponseEntity<List<Order>> getOrdersByRestaurantIdAndStatus(Long restaurantId){
		List<Order> orders = orderRepository.findByRestaurantIdAndDelivered(restaurantId);
		return ResponseEntity.ok(orders);
	}



}