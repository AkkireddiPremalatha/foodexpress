package com.foodexpress.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.foodexpress.model.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Cart> findByCustomerId(Long customerId);
    void deleteByCustomerId(Long customerId);
    void deleteByCustomerIdAndMenuItemId(Long customerId, Long menuItemId);
    @Query("select c from Cart c where c.customerId=?1 and c.menuItemId=?2 ")
	//Cart findTopByCustomerIdAndMenuItemIdOrderByCreatedAtDesc(Long customerId, Long menuItemId);
	List<Cart> findByCustomerIdAndMenuItemId(Long customerId, Long menuItemId);
}