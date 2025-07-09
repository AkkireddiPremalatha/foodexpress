//package com.menu.repository;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.Mockito.when;
//
//import java.math.BigDecimal;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import com.menu.entity.Category;
//import com.menu.entity.MenuItem;
//import com.menu.entity.Restaurant;
//
//public class MenuItemRepositoryTest {
//
//    @Mock
//    private MenuItemRepository menuItemRepository;
//
//    private MenuItem menuItem1;
//    private MenuItem menuItem2;
//    private Restaurant restaurant;
//    private Category category;
//
//    @BeforeEach
//    public void setup() {
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
//        // Mock MenuItem 1
//        menuItem1 = new MenuItem();
//        menuItem1.setMenuItemId(1L);
//        menuItem1.setItemName("Pizza");
//        menuItem1.setItemDescription("Delicious cheese pizza");
//        menuItem1.setPrice(new Double("10.99"));
//        menuItem1.setAvailable(true);
//        menuItem1.setRestaurant(restaurant); // Set the Restaurant object
//        menuItem1.setCategory(category); // Set the Category object
//
//        // Mock MenuItem 2
//        menuItem2 = new MenuItem();
//        menuItem2.setMenuItemId(2L);
//        menuItem2.setItemName("Burger");
//        menuItem2.setItemDescription("Juicy beef burger");
//        menuItem2.setPrice(new Double("8.99"));
//        menuItem2.setAvailable(true);
//        menuItem2.setRestaurant(restaurant); // Set the Restaurant object
//        menuItem2.setCategory(category); // Set the Category object
//    }
//
//    @Test
//    public void testFindByRestaurantId() {
//        // Mock the behavior of the repository
//        when(menuItemRepository.findByRestaurant_Id(1L)).thenReturn(Arrays.asList(menuItem1, menuItem2));
//
//        // Call the method
//        List<MenuItem> menuItems = menuItemRepository.findByRestaurant_Id(1L);
//
//        // Assert the result
//        assertThat(menuItems).hasSize(2);
//        assertThat(menuItems).contains(menuItem1, menuItem2);
//    }
//
//    @Test
//    public void testFindByRestaurantIdInAndItemNameContainingIgnoreCase() {
//        // Mock the behavior of the repository
//        when(menuItemRepository.findByRestaurantIdInAndItemNameContainingIgnoreCase(Arrays.asList(1L, 2L), "Pizza"))
//                .thenReturn(Arrays.asList(menuItem1));
//
//        // Call the method
//        List<MenuItem> menuItems = menuItemRepository.findByRestaurantIdInAndItemNameContainingIgnoreCase(
//                Arrays.asList(1L, 2L), "Pizza");
//
//        // Assert the result
//        assertThat(menuItems).hasSize(1);
//        assertThat(menuItems.get(0).getItemName()).isEqualTo("Pizza");
//    }
//
//    @Test
//    public void testFindByRestaurantIdAndCategoryName() {
//        // Mock the behavior of the repository
//        when(menuItemRepository.findByRestaurant_IdAndCategory_Name(1L, "Fast Food"))
//                .thenReturn(Arrays.asList(menuItem2));
//
//        // Call the method
//        List<MenuItem> menuItems = menuItemRepository.findByRestaurant_IdAndCategory_Name(1L, "Fast Food");
//
//        // Assert the result
//        assertThat(menuItems).hasSize(1);
//        assertThat(menuItems.get(0).getItemName()).isEqualTo("Burger");
//    }
//
//    @Test
//    public void testFindById() {
//        // Mock the behavior of the repository
//        when(menuItemRepository.findById(1L)).thenReturn(Optional.of(menuItem1));
//
//        // Call the method
//        Optional<MenuItem> foundMenuItem = menuItemRepository.findById(1L);
//
//        // Assert the result
//        assertThat(foundMenuItem).isPresent();
//        assertThat(foundMenuItem.get().getItemName()).isEqualTo("Pizza");
//    }
//
//    @Test
//    public void testFindByRestaurantIdAndItemNameContainingIgnoreCase() {
//        // Mock the behavior of the repository
//        when(menuItemRepository.findByRestaurant_IdAndItemNameContainingIgnoreCase(1L, "Bur"))
//                .thenReturn(Arrays.asList(menuItem2));
//
//        // Call the method
//        List<MenuItem> menuItems = menuItemRepository.findByRestaurant_IdAndItemNameContainingIgnoreCase(1L, "Bur");
//
//        // Assert the result
//        assertThat(menuItems).hasSize(1);
//        assertThat(menuItems.get(0).getItemName()).isEqualTo("Burger");
//    }
//
//    @Test
//    public void testFindByRestaurantIdAndCategoryId() {
//        // Mock the behavior of the repository
//        when(menuItemRepository.findByRestaurant_IdAndCategory_Id(1L, 1L)).thenReturn(Arrays.asList(menuItem1));
//
//        // Call the method
//        List<MenuItem> menuItems = menuItemRepository.findByRestaurant_IdAndCategory_Id(1L, 1L);
//
//        // Assert the result
//        assertThat(menuItems).hasSize(1);
//        assertThat(menuItems.get(0).getItemName()).isEqualTo("Pizza");
//    }
//}