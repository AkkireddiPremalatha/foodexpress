//package com.menu.controller;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
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
//import com.menu.dto.AddMenuItemRequest;
//import com.menu.dto.MenuItemDTO;
//import com.menu.dto.UpdateAvailabilityRequest;
//import com.menu.dto.UpdateMenuItemRequest;
//import com.menu.service.RestaurantMenuService;
//
//@ExtendWith(MockitoExtension.class)
//public class RestaurantMenuControllerTest {
//
//    @Mock
//    private RestaurantMenuService restaurantMenuService;
//
//    @InjectMocks
//    private RestaurantMenuController restaurantMenuController;
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    private MenuItemDTO menuItemDTO;
//    private AddMenuItemRequest addMenuItemRequest;
//    private UpdateMenuItemRequest updateMenuItemRequest;
//    private UpdateAvailabilityRequest updateAvailabilityRequest;
//
//    @BeforeEach
//    public void setup() {
////        menuItemDTO = new MenuItemDTO(1L, 1L, "Test Item", "Delicious food", 
////                new java.math.BigDecimal("100.0"), "Veg", true, "test-image.jpg");
////        addMenuItemRequest = new AddMenuItemRequest( 1L, 2L,"Test Item","Delicious food", new java.math.double("100.0"),true  );
//        updateMenuItemRequest = new UpdateMenuItemRequest("Updated Item", "Updated description",new java.math.BigDecimal("150.0"),false,2L);
//        updateAvailabilityRequest = new UpdateAvailabilityRequest(false);
//
//        mockMvc = MockMvcBuilders.standaloneSetup(restaurantMenuController).build();
//    }
//
//    @AfterEach
//    public void tearDown() {
//        menuItemDTO = null;
//        addMenuItemRequest = null;
//        updateMenuItemRequest = null;
//        updateAvailabilityRequest = null;
//    }
//
//    @Test
//    public void testAddMenuItem() throws Exception {
//        when(restaurantMenuService.addMenuItem(any(), any())).thenReturn(menuItemDTO);
//
//        mockMvc.perform(post("/api/v1/menu/add") // Use POST instead of multipart
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(asJsonString(addMenuItemRequest)))
//                .andExpect(status().isCreated()) // Expect 201 Created
//                .andDo(MockMvcResultHandlers.print());
//
//        verify(restaurantMenuService, times(1)).addMenuItem(any(), any());
//    }
//
//    @Test
//    public void testUpdateMenuItem() throws Exception {
//        when(restaurantMenuService.updateMenuItem(anyLong(), any(), any())).thenReturn(menuItemDTO);
//
//        mockMvc.perform(put("/api/v1/menu/update/1") // Use PUT instead of multipart
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(asJsonString(updateMenuItemRequest)))
//                .andExpect(status().isOk()) // Expect 200 OK
//                .andDo(MockMvcResultHandlers.print());
//
//        verify(restaurantMenuService, times(1)).updateMenuItem(anyLong(), any(), any());
//    }
//
//    @Test
//    public void testDeleteMenuItem() throws Exception {
//        mockMvc.perform(delete("/api/v1/menu/delete/1")
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isNoContent())
//                .andDo(MockMvcResultHandlers.print());
//
//        verify(restaurantMenuService, times(1)).deleteMenuItem(anyLong());
//    }
//
//    @Test
//    public void testUpdateMenuItemAvailability() throws Exception {
//        when(restaurantMenuService.updateMenuItemAvailability(anyLong(), any())).thenReturn(menuItemDTO);
//
//        mockMvc.perform(put("/api/v1/menu/availability/1")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(asJsonString(updateAvailabilityRequest)))
//                .andExpect(status().isOk())
//                .andDo(MockMvcResultHandlers.print());
//
//        verify(restaurantMenuService, times(1)).updateMenuItemAvailability(anyLong(), any());
//    }
//
//    @Test
//    public void testGetMenuItemById() throws Exception {
//        when(restaurantMenuService.getMenuItemById(anyLong())).thenReturn(menuItemDTO);
//
//        mockMvc.perform(get("/api/v1/menu/item/1")
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andDo(MockMvcResultHandlers.print());
//
//        verify(restaurantMenuService, times(1)).getMenuItemById(anyLong());
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