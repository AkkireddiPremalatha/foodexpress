package com.foodexpress.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "assigned_delivery_partner")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AssignedDeliveryPartner extends AuditableEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int partnerId;
	private int userId;
	private String name;
	private String email;
	private String phoneNumber;
	private String vehicleNumber;
	private String address;
	private String servicePincode;
	private Boolean deliveryPartnerStatus;
}
