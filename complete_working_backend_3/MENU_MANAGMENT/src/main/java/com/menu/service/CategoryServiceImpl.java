//package com.menu.service;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//import org.springframework.stereotype.Service;
//
//import com.menu.dto.CategoryDTO;
//import com.menu.exception.ResourceNotFoundException;
//import com.menu.repository.CategoryRepository;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//
//@Service
//@Slf4j
//@RequiredArgsConstructor
//public class CategoryServiceImpl implements CategoryService {
//
//	private final CategoryRepository categoryRepository;
//
//    // method to map Entity to DTO
//    private CategoryDTO mapToCategoryDTO(Category category) {
//    	if (category == null) {
//            log.warn("Attempted to map a null Category entity to DTO.");
//            return null;
//        }
//    	log.debug("Mapping Category entity with ID {} to DTO.", category.getId());
//        return new CategoryDTO(
//            category.getId(),
//            category.getName(),
//            category.getDescription()
//            // did not include audit fields in DTO
//        );
//    }
//
//    // method to map DTO to Entity (for creation)
//    private Category mapToCategoryEntity(CategoryDTO categoryDTO) {
//    	if (categoryDTO == null) {
//            log.warn("Attempted to map a null CategoryDTO to entity.");
//            return null;
//        }
//    	log.debug("Mapping CategoryDTO with name '{}' to entity.", categoryDTO.getName());
//        Category category = new Category();
//        category.setName(categoryDTO.getName());
//        category.setDescription(categoryDTO.getDescription());
//        category.setCreatedBy("Admin"); 
//        return category;
//    }
//
//    @Override
//    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
//    	 log.info("Attempting to create a new category with name: '{}'.", categoryDTO.getName());
//        if (categoryRepository.existsByName(categoryDTO.getName())) {
//        	log.warn("Category creation failed: Category with name '{}' already exists.", categoryDTO.getName());
//            throw new IllegalArgumentException("Category with name '" + categoryDTO.getName() + "' already exists");
//        }
//        Category category = mapToCategoryEntity(categoryDTO);
//        Category savedCategory = categoryRepository.save(category);
//        log.info("Category '{}' (ID: {}) created successfully.", savedCategory.getName(), savedCategory.getId());
//        return mapToCategoryDTO(savedCategory);
//    }
//
//    @Override
//    public CategoryDTO getCategoryById(Long id) {
//    	log.info("Fetching category with ID: {}.", id);
//    	Category category = categoryRepository.findById(id)
//                .orElseThrow(() -> {
//                    log.error("Category not found for ID: {}.", id);
//                    return new ResourceNotFoundException("Category not found with ID: " + id);
//                });
//            log.info("Category with ID {} fetched successfully.", id);
//        return mapToCategoryDTO(category);
//    }
//
//    @Override
//    public List<CategoryDTO> getAllCategories() {
//    	log.info("Fetching all categories.");
//    	List<CategoryDTO> categories = categoryRepository.findAll().stream()
//                .map(this::mapToCategoryDTO)
//                .collect(Collectors.toList());
//            log.info("Fetched {} categories.", categories.size());
//            return categories;
//    }
//}
