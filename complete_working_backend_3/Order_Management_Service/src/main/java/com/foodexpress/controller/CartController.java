package com.foodexpress.controller;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.foodexpress.dto.CartDTO;
import com.foodexpress.dto.CartResponseDTO;
import com.foodexpress.service.CartService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/cart")
@CrossOrigin("*")
@RequiredArgsConstructor
@Slf4j
public class CartController {

    private final CartService cartService;

    @PostMapping("customer/add")
    @Operation(
        summary = "Add item to cart",
        description = "Adds an item to the cart for the given customer ID. If the cart contains items from a different restaurant, the cart will be cleared before adding the new item."
    )

    public String addItemToCart(@Valid @RequestBody CartDTO cartDTO) {
    	log.info("Adding item to cart for customer ID: {}", cartDTO.getCustomerId());
        cartService.addItemToCart(cartDTO);
        log.info("Item added successfully to cart for customer ID: {}", cartDTO.getCustomerId());
        return "Item Added";
    }

    @DeleteMapping("customer/remove")
    @Transactional
    @Operation(
        summary = "Remove item from cart",
        description = "Removes an item from the cart for the given customer ID and menu item ID. Throws an error if the item is not found in the cart."
    )

    public CartResponseDTO removeItemFromCart(@RequestParam Long customerId, @RequestParam Long menuItemId) {
    	log.info("Removing item with menuItemId: {} from cart for customer ID: {}", menuItemId, customerId);
        CartResponseDTO response = cartService.removeItemFromCart(customerId, menuItemId);
        log.info("Item removed successfully from cart for customer ID: {}", customerId);
        return response;
    }

    @GetMapping("customer/{customerId}")
    @Operation(
        summary = "Get cart details",
        description = "Fetches the cart details for the given customer ID, including all items, total items, and total amount."
    )

    public CartResponseDTO getCartByCustomerId(@PathVariable Long customerId) {
    	log.info("Fetching cart details for customer ID: {}", customerId);
        CartResponseDTO response = cartService.getCartByCustomerId(customerId);
        log.info("Cart details fetched successfully for customer ID: {}", customerId);
        return response;
    }
}