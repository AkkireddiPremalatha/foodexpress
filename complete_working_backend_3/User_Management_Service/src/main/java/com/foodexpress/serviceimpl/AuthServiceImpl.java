package com.foodexpress.serviceimpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.foodexpress.dto.CustomerProfileDto;
import com.foodexpress.dto.CustomerRegisterRequestDto;
import com.foodexpress.dto.CustomerResponseDto;
import com.foodexpress.dto.DeliveryAgentRequestDto;
import com.foodexpress.dto.DeliveryAgentResponseDto;
import com.foodexpress.dto.PartnerDTO;
import com.foodexpress.dto.RestaurantDTO;
import com.foodexpress.dto.RestaurantOwnerRequestDto;
import com.foodexpress.entity.Customer;
import com.foodexpress.entity.DeliveryPartner;
import com.foodexpress.entity.RestaurantOwner;
import com.foodexpress.entity.Roles;
import com.foodexpress.exception.EmailAlreadyRegisteredException;
import com.foodexpress.exception.InvalidCredentialsException;
import com.foodexpress.exception.RoleNotFoundException;
import com.foodexpress.feign.DeliveryServiceFeignClient;
import com.foodexpress.feign.MenuServiceFeignClient;
//import com.foodexpress.exception.UserNotFoundException;
import com.foodexpress.repository.CustomerRepository;
import com.foodexpress.repository.DeliveryAgentRepository;
import com.foodexpress.repository.RestaurantOwnerRepository;
import com.foodexpress.repository.RolesRepository;
import com.foodexpress.security.JwtUtil;
import com.foodexpress.service.AuthService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

	private final CustomerRepository customerRepository;
	private final DeliveryAgentRepository deliveryAgentRepository;
	private final RestaurantOwnerRepository restaurantOwnerRepository;
	private final RolesRepository rolesRepository;
	private final PasswordEncoder passwordEncoder;
	private final DeliveryServiceFeignClient deliveryServiceFeignClient;
	private final MenuServiceFeignClient menuServiceFeignClient;
	private final JwtUtil jwtUtil;

	@Override
	public boolean isEmailAlreadyRegistered(String email) {
		return customerRepository.findByEmail(email) != null || deliveryAgentRepository.findByEmail(email) != null
				|| restaurantOwnerRepository.findByEmail(email) != null;
	}

	@Transactional
	@Override
	public CustomerRegisterRequestDto registerCustomer(CustomerRegisterRequestDto customerDto) {
		log.info("Registering customer with email: {}", customerDto.getEmail());

		if (isEmailAlreadyRegistered(customerDto.getEmail())) {
			throw new EmailAlreadyRegisteredException("Email " + customerDto.getEmail() + " is already registered.");
		}

		Roles customerRole = rolesRepository.findById(2)
				.orElseThrow(() -> new RoleNotFoundException("Customer role not found in database for ID 2."));

		Customer customer = new Customer();
		customer.setFirstName(customerDto.getFirstName());
		customer.setCustomerPincode(customerDto.getCustomerPincode());
		customer.setLastName(customerDto.getLastName());
		customer.setEmail(customerDto.getEmail());
		customer.setPassword(passwordEncoder.encode(customerDto.getPassword()));
		customer.setPhoneNo(customerDto.getPhoneNo());
		customer.setAddress(customerDto.getAddress());
		customer.setRoles(customerRole);

		customerRepository.save(customer);
		return customerDto;
	}

	@Transactional
	@Override
	public DeliveryAgentRequestDto registerDeliveryAgents(DeliveryAgentRequestDto deliveryAgentRequestDto) {
		log.info("Registering delivery agent with email: {}", deliveryAgentRequestDto.getEmail());

		if (isEmailAlreadyRegistered(deliveryAgentRequestDto.getEmail())) {
			throw new EmailAlreadyRegisteredException(
					"Email " + deliveryAgentRequestDto.getEmail() + " is already registered.");
		}

		Roles deliveryAgentRole = rolesRepository.findById(3)
				.orElseThrow(() -> new RoleNotFoundException("Delivery Agent role not found in database for ID 3."));

		DeliveryPartner deliveryAgent = new DeliveryPartner();
		deliveryAgent.setName(deliveryAgentRequestDto.getName());
		deliveryAgent.setEmail(deliveryAgentRequestDto.getEmail());
		deliveryAgent.setPassword(passwordEncoder.encode(deliveryAgentRequestDto.getPassword()));
		deliveryAgent.setPhoneNo(deliveryAgentRequestDto.getPhoneNo());
		deliveryAgent.setAddress(deliveryAgentRequestDto.getAddress());
		deliveryAgent.setDeliveryPartnerStatus(deliveryAgentRequestDto.getDeliveryPartnerStatus());
		deliveryAgent.setVehicleNumber(deliveryAgentRequestDto.getVehicleNumber());
		deliveryAgent.setServicePincode(deliveryAgentRequestDto.getServicePincode());
		deliveryAgent.setAddress(deliveryAgentRequestDto.getAddress());
		deliveryAgent.setRoles(deliveryAgentRole);

		DeliveryPartner saved = deliveryAgentRepository.save(deliveryAgent);
//			return deliveryAgentRequestDto;
		PartnerDTO savedDTO = new PartnerDTO(saved.getUserId(), saved.getName(), saved.getEmail(),
				saved.getPhoneNo(), saved.getVehicleNumber(), saved.getAddress(), saved.getServicePincode(),
				saved.getDeliveryPartnerStatus());
		deliveryServiceFeignClient.createDeliveryPartner(savedDTO);
		return deliveryAgentRequestDto;
	}

	@Transactional
	@Override
	public RestaurantOwnerRequestDto registerRestaurantOwner(RestaurantOwnerRequestDto restaurantOwnerDto) {
		log.info("Registering restaurant owner with email: {}", restaurantOwnerDto.getEmail());

		if (isEmailAlreadyRegistered(restaurantOwnerDto.getEmail())) {
			throw new EmailAlreadyRegisteredException(
					"Email " + restaurantOwnerDto.getEmail() + " is already registered.");
		}

		Roles restaurantOwnerRole = rolesRepository.findById(4)
				.orElseThrow(() -> new RoleNotFoundException("Restaurant Owner role not found in database for ID 4."));

		RestaurantOwner restaurantOwner = new RestaurantOwner();
		restaurantOwner.setFirstName(restaurantOwnerDto.getFirstName());
		restaurantOwner.setLastName(restaurantOwnerDto.getLastName());
		restaurantOwner.setEmail(restaurantOwnerDto.getEmail());
		restaurantOwner.setPassword(passwordEncoder.encode(restaurantOwnerDto.getPassword()));
		restaurantOwner.setPhoneNo(restaurantOwnerDto.getPhoneNo());
		restaurantOwner.setAddress(restaurantOwnerDto.getAddress());
		restaurantOwner.setRoles(restaurantOwnerRole);
		restaurantOwner.setRestaurantName(restaurantOwnerDto.getRestaurantName());
		restaurantOwner.setRestaurantLocation(restaurantOwnerDto.getRestaurantLocation());
		restaurantOwner.setRestaurantPin(restaurantOwnerDto.getRestaurantPin());

		restaurantOwner = restaurantOwnerRepository.save(restaurantOwner);
		RestaurantDTO restaurantDTO = new RestaurantDTO(restaurantOwner.getUserId(), 0,
				restaurantOwnerDto.getRestaurantName(), restaurantOwnerDto.getRestaurantLocation(),
				restaurantOwnerDto.getRestaurantPin(), restaurantOwnerDto.getPhoneNo(), restaurantOwnerDto.getEmail());
		menuServiceFeignClient.createRestaurant(restaurantDTO);

		return restaurantOwnerDto;
	}

	@Override
	public Map<String, Object> login(String email, String password) {
	    try {
	        return authenticateAndValidateRole(email, password, "Admin");
	    } catch (Exception e) { }
 
	    try {
	        return authenticateAndValidateRole(email, password, "Customer");
	    } catch (Exception e) { }
 
	    try {
	        return authenticateAndValidateRole(email, password, "Delivery Agent");
	    } catch (Exception e) { }
 
	    try {
	        return authenticateAndValidateRole(email, password, "Restaurant Owner");
	    } catch (Exception e) { }
 
	    throw new InvalidCredentialsException("User not found or invalid credentials");
	}
 
 
	private Map<String, Object> authenticateAndValidateRole(String email, String password, String expectedRoleName) {
	    log.info("Authenticating user with email: {}", email);
 
	    Customer customer = customerRepository.findByEmail(email);
	    if (customer != null && passwordEncoder.matches(password, customer.getPassword())
	        && customer.getRoles() != null
	        && customer.getRoles().getRoleName().equalsIgnoreCase(expectedRoleName)) {
 
	        String role = customer.getRoles().getRoleName();
	        String token = jwtUtil.generateToken(customer.getEmail(), role);
 
	        Map<String, Object> response = new HashMap<>();
	        response.put("token", token);
	        response.put("id", customer.getUserId());
	        response.put("role", role); // 👈 Add role here
	        response.put("name", customer.getFirstName());
	        return response;
	    }
 
	    DeliveryPartner deliveryAgent = deliveryAgentRepository.findByEmail(email);
	    if (deliveryAgent != null && passwordEncoder.matches(password, deliveryAgent.getPassword())
	        && deliveryAgent.getRoles() != null
	        && deliveryAgent.getRoles().getRoleName().equalsIgnoreCase(expectedRoleName)) {
 
	        String role = deliveryAgent.getRoles().getRoleName();
	        String token = jwtUtil.generateToken(deliveryAgent.getEmail(), role);
 
	        Map<String, Object> response = new HashMap<>();
	        response.put("token", token);
	        response.put("id", deliveryAgent.getUserId());
	        response.put("role", role); // 👈 Add role here
	        response.put("name", deliveryAgent.getName());
	        return response;
	    }
 
	    RestaurantOwner restaurantOwner = restaurantOwnerRepository.findByEmail(email);
	    if (restaurantOwner != null && passwordEncoder.matches(password, restaurantOwner.getPassword())
	        && restaurantOwner.getRoles() != null
	        && restaurantOwner.getRoles().getRoleName().equalsIgnoreCase(expectedRoleName)) {
 
	        String role = restaurantOwner.getRoles().getRoleName();
	        String token = jwtUtil.generateToken(restaurantOwner.getEmail(), role);
 
	        Map<String, Object> response = new HashMap<>();
	        response.put("token", token);
	        response.put("id", restaurantOwner.getUserId());
	        response.put("role", role); // 👈 Add role here
	        response.put("name", restaurantOwner.getFirstName());
	        return response;
	    }
 
	    throw new InvalidCredentialsException("Invalid credentials or access denied!");
	}
	@Override
	public CustomerProfileDto viewCustomerProfile(Long customerId) {
		Customer customer = customerRepository.findById(customerId)
				.orElseThrow(() -> new RuntimeException("Customer not found with ID: " + customerId));
		CustomerProfileDto customerProfileDto = new CustomerProfileDto();
		customerProfileDto.setFirstName(customer.getFirstName());
		customerProfileDto.setLastName(customer.getLastName());
		customerProfileDto.setCustomerPincode(customer.getCustomerPincode());
		customerProfileDto.setPhoneNo(customer.getPhoneNo());
		customerProfileDto.setAddress(customer.getAddress());
		return customerProfileDto;
	}

	@Override
	public DeliveryAgentRequestDto viewDeliveryAgentProfile(int agentId) {
		DeliveryPartner deliveryAgent = deliveryAgentRepository.findById(agentId)
				.orElseThrow(() -> new RuntimeException("Delivery Agent not found with ID: " + agentId));
		DeliveryAgentRequestDto deliveryAgentProfileDto = new DeliveryAgentRequestDto();
		deliveryAgentProfileDto.setName(deliveryAgent.getName());
		deliveryAgentProfileDto.setEmail(deliveryAgent.getEmail());
		deliveryAgentProfileDto.setPhoneNo(deliveryAgent.getPhoneNo());
		deliveryAgentProfileDto.setAddress(deliveryAgent.getAddress());
		return deliveryAgentProfileDto;
	}

	@Override
	public RestaurantOwnerRequestDto viewRestaurantOwnerProfile(int ownerId) {
		RestaurantOwner restaurantOwner = restaurantOwnerRepository.findById(ownerId)
				.orElseThrow(() -> new RuntimeException("Restaurant Owner not found with ID: " + ownerId));
		RestaurantOwnerRequestDto restaurantOwnerProfileDto = new RestaurantOwnerRequestDto();
		restaurantOwnerProfileDto.setFirstName(restaurantOwner.getFirstName());
		restaurantOwnerProfileDto.setLastName(restaurantOwner.getLastName());
		restaurantOwnerProfileDto.setEmail(restaurantOwner.getEmail());
		restaurantOwnerProfileDto.setPhoneNo(restaurantOwner.getPhoneNo());
//			restaurantOwnerProfileDto.setAddress(restaurantOwner.getAddress());
		return restaurantOwnerProfileDto;
	}

	@Transactional
	@Override
	public void updateCustomerProfile(Long customerId, CustomerProfileDto customerProfileDto) {
		Customer customer = customerRepository.findById(customerId)
				.orElseThrow(() -> new RuntimeException("Customer not found with ID: " + customerId));
		customer.setFirstName(customerProfileDto.getFirstName());
		customer.setLastName(customerProfileDto.getLastName());
		customer.setPhoneNo(customerProfileDto.getPhoneNo());
		customer.setAddress(customerProfileDto.getAddress());
		customer.setCustomerPincode(customerProfileDto.getCustomerPincode());
		customerRepository.save(customer);
	}

	@Transactional
	@Override
	public void updateDeliveryAgentProfile(int agentId, DeliveryAgentRequestDto deliveryAgentProfileDto) {
		DeliveryPartner deliveryAgent = deliveryAgentRepository.findById(agentId)
				.orElseThrow(() -> new RuntimeException("Delivery Agent not found with ID: " + agentId));
		deliveryAgent.setName(deliveryAgentProfileDto.getName());
		deliveryAgent.setPhoneNo(deliveryAgentProfileDto.getPhoneNo());
		deliveryAgent.setAddress(deliveryAgentProfileDto.getAddress());
		deliveryAgentRepository.save(deliveryAgent);
	}

	@Transactional
	@Override
	public void updateRestaurantOwnerProfile(int ownerId, RestaurantOwnerRequestDto restaurantOwnerProfileDto) {
		RestaurantOwner restaurantOwner = restaurantOwnerRepository.findById(ownerId)
				.orElseThrow(() -> new RuntimeException("Restaurant Owner not found with ID: " + ownerId));
		restaurantOwner.setFirstName(restaurantOwnerProfileDto.getFirstName());
		restaurantOwner.setLastName(restaurantOwnerProfileDto.getLastName());
		restaurantOwner.setPhoneNo(restaurantOwnerProfileDto.getPhoneNo());
//			restaurantOwner.setAddress(restaurantOwnerProfileDto.getAddress());
		restaurantOwnerRepository.save(restaurantOwner);
	}

	@Override
	public List<DeliveryAgentResponseDto> getAllDeliveryPartners() {
		List<DeliveryPartner> agents = deliveryAgentRepository.findAll();
		List<DeliveryAgentResponseDto> result = new ArrayList<>();
		for (DeliveryPartner partner : agents) {
			DeliveryAgentResponseDto dto = new DeliveryAgentResponseDto();
			dto.setPartnerId(partner.getUserId());
			dto.setName(partner.getName());
			dto.setEmail(partner.getEmail());
			dto.setPhoneNo(partner.getPhoneNo());
			dto.setVehicleNumber(partner.getVehicleNumber());
			dto.setServicePincode(partner.getServicePincode());
			dto.setDeliveryPartnerStatus(partner.getDeliveryPartnerStatus());

			result.add(dto);
		}
		return result;
	}

	@Override
	public CustomerResponseDto getCustomerById(Long customerId) {
		Customer customer = customerRepository.findById(customerId)
				.orElseThrow(() -> new RuntimeException("Customer with ID " + customerId + " not found"));

		CustomerResponseDto response = new CustomerResponseDto();
		response.setUserId(customer.getUserId());
		response.setFirstName(customer.getFirstName());
		response.setEmail(customer.getEmail());
		response.setPhone(customer.getPhoneNo());
		response.setAddress(customer.getAddress());
		response.setCustomerPincode(customer.getCustomerPincode());
		return response;
	}
}