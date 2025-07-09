package com.foodexpress.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.foodexpress.entity.Delivery;

public interface DeliveryRepository extends JpaRepository<Delivery, Integer> {

	
	Optional<Delivery> findByOrderId(Long orderId);
	
	
	@Query("select d from Delivery d where d.deliveryPartner.partnerId = ?1")
	Delivery findByDeliveryPartner(int partnerId);
	
	@Query("select d from Delivery d where d.deliveryPartner.partnerId = ?1 and d.deliveryStatus='In Progress'")
	Delivery findDeliveryByPartnerId(Integer partnerId);
	
	 List<Delivery> findByDeliveryPartner_PartnerIdAndDeliveryStatus(Integer partnerId, String deliveryStatus);
	

}