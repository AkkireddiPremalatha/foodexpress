//package com.menu.service;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.Mockito.*;
//
//import java.io.IOException;
//import java.math.BigDecimal;
//import java.util.Optional;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.web.multipart.MultipartFile;
//
//import com.menu.dto.AddMenuItemRequest;
//import com.menu.dto.MenuItemDTO;
//import com.menu.dto.UpdateAvailabilityRequest;
//import com.menu.dto.UpdateMenuItemRequest;
//import com.menu.entity.Category;
//import com.menu.entity.MenuItem;
//import com.menu.entity.Restaurant;
//import com.menu.exception.ResourceNotFoundException;
//import com.menu.repository.CategoryRepository;
//import com.menu.repository.MenuItemRepository;
//import com.menu.repository.RestaurantRepository;
//
//public class RestaurantMenuServiceImplTest {
//
//    @Mock
//    private MenuItemRepository menuItemRepository;
//
//    @Mock
//    private RestaurantRepository restaurantRepository;
//
//    @Mock
//    private CategoryRepository categoryRepository;
//
//    @Mock
//    private MultipartFile mockMultipartFile;
//
//    @InjectMocks
//    private RestaurantMenuServiceImpl restaurantMenuService;
//
//    private Restaurant restaurant;
//    private Category category;
//    private MenuItem menuItem;
//
//    @BeforeEach
//    public void setup() throws IOException {
//        MockitoAnnotations.openMocks(this);
//
//        // Mock Restaurant
//        restaurant = new Restaurant();
//        restaurant.setId(1L);
//        restaurant.setRestaurantName("Test Restaurant");
//
//        // Mock Category
//        category = new Category();
//        category.setId(1L);
//        category.setName("Fast Food");
//
//        // Mock MenuItem
//        menuItem = new MenuItem();
//        menuItem.setMenuItemId(1L);
//        menuItem.setItemName("Pizza");
//        menuItem.setItemDescription("Delicious cheese pizza");
//        menuItem.setPrice(new Double("10.99"));
//        menuItem.setAvailable(true);
//        menuItem.setRestaurant(restaurant);
//        menuItem.setCategory(category);
//
//        // Mock MultipartFile
//        when(mockMultipartFile.getBytes()).thenReturn("test-image".getBytes());
//    }
//
//    @Test
//    public void testAddMenuItem() {
//        // Mock behavior
//        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
//        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
//        when(menuItemRepository.save(any(MenuItem.class))).thenReturn(menuItem);
//
//        // Create request
////        AddMenuItemRequest request = new AddMenuItemRequest(1L,1L,"Pizza","Delicious cheese pizza",new BigDecimal("10.99"),true);
//
//        // Call the method
////        MenuItemDTO result = restaurantMenuService.addMenuItem(request, mockMultipartFile);
//
//        // Assert the result
////        assertThat(result).isNotNull();
////        assertThat(result.getItemName()).isEqualTo("Pizza");
//
//        // Verify interactions
//        verify(restaurantRepository, times(1)).findById(1L);
//        verify(categoryRepository, times(1)).findById(1L);
//        verify(menuItemRepository, times(1)).save(any(MenuItem.class));
//    }
//
//    @Test
//    public void testUpdateMenuItem() {
//        // Mock behavior
//        when(menuItemRepository.findById(1L)).thenReturn(Optional.of(menuItem));
//        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
//        when(menuItemRepository.save(any(MenuItem.class))).thenReturn(menuItem);
//
//        // Create request
//        UpdateMenuItemRequest request = new UpdateMenuItemRequest("Updated Pizza", "Updated description", new BigDecimal("12.99"),true,1L);
//
//        // Call the method
//        MenuItemDTO result = restaurantMenuService.updateMenuItem(1L, request, mockMultipartFile);
//
//        // Assert the result
//        assertThat(result).isNotNull();
//        assertThat(result.getItemName()).isEqualTo("Updated Pizza");
//
//        // Verify interactions
//        verify(menuItemRepository, times(1)).findById(1L);
//        verify(categoryRepository, times(1)).findById(1L);
//        verify(menuItemRepository, times(1)).save(any(MenuItem.class));
//    }
//
//    @Test
//    public void testDeleteMenuItem() {
//        // Mock behavior
//        when(menuItemRepository.existsById(1L)).thenReturn(true);
//
//        // Call the method
//        restaurantMenuService.deleteMenuItem(1L);
//
//        // Verify interactions
//        verify(menuItemRepository, times(1)).existsById(1L);
//        verify(menuItemRepository, times(1)).deleteById(1L);
//    }
//
//    @Test
//    public void testUpdateMenuItemAvailability() {
//        // Mock behavior
//        when(menuItemRepository.findById(1L)).thenReturn(Optional.of(menuItem));
//        when(menuItemRepository.save(any(MenuItem.class))).thenReturn(menuItem);
//
//        // Create request
//        UpdateAvailabilityRequest request = new UpdateAvailabilityRequest(false);
//
//        // Call the method
//        MenuItemDTO result = restaurantMenuService.updateMenuItemAvailability(1L, request);
//
//        // Assert the result
//        assertThat(result).isNotNull();
//        assertThat(result.isAvailable()).isFalse();
//
//        // Verify interactions
//        verify(menuItemRepository, times(1)).findById(1L);
//        verify(menuItemRepository, times(1)).save(any(MenuItem.class));
//    }
//
//    @Test
//    public void testGetMenuItemById() {
//        // Mock behavior
//        when(menuItemRepository.findById(1L)).thenReturn(Optional.of(menuItem));
//
//        // Call the method
//        MenuItemDTO result = restaurantMenuService.getMenuItemById(1L);
//
//        // Assert the result
//        assertThat(result).isNotNull();
//        assertThat(result.getItemName()).isEqualTo("Pizza");
//
//        // Verify interactions
//        verify(menuItemRepository, times(1)).findById(1L);
//    }
//
//    @Test
//    public void testAddMenuItemThrowsExceptionWhenRestaurantNotFound() {
//        // Mock behavior
//        when(restaurantRepository.findById(1L)).thenReturn(Optional.empty());
//
//        // Create request
////        AddMenuItemRequest request = new AddMenuItemRequest(1L,1L,"Pizza","Delicious cheese pizza",new BigDecimal("10.99"),true);
//
//        // Call the method and assert exception
////        assertThrows(ResourceNotFoundException.class, () -> restaurantMenuService.addMenuItem(request, mockMultipartFile));
//
//        // Verify interactions
//        verify(restaurantRepository, times(1)).findById(1L);
//        verify(categoryRepository, never()).findById(anyLong());
//        verify(menuItemRepository, never()).save(any(MenuItem.class));
//    }
//}