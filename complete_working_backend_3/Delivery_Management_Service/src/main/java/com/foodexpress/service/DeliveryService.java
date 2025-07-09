package com.foodexpress.service;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import com.foodexpress.dto.AssignDeliveryRequestDTO;
import com.foodexpress.dto.DeliveryStatusDTO;
import com.foodexpress.dto.PartnerDTO;
import com.foodexpress.entity.AssignedDeliveryPartner;
import com.foodexpress.entity.Delivery;
import com.foodexpress.enums.Status;

public interface DeliveryService {
	void assignDelivery(AssignDeliveryRequestDTO request);

	String getDeliveryStatus(AssignDeliveryRequestDTO request);

	void updateDeliveryStatus(DeliveryStatusDTO request);

	void notifyCustomer(Long orderId);
	
	ResponseEntity<PartnerDTO> createDeliveryPartner(@RequestBody PartnerDTO partnerDTO);
		
	public boolean getDeliveryAgentStatus(String customerPincode);
		 
	 public Delivery getDeliveryDetails(Long orderId);
	 
	 public Delivery getDeliveryByPartnerId(int partnerId);
	 
	 public AssignedDeliveryPartner getPartnersByUserId(int userId);
	 
	 public  List<Delivery> getPreviousDeliveries(int partnerId);

	void updateOrderStatus(int deliveryId, Status status);
	
	public ResponseEntity<String> updateCODDelivery(Long orderId);
}