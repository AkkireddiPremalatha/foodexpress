//package com.menu.service;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//import java.math.BigDecimal;
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
//import com.menu.dto.MenuItemDTO;
//import com.menu.dto.RestaurantDTO;
//import com.menu.entity.Category;
//import com.menu.entity.MenuItem;
//import com.menu.entity.Restaurant;
//import com.menu.exception.ResourceNotFoundException;
//import com.menu.repository.MenuItemRepository;
//import com.menu.repository.RestaurantRepository;
//
//public class CustomerMenuServiceImplTest {
//
//    @Mock
//    private RestaurantRepository restaurantRepository;
//
//    @Mock
//    private MenuItemRepository menuItemRepository;
//
//    @InjectMocks
//    private CustomerMenuServiceImpl customerMenuService;
//
//    private Restaurant restaurant;
//    private MenuItem menuItem;
//
//    @BeforeEach
//public void setup() {
//    MockitoAnnotations.openMocks(this);
//
//    // Mock Restaurant
//    restaurant = new Restaurant();
//    restaurant.setId(1L);
//    restaurant.setRestaurantName("Test Restaurant");
//    restaurant.setRestaurantPin("123456");
//    restaurant.setRestaurantLocation("Test Location");
//    restaurant.setContactNumber("1234567890");
//
//    // Mock Category
//    Category category = new Category();
//    category.setId(1L);
//    category.setName("Fast Food");
//    category.setDescription("Category for fast food items");
//
//    // Mock MenuItem
//    menuItem = new MenuItem();
//    menuItem.setId(1L);
//    menuItem.setItemName("Pizza");
//    menuItem.setItemDescription("Delicious cheese pizza");
//    menuItem.setPrice(new BigDecimal("10.99"));
//    menuItem.setAvailable(true);
//    menuItem.setRestaurant(restaurant);
//    menuItem.setCategory(category); // Set the Category object
//}
//
//    @Test
//    public void testGetNearbyRestaurants() {
//        // Mock behavior
//        when(restaurantRepository.findByRestaurantPin("123456")).thenReturn(Arrays.asList(restaurant));
//
//        // Call the method
//        List<RestaurantDTO> restaurants = customerMenuService.getNearbyRestaurants("123456");
//
//        // Assert the result
//        assertThat(restaurants).hasSize(1);
//        assertThat(restaurants.get(0).getRestaurantName()).isEqualTo("Test Restaurant");
//
//        // Verify interactions
//        verify(restaurantRepository, times(1)).findByRestaurantPin("123456");
//    }
//
//    @Test
//    public void testGetNearbyRestaurantsThrowsExceptionWhenNotFound() {
//        // Mock behavior
//        when(restaurantRepository.findByRestaurantPin("123456")).thenReturn(List.of());
//
//        // Call the method and assert exception
//        assertThrows(ResourceNotFoundException.class, () -> customerMenuService.getNearbyRestaurants("123456"));
//
//        // Verify interactions
//        verify(restaurantRepository, times(1)).findByRestaurantPin("123456");
//    }
//
//    @Test
//    public void testGetRestaurantMenu() {
//        // Mock behavior
//        when(restaurantRepository.existsById(1L)).thenReturn(true);
//        when(menuItemRepository.findByRestaurant_Id(1L)).thenReturn(Arrays.asList(menuItem));
//
//        // Call the method
//        List<MenuItemDTO> menuItems = customerMenuService.getRestaurantMenu(1L);
//
//        // Assert the result
//        assertThat(menuItems).hasSize(1);
//        assertThat(menuItems.get(0).getItemName()).isEqualTo("Pizza");
//
//        // Verify interactions
//        verify(restaurantRepository, times(1)).existsById(1L);
//        verify(menuItemRepository, times(1)).findByRestaurant_Id(1L);
//    }
//
//    @Test
//    public void testSearchMenuItems() {
//        // Mock behavior
//        when(restaurantRepository.findByRestaurantPin("123456")).thenReturn(Arrays.asList(restaurant));
//        when(menuItemRepository.findByRestaurantIdInAndItemNameContainingIgnoreCase(any(), anyString()))
//                .thenReturn(Arrays.asList(menuItem));
//
//        // Call the method
//        List<MenuItemDTO> menuItems = customerMenuService.searchMenuItems("Pizza", "123456");
//
//        // Assert the result
//        assertThat(menuItems).hasSize(1);
//        assertThat(menuItems.get(0).getItemName()).isEqualTo("Pizza");
//
//        // Verify interactions
//        verify(restaurantRepository, times(1)).findByRestaurantPin("123456");
//        verify(menuItemRepository, times(1)).findByRestaurantIdInAndItemNameContainingIgnoreCase(any(), eq("Pizza"));
//    }
//
//    @Test
//    public void testGetMenuItemsByCategory() {
//        // Mock behavior
//        when(restaurantRepository.existsById(1L)).thenReturn(true);
//        when(menuItemRepository.findByRestaurant_IdAndCategory_Name(1L, "Fast Food"))
//                .thenReturn(Arrays.asList(menuItem));
//
//        // Call the method
//        List<MenuItemDTO> menuItems = customerMenuService.getMenuItemsByCategory(1L, "Fast Food");
//
//        // Assert the result
//        assertThat(menuItems).hasSize(1);
//        assertThat(menuItems.get(0).getItemName()).isEqualTo("Pizza");
//
//        // Verify interactions
//        verify(restaurantRepository, times(1)).existsById(1L);
//        verify(menuItemRepository, times(1)).findByRestaurant_IdAndCategory_Name(1L, "Fast Food");
//    }
//
//    @Test
//    public void testGetRestaurantById() {
//        // Mock behavior
//        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
//
//        // Call the method
//        RestaurantDTO foundRestaurant = customerMenuService.getRestaurantById(1L);
//
//        // Assert the result
//        assertThat(foundRestaurant).isNotNull();
//        assertThat(foundRestaurant.getRestaurantName()).isEqualTo("Test Restaurant");
//
//        // Verify interactions
//        verify(restaurantRepository, times(1)).findById(1L);
//    }
//}