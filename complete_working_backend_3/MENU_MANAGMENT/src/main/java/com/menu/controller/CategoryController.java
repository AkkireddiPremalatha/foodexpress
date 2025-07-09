//package com.menu.controller;
//
//import java.util.List;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.ErrorResponse;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.menu.dto.CategoryDTO;
//import com.menu.service.CategoryService;
//
////Swagger/OpenAPI Imports
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.Parameter;
//import io.swagger.v3.oas.annotations.media.Content;
//import io.swagger.v3.oas.annotations.media.Schema;
//import io.swagger.v3.oas.annotations.responses.ApiResponse;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//
//
//@RestController
//@RequestMapping("/api/v1")
//@RequiredArgsConstructor
//@Tag(name = "Category Management", description = "APIs for managing food categories")
//public class CategoryController {
//
//	private final CategoryService categoryService;
//
//
//    
//	@Operation(
//	        summary = "Create a new food category",
//	        description = "Adds a new food category to the system. Category names must be unique.",
//	        responses = {
//	            @ApiResponse(
//	                responseCode = "201",
//	                description = "Category created successfully",
//	                content = @Content(mediaType = "application/json", schema = @Schema(implementation = CategoryDTO.class))
//	            ),
//	            @ApiResponse(
//	                responseCode = "400",
//	                description = "Invalid input (e.g., missing name, category with same name already exists, validation errors)",
//	                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
//	            ),
//	            @ApiResponse(
//	                responseCode = "500",
//	                description = "Internal server error",
//	                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
//	            )
//	        }
//	    )
////    @PostMapping("restaurants/categories")
////    public ResponseEntity<CategoryDTO> createCategory(
////            @Parameter(description = "Category details to be created", required = true)
////            @Valid @RequestBody CategoryDTO categoryDTO) {
////        CategoryDTO createdCategory = categoryService.createCategory(categoryDTO);
////        return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
////    }
//
//    // GET /api/v1/categories/{id}
//	@Operation(
//	        summary = "Get a category by ID",
//	        description = "Retrieves a specific food category using its unique ID.",
//	        responses = {
//	            @ApiResponse(
//	                responseCode = "200",
//	                description = "Category found and returned",
//	                content = @Content(mediaType = "application/json", schema = @Schema(implementation = CategoryDTO.class))
//	            ),
//	            @ApiResponse(
//	                responseCode = "404",
//	                description = "Category not found for the given ID",
//	                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
//	            ),
//	            @ApiResponse(
//	                responseCode = "500",
//	                description = "Internal server error",
//	                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
//	            )
//	        }
//	    )
////    @GetMapping("/categories/{id}")
////    public ResponseEntity<CategoryDTO> getCategoryById(
////            @Parameter(description = "ID of the category to retrieve", required = true)
////            @PathVariable Long id) {
////        CategoryDTO category = categoryService.getCategoryById(id);
////        return new ResponseEntity<>(category, HttpStatus.OK);
////    }
//
//    // GET /api/v1/categories
//	@Operation(
//	        summary = "Get all food categories",
//	        description = "Retrieves a list of all available food categories.",
//	        responses = {
//	            @ApiResponse(
//	                responseCode = "200",
//	                description = "List of categories returned successfully",
//	                content = @Content(mediaType = "application/json", schema = @Schema(implementation = CategoryDTO.class))
//	            ),
//	            @ApiResponse(
//	                responseCode = "500",
//	                description = "Internal server error",
//	                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
//	            )
//	        }
//	    )
//    @GetMapping("/categories")
//    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
//        List<CategoryDTO> categories = categoryService.getAllCategories();
//        return new ResponseEntity<>(categories, HttpStatus.OK);
//    }
//    
//    
//}
