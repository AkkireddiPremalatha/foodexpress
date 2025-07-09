package com.foodexpress.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.foodexpress.model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
	// Finds the most recent order for a given customer based on the creation
	// timestamp.
	Optional<Order> findTopByCustomerIdOrderByCreatedAtDesc(Long customerId);

	@Query("SELECT o FROM Order o WHERE o.status = 'DELIVERED' OR o.paymentMethod = 'COD'")
	List<Order> findOrdersWithSuccessfulStatusOrCOD();
	
	@Query("SELECT o FROM Order o "
			+ "WHERE o.customerId = ?1 and o.status in ('ACCEPTED','PREPARING','READY','PICKED_UP','ON_THE_WAY')")
	Order findOrderDetailsByCustomerId(Long customerId);
	
	@Query("select o from Order o where o.restaurantId = ?1 and o.status in ('ACCEPTED','PREPARING','READY','PICKED_UP','ON_THE_WAY')")
	List<Order> findByRestaurantIdAndStatus(Long restaurantId);
	
	@Query("select o from Order o where o.customerId=?1 and o.status='DELIVERED'")
	List<Order> findByPastOrders(Long customerId);
	
	@Query("select o from Order o where o.restaurantId=?1 and o.status = 'DELIVERED'")
	List<Order> findByRestaurantIdAndDelivered(Long restaurantId);
}