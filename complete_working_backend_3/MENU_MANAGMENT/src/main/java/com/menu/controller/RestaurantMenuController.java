package com.menu.controller;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.menu.dto.AddMenuItemRequest;
import com.menu.dto.MenuItemDTO;
import com.menu.dto.RestaurantDTO;
import com.menu.dto.UpdateAvailabilityRequest;
import com.menu.dto.UpdateMenuItemRequest;
import com.menu.service.RestaurantMenuService;

//Swagger/OpenAPI Imports
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/menu") 
@RequiredArgsConstructor
@CrossOrigin("*")
@Tag(name = "Restaurant Menu Management", description = "APIs for restaurant owners to manage their menu items (add, update, delete, view, change availability).")
public class RestaurantMenuController {

	private final RestaurantMenuService restaurantMenuService;
	
	
    // POST /api/v1/menu/add
    // The Restaurant Owner can add menu items
	@Operation(
	        summary = "Add a new menu item",
	        description = "Allows a restaurant owner to add a new menu item to their restaurant's menu.",
	        responses = {
	            @ApiResponse(
	                responseCode = "201",
	                description = "Menu item created successfully",
	                content = @Content(mediaType = "application/json", schema = @Schema(implementation = MenuItemDTO.class))
	            ),
	            @ApiResponse(
	                responseCode = "400",
	                description = "Invalid input (e.g., validation errors, missing required fields)",
	                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
	            ),
	            @ApiResponse(
	                responseCode = "404",
	                description = "Restaurant or Category not found for the provided IDs",
	                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
	            ),
	            @ApiResponse(
	                responseCode = "500",
	                description = "Internal server error",
	                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
	            )
	        }
	    )
    @PostMapping(value="/restaurants/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MenuItemDTO> addMenuItem(
    		@Parameter(description = "JSON object with details of the menu item (name, description, price, etc.). Must be sent as a part named 'menuItemData'.", required = true)
            @RequestPart AddMenuItemRequest request,
            
            @Parameter(description = "Optional image file for the menu item. Must be sent as a part named 'itemImage'.", required = false)
            @RequestPart(value = "itemImage", required = false) MultipartFile itemImageFile) throws IOException{
		
		
        MenuItemDTO newMenuItem = restaurantMenuService.addMenuItem(request, itemImageFile);
        return new ResponseEntity<>(newMenuItem, HttpStatus.CREATED); // 201 Created
    }

    // PUT /api/v1/menu/update/{menu_item_id}
	 @Operation(
		        summary = "Update an existing menu item",
		        description = "Allows a restaurant owner to update the details of an existing menu item.",
		        responses = {
		            @ApiResponse(
		                responseCode = "200",
		                description = "Menu item updated successfully",
		                content = @Content(mediaType = "application/json", schema = @Schema(implementation = MenuItemDTO.class))
		            ),
		            @ApiResponse(
		                responseCode = "400",
		                description = "Invalid input (e.g., validation errors)",
		                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
		            ),
		            @ApiResponse(
		                responseCode = "404",
		                description = "Menu item or Category not found for the provided IDs",
		                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
		            ),
		            @ApiResponse(
		                responseCode = "500",
		                description = "Internal server error",
		                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
		            )
		        }
		    )
    @PutMapping(value="/restaurants/update/{menu_item_id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MenuItemDTO> updateMenuItem(
    		@Parameter(description = "ID of the menu item to update", required = true, example = "1")
            @PathVariable("menu_item_id") Long itemId,
             
            @Parameter(description = "JSON object with updated details of the menu item. Must be sent as a part named 'menuItemData'.", required = true)
            @RequestPart @Valid UpdateMenuItemRequest request,
            
            @Parameter(description = "Optional image file for the menu item. If provided, it replaces the existing image. An empty file clears the image. Must be sent as a part named 'itemImage'.", required = false)
            @RequestPart(value = "itemImage", required = false) MultipartFile itemImageFile) throws IOException {
	 
        MenuItemDTO updatedMenuItem = restaurantMenuService.updateMenuItem(itemId, request, itemImageFile);
        return new ResponseEntity<>(updatedMenuItem, HttpStatus.OK); // 200 OK
    }

