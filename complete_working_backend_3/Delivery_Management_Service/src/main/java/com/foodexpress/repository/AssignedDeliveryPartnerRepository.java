package com.foodexpress.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.foodexpress.entity.AssignedDeliveryPartner;

public interface AssignedDeliveryPartnerRepository extends JpaRepository<AssignedDeliveryPartner, Integer> {
	List<AssignedDeliveryPartner> findByServicePincodeAndDeliveryPartnerStatus(String customerPincode, boolean status);

	@Query("SELECT d FROM AssignedDeliveryPartner d WHERE d.deliveryPartnerStatus = true ")
	List<AssignedDeliveryPartner> findFirstByStatus();
	@Query("select d from AssignedDeliveryPartner d where d.deliveryPartnerStatus=true and d.servicePincode=?1")
	List<AssignedDeliveryPartner> findByCustomerPincodeAndStatus(String customerPincode);
	
	AssignedDeliveryPartner findByUserId(int userId);
}
