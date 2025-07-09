//package com.menu.controller;
//
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//import java.math.BigDecimal;
//import java.util.Collections;
//import java.util.List;
//
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.menu.dto.MenuItemDTO;
//import com.menu.dto.RestaurantDTO;
//import com.menu.service.CustomerMenuService;
//
//@ExtendWith(MockitoExtension.class)
//public class CustomerMenuControllerTest {
//
//    @Mock
//    private CustomerMenuService customerMenuService;
//
//    @InjectMocks
//    private CustomerMenuController customerMenuController;
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    private RestaurantDTO restaurantDTO;
//    private MenuItemDTO menuItemDTO;
//    private List<RestaurantDTO> restaurantList;
//    private List<MenuItemDTO> menuItemList;
//
//    @BeforeEach
//    public void setup() {
//        restaurantDTO = new RestaurantDTO(1,1L, "Test Restaurant","Test Address", "600001", "123456789");
////        menuItemDTO = new MenuItemDTO(1L,1L,"Test Item","Delicious food", new BigDecimal("100.0"),"Veg", true,"test-image.jpg");
//        restaurantList = Collections.singletonList(restaurantDTO);
//        menuItemList = Collections.singletonList(menuItemDTO);
//        mockMvc = MockMvcBuilders.standaloneSetup(customerMenuController).build();
//    }
//
//    @AfterEach
//    public void tearDown() {
//        restaurantDTO = null;
//        menuItemDTO = null;
//        restaurantList = null;
//        menuItemList = null;
//    }
//
//    @Test
//    public void testGetNearbyRestaurants() throws Exception {
////        when(customerMenuService.getNearbyRestaurants(anyString())).thenReturn(restaurantList);
//
//        mockMvc.perform(get("/api/v1/restaurants/nearby")
//                .param("pinCode", "600001")
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andDo(MockMvcResultHandlers.print());
//
////        verify(customerMenuService, times(1)).getNearbyRestaurants("600001");
//    }
//
//    @Test
//    public void testGetRestaurantById() throws Exception {
//        when(customerMenuService.getRestaurantById(anyLong())).thenReturn(restaurantDTO);
//
//        mockMvc.perform(get("/api/v1/restaurants/1")
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andDo(MockMvcResultHandlers.print());
//
//        verify(customerMenuService, times(1)).getRestaurantById(1L);
//    }
//
//    @Test
//    public void testGetRestaurantMenu() throws Exception {
//        when(customerMenuService.getRestaurantMenu(anyLong())).thenReturn(menuItemList);
//
//        mockMvc.perform(get("/api/v1/menu/1")
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andDo(MockMvcResultHandlers.print());
//
//        verify(customerMenuService, times(1)).getRestaurantMenu(1L);
//    }
//
//    @Test
//    public void testSearchMenuItems() throws Exception {
////        when(customerMenuService.searchMenuItems(anyString(), Long())).thenReturn(menuItemList);
//
//        mockMvc.perform(get("/api/v1/menu/search")
//                .param("query", "Biryani")
//                .param("pinCode", "600001")
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andDo(MockMvcResultHandlers.print());
//
//        verify(customerMenuService, times(1)).searchMenuItems("Biryani", 2L);
//    }
//
//    @Test
//    public void testGetMenuItemsByCategory() throws Exception {
//        when(customerMenuService.getMenuItemsByCategory(anyLong(), anyString())).thenReturn(menuItemList);
//
//        mockMvc.perform(get("/api/v1/menu/filter")
//                .param("restaurantId", "1")
//                .param("category", "Veg")
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andDo(MockMvcResultHandlers.print());
//
//        verify(customerMenuService, times(1)).getMenuItemsByCategory(1L, "Veg");
//    }
//
//    public static String asJsonString(final Object obj) {
//        try {
//            return new ObjectMapper().writeValueAsString(obj);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//}