    // DELETE /api/v1/menu/delete/{menu_item_id}
	 @Operation(
		        summary = "Delete a menu item",
		        description = "Allows a restaurant owner to delete a menu item from their menu.",
		        responses = {
		            @ApiResponse(
		                responseCode = "204",
		                description = "Menu item deleted successfully (No Content)"
		            ),
		            @ApiResponse(
		                responseCode = "404",
		                description = "Menu item not found for the given ID",
		                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
		            ),
		            @ApiResponse(
		                responseCode = "500",
		                description = "Internal server error",
		                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
		            )
		        }
		    )
    @DeleteMapping("/restaurants/delete/{menu_item_id}")
    public ResponseEntity<Void> deleteMenuItem(
    		@Parameter(description = "ID of the menu item to delete", required = true, example = "101")
    		@PathVariable("menu_item_id") Long itemId) {
        restaurantMenuService.deleteMenuItem(itemId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content
    }

    // PUT /api/v1/menu/availability/{menu_item_id}
    		@Operation(
    		        summary = "Update menu item availability",
    		        description = "Allows a restaurant owner to change the availability status (e.g., in stock/out of stock) of a menu item.",
    		        responses = {
    		            @ApiResponse(
    		                responseCode = "200",
    		                description = "Menu item availability updated successfully",
    		                content = @Content(mediaType = "application/json", schema = @Schema(implementation = MenuItemDTO.class))
    		            ),
    		            @ApiResponse(
    		                responseCode = "400",
    		                description = "Invalid input (e.g., validation errors)",
    		                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
    		            ),
    		            @ApiResponse(
    		                responseCode = "404",
    		                description = "Menu item not found for the given ID",
    		                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
    		            ),
    		            @ApiResponse(
    		                responseCode = "500",
    		                description = "Internal server error",
    		                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
    		            )
    		        }
    		    )
    @PutMapping("/restaurants/availability/{menu_item_id}")
    public ResponseEntity<MenuItemDTO> updateMenuItemAvailability(
    		@Parameter(description = "ID of the menu item to update availability for", required = true, example = "101")
            @PathVariable("menu_item_id") Long itemId,
            @Parameter(description = "New availability status (true for available, false for unavailable)", required = true)
            @Valid @RequestBody UpdateAvailabilityRequest request) {
        MenuItemDTO updatedMenuItem = restaurantMenuService.updateMenuItemAvailability(itemId, request);
        return new ResponseEntity<>(updatedMenuItem, HttpStatus.OK); 
    }

    // GET /api/v1/menu/{menu_item_id} for owner to view a single item
    		@Operation(
    		        summary = "Get a single menu item by ID for owner view",
    		        description = "Retrieves details of a specific menu item, typically for a restaurant owner's view.",
    		        responses = {
    		            @ApiResponse(
    		                responseCode = "200",
    		                description = "Menu item found and returned",
    		                content = @Content(mediaType = "application/json", schema = @Schema(implementation = MenuItemDTO.class))
    		            ),
    		            @ApiResponse(
    		                responseCode = "404",
    		                description = "Menu item not found for the given ID",
    		                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
    		            ),
    		            @ApiResponse(
    		                responseCode = "500",
    		                description = "Internal server error",
    		                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
    		            )
    		        }
    		    )
    @GetMapping("/item/{menu_item_id}")
    public ResponseEntity<MenuItemDTO> getMenuItemById(
    		@Parameter(description = "ID of the menu item to retrieve", required = true, example = "101")
    		@PathVariable("menu_item_id") Long itemId) {
        MenuItemDTO menuItem = restaurantMenuService.getMenuItemById(itemId);
        return new ResponseEntity<>(menuItem, HttpStatus.OK);
    }
    		

	
}
