package com.foodexpress.service;


//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;

//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.foodexpress.dto.AssignDeliveryRequestDTO;
import com.foodexpress.dto.DeliveryStatusDTO;
import com.foodexpress.dto.PartnerDTO;
import com.foodexpress.entity.AssignedDeliveryPartner;
import com.foodexpress.entity.Delivery;
import com.foodexpress.enums.Status;
import com.foodexpress.exception.InvalidDeliveryStatusException;
import com.foodexpress.exception.OrderAlreadyExistException;
import com.foodexpress.exception.OrderNotFoundException;
import com.foodexpress.exception.ResourceNotFoundException;
import com.foodexpress.feign.OrderServiceFeignClient;
import com.foodexpress.feign.PaymentServiceFeignClient;
import com.foodexpress.feign.UserServiceFeignClient;
import com.foodexpress.repository.AssignedDeliveryPartnerRepository;
import com.foodexpress.repository.DeliveryPartnerRepository;
import com.foodexpress.repository.DeliveryRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class DeliveryServiceImpl implements DeliveryService {

	private final OrderServiceFeignClient orderServiceFeignClient;

	private final DeliveryRepository deliveryRepository;
	
	private final UserServiceFeignClient userServiceFeignClient;

	private final DeliveryPartnerRepository deliveryPartnerRepository;
	
	private final PaymentServiceFeignClient paymentServiceFeignClient;

	private Delivery delivery;

	private AssignedDeliveryPartner deliveryPartner;

	private final AssignedDeliveryPartnerRepository assignedDeliveryPartnerRepository;

	private final UserServiceFeignClient userFeignServiceClient;

	String delivered = "Delivered";
	String inProgress = "In Progress";
	

	@Override
	public void assignDelivery(AssignDeliveryRequestDTO request)
			throws ResourceNotFoundException, OrderAlreadyExistException {
		log.info("Assigning delivery for order ID: {}", request.getOrderId());

		Delivery getByOrderId = deliveryRepository.findByOrderId(request.getOrderId()).orElse(null);

		if (getByOrderId != null) {
			if (inProgress.equalsIgnoreCase(getByOrderId.getDeliveryStatus())) {
				log.warn("Order with ID {} is already in progress.", request.getOrderId());
				throw new OrderAlreadyExistException("Order with " + request.getOrderId() + " already exists.");
			}
			if (delivered.equalsIgnoreCase(getByOrderId.getDeliveryStatus())) {
				log.warn("Order with ID {} is already delivered and cannot be reassigned.", request.getOrderId());
				throw new OrderAlreadyExistException("Order with " + request.getOrderId()
						+ " already exists or delivered and cannot be reassigned.");
			}
		}
		System.out.println(request.getCustomerPincode());

		List<AssignedDeliveryPartner> availableAgents = assignedDeliveryPartnerRepository
				.findByCustomerPincodeAndStatus(request.getCustomerPincode());

		if (availableAgents.isEmpty()) {
			log.error("No delivery agents available for order ID: {} in pincode: {}", request.getOrderId(),
					request.getCustomerPincode());
			throw new ResourceNotFoundException("No delivery agents available in the specified pincode.");
		}

		AssignedDeliveryPartner assignedAgent = availableAgents.get(0);
	    AssignedDeliveryPartner managedAgent = assignedDeliveryPartnerRepository.findById(assignedAgent.getPartnerId())
	            .orElseThrow(() -> new ResourceNotFoundException("Agent not found"));

	    Delivery delivery = new Delivery();
	    delivery.setOrderId(request.getOrderId());
	    delivery.setCustomerPincode(request.getCustomerPincode());
	    delivery.setDeliveryPartner(managedAgent);
	    delivery.setDeliveryStatus(inProgress);

	    deliveryRepository.save(delivery);
	    log.info("Setting delivery agent status to false for the partner id: {}",managedAgent.getPartnerId());
	    managedAgent.setDeliveryPartnerStatus(false);
	    assignedDeliveryPartnerRepository.save(managedAgent);

	    log.info("Delivery assigned to partner ID: {} for order ID: {}", managedAgent.getPartnerId(), request.getOrderId());
	}
//	@Override
//	public void assignDeliveriesToCODOrSuccessfulOrders() {
//	    List<OrderResponseDTO> orders = orderServiceFeignClient.getAllOrdersWithCODAndPaymentSuccessful();
//
//	    for (OrderResponseDTO order : orders) {
//	        try {
//	            Long customerId = order.getCustomerId();
//	            CustomerResponseDto customer = userServiceFeignClient.getCustomerById(customerId);
//	            String customerPincode = customer.getCustomerPincode();
//
//	            if (customerPincode == null || customerPincode.isBlank()) {
//	                log.warn("Skipping order {} due to missing pincode from customer {}", order.getOrderId(), customerId);
//	                continue;
//	            }
//
//	            AssignDeliveryRequestDTO request = new AssignDeliveryRequestDTO();
//	            request.setOrderId(order.getOrderId().intValue());
//	            request.setCustomerPincode(customerPincode);
//
//	            assignDelivery(request);
//
//	        } catch (OrderAlreadyExistException | ResourceNotFoundException e) {
//	            log.warn("Order {} skipped: {}", order.getOrderId(), e.getMessage());
//	        } catch (Exception e) {
//	            log.error("Unexpected error while assigning order {}: {}", order.getOrderId(), e.getMessage());
//	        }
//	    }
//	}



	@Override
	public String getDeliveryStatus(AssignDeliveryRequestDTO request) throws OrderNotFoundException {
		log.info("Fetching delivery status for order ID: {}", request.getOrderId());
		Delivery delivery = deliveryRepository.findByOrderId(request.getOrderId()).orElseThrow(() -> {
			log.error("Order with ID {} not found.", request.getOrderId());
			return new OrderNotFoundException("Order not found.");
		});

		AssignDeliveryRequestDTO deliveryStatusDTO = new AssignDeliveryRequestDTO();
		deliveryStatusDTO.setOrderId(delivery.getOrderId());
		deliveryStatusDTO.setDeliveryStatus(delivery.getDeliveryStatus());
		log.info("Delivery status for order ID {}: {}", request.getOrderId(), delivery.getDeliveryStatus());
		return delivery.getDeliveryStatus();
	}

	@Override
	public void updateDeliveryStatus(DeliveryStatusDTO request)
			throws ResourceNotFoundException, OrderNotFoundException {
		log.info("Updating delivery status for order ID: {} to {}", request.getOrderId(), request.getDeliveryStatus());
		
		Delivery delivery = deliveryRepository.findByOrderId(request.getOrderId()).orElseThrow(() -> {
			log.error("Order with ID {} not found.", request.getOrderId());
			return new OrderNotFoundException("Order not found.");
		});

		if (!inProgress.equalsIgnoreCase(request.getDeliveryStatus())
				&& !delivered.equalsIgnoreCase(request.getDeliveryStatus())) {
			log.error("Invalid delivery status: {}", request.getDeliveryStatus());
			throw new InvalidDeliveryStatusException("Invalid delivery status: " + request.getDeliveryStatus());
		}

		delivery.setDeliveryStatus(request.getDeliveryStatus());
		

		if (delivered.equalsIgnoreCase(request.getDeliveryStatus())) {
			int partnerid = delivery.getDeliveryPartner().getPartnerId();
			AssignedDeliveryPartner deliveryPartner = deliveryPartnerRepository.findById(partnerid).orElseThrow(() -> {
				log.error("Delivery partner with ID {} not found.", partnerid);
				return new ResourceNotFoundException("Delivery partner not found.");
			});
			deliveryPartner.setDeliveryPartnerStatus(true);
			deliveryPartnerRepository.save(deliveryPartner);
			log.info("Delivery partner with ID {} marked as available.", partnerid);
		}
		deliveryRepository.save(delivery);
		log.info("Delivery status for order ID {} updated to {}", request.getOrderId(), request.getDeliveryStatus());
	}

	@Override
	public void notifyCustomer(Long orderId) {
		if (orderId == null) {
			throw new NullPointerException("Order ID cannot be null.");
		}

		log.info("Notifying customer for order ID: {}", orderId);

		Delivery delivery = deliveryRepository.findByOrderId(orderId).orElseThrow(() -> {
			log.error("Order with ID {} not found.", orderId);
			return new ResourceNotFoundException("Order not found.");
		});
		
	}

	/*
	 * AssignedDeliveryPartner partner = delivery.getDeliveryPartner(); if (partner
	 * == null) { log.error("Delivery partner not found for order ID: {}", orderId);
	 * throw new ResourceNotFoundException("Delivery partner not found."); }
	 * 
	 * String notificationMessage = "Dear Customer, your order " + orderId +
	 * " is currently " + delivery.getDeliveryStatus() ".");
	 * log.info("Notification sent: {}", notificationMessage); }
	 */


	public boolean getDeliveryAgentStatus(String customerPincode) {
		List<AssignedDeliveryPartner> deliveryPartners = assignedDeliveryPartnerRepository.findByCustomerPincodeAndStatus(customerPincode);
		if(deliveryPartners.isEmpty()) {
			return false;
		}
		return true;
				
	}
	
	public ResponseEntity<PartnerDTO> createDeliveryPartner(@RequestBody PartnerDTO partnerDTO){
		AssignedDeliveryPartner partner = new AssignedDeliveryPartner();
	    partner.setUserId(partnerDTO.getUserId());
	    partner.setName(partnerDTO.getName());
	    partner.setEmail(partnerDTO.getEmail());
	    partner.setPhoneNumber(partnerDTO.getPhoneNumber());
	    partner.setVehicleNumber(partnerDTO.getVehicleNumber());
	    partner.setAddress(partnerDTO.getAddress());
	    partner.setServicePincode(partnerDTO.getServicePincode());
	    partner.setDeliveryPartnerStatus(partnerDTO.getDeliveryPartnerStatus());

	    // Save to DB
	    AssignedDeliveryPartner saved = assignedDeliveryPartnerRepository.save(partner);
	    PartnerDTO savedDTO = new PartnerDTO(
	            saved.getUserId(),
	            saved.getName(),
	            saved.getEmail(),
	            saved.getPhoneNumber(),
	            saved.getVehicleNumber(),
	            saved.getAddress(),
	            saved.getServicePincode(),
	            saved.getDeliveryPartnerStatus()
	        );

	        return new ResponseEntity<>(savedDTO, HttpStatus.CREATED);
	}
	
	@Override
	public void updateOrderStatus(int deliveryId,Status status) throws RuntimeException{
		Delivery partner=deliveryRepository.findById(deliveryId).orElseThrow(null);
		if(partner!=null) {
			orderServiceFeignClient.setOrderStatus(partner.getOrderId(),status);
		}else {
		throw new RuntimeException("Partner with partner id not found.");
		}
	}
	
	@Override
	public Delivery getDeliveryDetails(Long orderId) {
		Optional<Delivery> deliveryDetails = deliveryRepository.findByOrderId(orderId);
		Delivery delivery = null;
		if(deliveryDetails.isPresent()) {
			return deliveryDetails.get();
		}
		return delivery;
		
	}
	
	@Override
	public Delivery getDeliveryByPartnerId(int partnerId) {
		Delivery delivery = deliveryRepository.findDeliveryByPartnerId(partnerId);
		if(delivery!=null) {
			return delivery;
		}
		else {
			throw new ResourceNotFoundException("No partner found with the given partner id!");
		}
		
	}
	
	public AssignedDeliveryPartner getPartnersByUserId(int userId) {
		AssignedDeliveryPartner deliveryPartner = assignedDeliveryPartnerRepository.findByUserId(userId);
		return deliveryPartner;
	}
	
	//this method should use deliveryId to uniquely fetch a delivery record
	@Override
	public List<Delivery> getPreviousDeliveries(int partnerId) {
		List<Delivery> delivery = deliveryRepository.findByDeliveryPartner_PartnerIdAndDeliveryStatus(partnerId, "DELIVERED");
		return delivery;
	}
	
	@Override
	public ResponseEntity<String> updateCODDelivery(Long orderId) {
		ResponseEntity<String> delivery = paymentServiceFeignClient.confirmCODPayment(orderId);
		return delivery;
	}

}