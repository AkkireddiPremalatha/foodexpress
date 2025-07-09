package com.foodexpress.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import com.foodexpress.entity.Customer;

@Component

public interface CustomerRepository extends JpaRepository<Customer, Long> {

	Customer findByEmail(String email);
	@Query("SELECT c FROM Customer c WHERE c.userId=?1")
	Customer findCustomerById(Long userId);
	@Query("SELECT COUNT(c) > 0 FROM Customer c WHERE c.userId = :userId")
	boolean existsById(@Param("userId") Long userId);
}
