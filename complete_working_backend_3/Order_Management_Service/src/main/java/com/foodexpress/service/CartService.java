package com.foodexpress.service;

import com.foodexpress.dto.CartDTO;
import com.foodexpress.dto.CartResponseDTO;

public interface CartService {
    CartResponseDTO addItemToCart(CartDTO cartDTO);
    CartResponseDTO removeItemFromCart(Long customerId, Long menuItemId);
    CartResponseDTO getCartByCustomerId(Long customerId);
    //create a finalizedCart to store the cart details in the cart table 
}