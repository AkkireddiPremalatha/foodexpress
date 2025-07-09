package com.menu.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.menu.entity.Restaurant;

public class RestaurantRepositoryTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    private Restaurant restaurant1;
    private Restaurant restaurant2;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        // Mock Restaurant 1
        restaurant1 = new Restaurant();
        restaurant1.setId(1L);
        restaurant1.setRestaurantName("Test Restaurant 1");
        restaurant1.setRestaurantPin("123456");

        // Mock Restaurant 2
        restaurant2 = new Restaurant();
        restaurant2.setId(2L);
        restaurant2.setRestaurantName("Test Restaurant 2");
        restaurant2.setRestaurantPin("654321");
    }

    @Test
    public void testFindByRestaurantPin() {
        // Mock the behavior of the repository
        when(restaurantRepository.findByRestaurantPin("123456")).thenReturn(Arrays.asList(restaurant1));

        // Call the method
        List<Restaurant> restaurants = restaurantRepository.findByRestaurantPin("123456");

        // Assert the result
        assertThat(restaurants).hasSize(1);
        assertThat(restaurants.get(0).getRestaurantName()).isEqualTo("Test Restaurant 1");
    }

    @Test
    public void testFindByRestaurantName() {
        // Mock the behavior of the repository
        when(restaurantRepository.findByRestaurantName("Test Restaurant 1")).thenReturn(Optional.of(restaurant1));

        // Call the method
        Optional<Restaurant> foundRestaurant = restaurantRepository.findByRestaurantName("Test Restaurant 1");

        // Assert the result
        assertThat(foundRestaurant).isPresent();
        assertThat(foundRestaurant.get().getRestaurantName()).isEqualTo("Test Restaurant 1");
    }

    @Test
    public void testExistsByRestaurantName() {
        // Mock the behavior of the repository
        when(restaurantRepository.existsByRestaurantName("Test Restaurant 1")).thenReturn(true);

        // Call the method
        boolean exists = restaurantRepository.existsByRestaurantName("Test Restaurant 1");

        // Assert the result
        assertThat(exists).isTrue();
    }

    @Test
    public void testFindByRestaurantNameNotFound() {
        // Mock the behavior of the repository
        when(restaurantRepository.findByRestaurantName("Nonexistent Restaurant")).thenReturn(Optional.empty());

        // Call the method
        Optional<Restaurant> foundRestaurant = restaurantRepository.findByRestaurantName("Nonexistent Restaurant");

        // Assert the result
        assertThat(foundRestaurant).isNotPresent();
    }

    @Test
    public void testExistsByRestaurantNameNotFound() {
        // Mock the behavior of the repository
        when(restaurantRepository.existsByRestaurantName("Nonexistent Restaurant")).thenReturn(false);

        // Call the method
        boolean exists = restaurantRepository.existsByRestaurantName("Nonexistent Restaurant");

        // Assert the result
        assertThat(exists).isFalse();
    }
}