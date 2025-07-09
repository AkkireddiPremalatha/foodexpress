package com.foodexpress.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.foodexpress.dto.CartDTO;
import com.foodexpress.dto.CartResponseDTO;
import com.foodexpress.enums.Status;
import com.foodexpress.model.Cart;
import com.foodexpress.model.Order;
import com.foodexpress.repository.CartRepository;
import com.foodexpress.repository.OrderRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
	private final CartRepository cartRepository;
	private final OrderRepository orderRepository;

	@Override
	public CartResponseDTO addItemToCart(CartDTO cartDTO) {
		log.info("Adding item to cart for customer ID: {}", cartDTO.getCustomerId());
		List<Cart> existingCartItems = cartRepository.findByCustomerId(cartDTO.getCustomerId());
		if (!existingCartItems.isEmpty()) {
			Integer existingRestaurantId = existingCartItems.get(0).getRestaurantId();
			if (!existingRestaurantId.equals(cartDTO.getRestaurantId())) {
				cartRepository.deleteByCustomerId(cartDTO.getCustomerId());
				log.info("Cart cleared due to switching restaurants");
			}
		}
		// ✅ Allow add if no recent order found
		Optional<Order> recentOrder = orderRepository.findTopByCustomerIdOrderByCreatedAtDesc(cartDTO.getCustomerId());
		if (recentOrder.isPresent()) {
			Order order = recentOrder.get();
			log.info("Recent order found (ID: {}, Status: {})", order.getOrderId(), order.getStatus());
			if (order.getStatus() != Status.DELIVERED) {
				log.warn("Order not delivered. Cannot add to cart.");
				throw new IllegalStateException("Cannot add item to cart. Last order is still in progress.");
			}
		} else {
			log.info("No recent order found — allowing cart add");
		}
		// ✅ Check if item already exists → increment quantity
		List<Cart> matches = cartRepository.findByCustomerIdAndMenuItemId(cartDTO.getCustomerId(),
				cartDTO.getMenuItemId());
		if (!matches.isEmpty()) {
			Cart cart = matches.get(0);
			cart.setQuantity(cart.getQuantity() + 1);
			cartRepository.save(cart);
			log.info("Incremented quantity for item ID {} to {}", cart.getMenuItemId(), cart.getQuantity());
		} else {
			Cart cart = new Cart();
			cart.setCustomerId(cartDTO.getCustomerId());
			cart.setMenuItemId(cartDTO.getMenuItemId());
			cart.setRestaurantId(cartDTO.getRestaurantId());
			cart.setItemName(cartDTO.getItemName());
			cart.setQuantity(1);
			cart.setPrice(cartDTO.getPrice());
			cartRepository.save(cart);
			log.info("New item added to cart: {}", cartDTO.getMenuItemId());
		}
		return getCartByCustomerId(cartDTO.getCustomerId());
	}

	@Override
	public CartResponseDTO removeItemFromCart(Long customerId, Long menuItemId) {
		log.info("Removing item from cart for customer ID: {} and menuItem ID: {}", customerId, menuItemId);
		List<Cart> cartItem = cartRepository.findByCustomerIdAndMenuItemId(customerId, menuItemId);
		if (cartItem.isEmpty()) {
			throw new RuntimeException("Item not found in cart.");
		}
		Cart item = cartItem.get(0);
		if (item.getQuantity() > 1) {
			item.setQuantity(item.getQuantity() - 1);
			cartRepository.save(item);
			log.info("Decremented quantity for item {}", menuItemId);
		} else {
			cartRepository.deleteById(item.getId());
			log.info("Removed item {} completely from cart", menuItemId);
		}
		return getCartByCustomerId(customerId);
	}

	@Override
	public CartResponseDTO getCartByCustomerId(Long customerId) {
		log.info("Fetching cart for customer ID: {}", customerId);
		List<Cart> cartItems = cartRepository.findByCustomerId(customerId);
		double totalAmount = cartItems.stream().mapToDouble(item -> item.getQuantity() * item.getPrice()).sum();
		CartResponseDTO response = new CartResponseDTO();
		response.setCustomerId(customerId);
		response.setItems(cartItems);
		response.setTotalItems(cartItems.size());
		response.setTotalAmount(totalAmount);
		log.info("Cart contains {} item(s) worth ₹{}", cartItems.size(), totalAmount);
		return response;
	}
}