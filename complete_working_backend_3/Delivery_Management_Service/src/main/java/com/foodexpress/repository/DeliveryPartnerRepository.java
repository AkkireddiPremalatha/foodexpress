package com.foodexpress.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.foodexpress.entity.AssignedDeliveryPartner;

public interface DeliveryPartnerRepository extends JpaRepository<AssignedDeliveryPartner, Integer> {



}
