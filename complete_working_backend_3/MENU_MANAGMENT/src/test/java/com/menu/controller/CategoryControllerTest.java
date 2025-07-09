//package com.menu.controller;
//
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//import java.util.List;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.menu.dto.CategoryDTO;
//import com.menu.service.CategoryService;
//
//@ExtendWith(MockitoExtension.class)
//public class CategoryControllerTest {
//
//    @Mock
//    private CategoryService categoryService;
//
//    @InjectMocks
//    private CategoryController categoryController;
//
//    private MockMvc mockMvc;
//
//    private CategoryDTO categoryDTO;
//
//    @BeforeEach
//    public void setup() {
//        categoryDTO = new CategoryDTO(1L, "Test Category", "Test Description");
//        mockMvc = MockMvcBuilders.standaloneSetup(categoryController).build();
//    }
//
//    @Test
//    public void testCreateCategory() throws Exception {
//        when(categoryService.createCategory(any())).thenReturn(categoryDTO);
//
//        mockMvc.perform(post("/api/v1/categories")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(asJsonString(categoryDTO)))
//                .andExpect(status().isCreated());
//
//        verify(categoryService, times(1)).createCategory(any());
//    }
//	@Test
//	public void testGetCategoryById() throws Exception {
//		when(categoryService.getCategoryById(1L)).thenReturn(categoryDTO);
//
//		mockMvc.perform(get("/api/v1/categories/1")
//				.contentType(MediaType.APPLICATION_JSON))
//				.andExpect(status().isOk())
//				.andDo(MockMvcResultHandlers.print());
//
//		verify(categoryService, times(1)).getCategoryById(1L);
//	}
//
//	@Test
//	public void testGetAllCategories() throws Exception {
//		List<CategoryDTO> categoryList = List.of(categoryDTO);
//		when(categoryService.getAllCategories()).thenReturn(categoryList);
//
//		mockMvc.perform(get("/api/v1/categories")
//				.contentType(MediaType.APPLICATION_JSON))
//				.andExpect(status().isOk())
//				.andDo(MockMvcResultHandlers.print());
//
//		verify(categoryService, times(1)).getAllCategories();
//	}
//    private static String asJsonString(final Object obj) {
//        try {
//            return new ObjectMapper().writeValueAsString(obj);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//}