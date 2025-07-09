package com.foodexpress.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.foodexpress.entity.DeliveryPartner;

@Repository
public interface DeliveryAgentRepository extends JpaRepository<DeliveryPartner, Integer>{
	DeliveryPartner findByEmail(String email);
}
