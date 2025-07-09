//package com.foodexpress.repository;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//import static org.mockito.Mockito.when;
//
//import java.util.Optional;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import com.foodexpress.entity.Delivery;
//
//class DeliveryRepositoryTest {
//
//	@Mock
//	private DeliveryRepository deliveryRepository;
//
//	private Delivery delivery;
//
//	@BeforeEach
//	void setUp() {
//		MockitoAnnotations.openMocks(this);
//
//		delivery = new Delivery();
//		delivery.setDeliveryId(1);
//		delivery.setOrderId(101);
//		delivery.setDeliveryStatus("In Progress");
//		delivery.setCustomerPincode("560001");
//	}
//
//	@Test
//	void testFindByOrderId_Success() {
//		when(deliveryRepository.findByOrderId(101)).thenReturn(Optional.of(delivery));
//
//		Optional<Delivery> result = deliveryRepository.findByOrderId(101);
//
//		assertTrue(result.isPresent());
//		assertEquals(101, result.get().getOrderId());
//		assertEquals("In Progress", result.get().getDeliveryStatus());
//	}
//
//	@Test
//	void testFindByOrderId_NotFound() {
//		when(deliveryRepository.findByOrderId(102)).thenReturn(Optional.empty());
//
//		Optional<Delivery> result = deliveryRepository.findByOrderId(102);
//
//		assertTrue(result.isEmpty());
//	}
//	
//	@Test
//	void testFindByOrderId_NullInput() {
//	    when(deliveryRepository.findByOrderId(null)).thenReturn(Optional.empty());
//
//	    Optional<Delivery> result = deliveryRepository.findByOrderId(null);
//
//	    assertTrue(result.isEmpty());
//	}
//	
//	@Test
//	void testFindByOrderId_NonExistentOrderId() {
//	    when(deliveryRepository.findByOrderId(999)).thenReturn(Optional.empty());
//
//	    Optional<Delivery> result = deliveryRepository.findByOrderId(999);
//
//	    assertTrue(result.isEmpty());
//	}
//	
//	@Test
//	void testFindByOrderId_LargeOrderId() {
//	    when(deliveryRepository.findByOrderId(Integer.MAX_VALUE)).thenReturn(Optional.empty());
//
//	    Optional<Delivery> result = deliveryRepository.findByOrderId(Integer.MAX_VALUE);
//
//	    assertTrue(result.isEmpty());
//	}
//	
//	@Test
//	void testFindByOrderId_NegativeOrderId() {
//	    when(deliveryRepository.findByOrderId(-1)).thenReturn(Optional.empty());
//
//	    Optional<Delivery> result = deliveryRepository.findByOrderId(-1);
//
//	    assertTrue(result.isEmpty());
//	}
//	
//	@Test
//	void testFindByOrderId_DifferentStatus() {
//	    delivery.setDeliveryStatus("Delivered");
//	    when(deliveryRepository.findByOrderId(101)).thenReturn(Optional.of(delivery));
//
//	    Optional<Delivery> result = deliveryRepository.findByOrderId(101);
//
//	    assertTrue(result.isPresent());
//	    assertEquals("Delivered", result.get().getDeliveryStatus());
//	}
//	
//	@Test
//	void testFindByOrderId_MultipleCalls() {
//	    when(deliveryRepository.findByOrderId(101)).thenReturn(Optional.of(delivery));
//
//	    Optional<Delivery> firstCall = deliveryRepository.findByOrderId(101);
//	    Optional<Delivery> secondCall = deliveryRepository.findByOrderId(101);
//
//	    assertTrue(firstCall.isPresent());
//	    assertTrue(secondCall.isPresent());
//	    assertEquals(firstCall.get(), secondCall.get());
//	}
//	
//	@Test
//	void testFindByOrderId_NullFieldsInDelivery() {
//	    delivery.setDeliveryStatus(null);
//	    delivery.setCustomerPincode(null);
//
//	    when(deliveryRepository.findByOrderId(101)).thenReturn(Optional.of(delivery));
//
//	    Optional<Delivery> result = deliveryRepository.findByOrderId(101);
//
//	    assertTrue(result.isPresent());
//	    assertEquals(101, result.get().getOrderId());
//	    assertEquals(null, result.get().getDeliveryStatus());
//	    assertEquals(null, result.get().getCustomerPincode());
//	}
//	
//	@Test
//	void testFindByOrderId_EmptyDatabase() {
//	    when(deliveryRepository.findByOrderId(101)).thenReturn(Optional.empty());
//
//	    Optional<Delivery> result = deliveryRepository.findByOrderId(101);
//
//	    assertTrue(result.isEmpty());
//	}
//	
//	
//}