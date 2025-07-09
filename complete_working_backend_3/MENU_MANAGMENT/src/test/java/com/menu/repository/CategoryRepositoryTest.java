//package com.menu.repository;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.Mockito.when;
//
//import java.util.Optional;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import com.menu.entity.Category;
//
//public class CategoryRepositoryTest {
//
//    @Mock
//    private CategoryRepository categoryRepository;
//
//    private Category category;
//
//    @BeforeEach
//    public void setup() {
//        MockitoAnnotations.openMocks(this);
//
//        category = new Category();
//        category.setId(1L);
//        category.setName("Test Category");
//    }
//
//    @Test
//    public void testFindByName() {
//        // Mock the behavior of the repository
//        when(categoryRepository.findByName("Test Category")).thenReturn(Optional.of(category));
//
//        // Call the method
//        Optional<Category> foundCategory = categoryRepository.findByName("Test Category");
//
//        // Assert the result
//        assertThat(foundCategory).isPresent();
//        assertThat(foundCategory.get().getName()).isEqualTo("Test Category");
//    }
//
//    @Test
//    public void testExistsByName() {
//        // Mock the behavior of the repository
//        when(categoryRepository.existsByName("Test Category")).thenReturn(true);
//
//        // Call the method
//        boolean exists = categoryRepository.existsByName("Test Category");
//
//        // Assert the result
//        assertThat(exists).isTrue();
//    }
//
//    @Test
//    public void testFindByNameNotFound() {
//        // Mock the behavior of the repository
//        when(categoryRepository.findByName("Nonexistent Category")).thenReturn(Optional.empty());
//
//        // Call the method
//        Optional<Category> foundCategory = categoryRepository.findByName("Nonexistent Category");
//
//        // Assert the result
//        assertThat(foundCategory).isNotPresent();
//    }
//
//    @Test
//    public void testExistsByNameNotFound() {
//        // Mock the behavior of the repository
//        when(categoryRepository.existsByName("Nonexistent Category")).thenReturn(false);
//
//        // Call the method
//        boolean exists = categoryRepository.existsByName("Nonexistent Category");
//
//        // Assert the result
//        assertThat(exists).isFalse();
//    }
//}