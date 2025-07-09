//package com.menu.service;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import com.menu.dto.CategoryDTO;
//import com.menu.entity.Category;
//import com.menu.exception.ResourceNotFoundException;
//import com.menu.repository.CategoryRepository;
//
//public class CategoryServiceImplTest {
//
//    @Mock
//    private CategoryRepository categoryRepository;
//
//    @InjectMocks
//    private CategoryServiceImpl categoryService;
//
//    private Category category;
//    private CategoryDTO categoryDTO;
//
//    @BeforeEach
//    public void setup() {
//        MockitoAnnotations.openMocks(this);
//
//        category = new Category();
//        category.setId(1L);
//        category.setName("Test Category");
//        category.setDescription("Test Description");
//
//        categoryDTO = new CategoryDTO();
//        categoryDTO.setId(1L);
//        categoryDTO.setName("Test Category");
//        categoryDTO.setDescription("Test Description");
//    }
//
//    @Test
//    public void testCreateCategory() {
//        // Mock behavior
//        when(categoryRepository.existsByName(categoryDTO.getName())).thenReturn(false);
//        when(categoryRepository.save(any(Category.class))).thenReturn(category);
//
//        // Call the method
//        CategoryDTO createdCategory = categoryService.createCategory(categoryDTO);
//
//        // Assert the result
//        assertThat(createdCategory).isNotNull();
//        assertThat(createdCategory.getName()).isEqualTo("Test Category");
//
//        // Verify interactions
//        verify(categoryRepository, times(1)).existsByName(categoryDTO.getName());
//        verify(categoryRepository, times(1)).save(any(Category.class));
//    }
//
//    @Test
//    public void testCreateCategoryThrowsExceptionWhenNameExists() {
//        // Mock behavior
//        when(categoryRepository.existsByName(categoryDTO.getName())).thenReturn(true);
//
//        // Call the method and assert exception
//        assertThrows(IllegalArgumentException.class, () -> categoryService.createCategory(categoryDTO));
//
//        // Verify interactions
//        verify(categoryRepository, times(1)).existsByName(categoryDTO.getName());
//        verify(categoryRepository, never()).save(any(Category.class));
//    }
//
//    @Test
//    public void testGetCategoryById() {
//        // Mock behavior
//        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
//
//        // Call the method
//        CategoryDTO foundCategory = categoryService.getCategoryById(1L);
//
//        // Assert the result
//        assertThat(foundCategory).isNotNull();
//        assertThat(foundCategory.getName()).isEqualTo("Test Category");
//
//        // Verify interactions
//        verify(categoryRepository, times(1)).findById(1L);
//    }
//
//    @Test
//    public void testGetCategoryByIdThrowsExceptionWhenNotFound() {
//        // Mock behavior
//        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());
//
//        // Call the method and assert exception
//        assertThrows(ResourceNotFoundException.class, () -> categoryService.getCategoryById(1L));
//
//        // Verify interactions
//        verify(categoryRepository, times(1)).findById(1L);
//    }
//
//    @Test
//    public void testGetAllCategories() {
//        // Mock behavior
//        when(categoryRepository.findAll()).thenReturn(Arrays.asList(category));
//
//        // Call the method
//        List<CategoryDTO> categories = categoryService.getAllCategories();
//
//        // Assert the result
//        assertThat(categories).hasSize(1);
//        assertThat(categories.get(0).getName()).isEqualTo("Test Category");
//
//        // Verify interactions
//        verify(categoryRepository, times(1)).findAll();
//    }
//}