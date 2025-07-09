package com.foodexpress.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "delivery_partner")
@EqualsAndHashCode(of = "userId", callSuper = false)
public class DeliveryPartner extends AuditableEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int userId;
	private String name;

	@Column(unique = true)
	private String email;
	private String vehicleNumber;
	private String password;
	private String phoneNo;
	private String address;
	private String servicePincode;
	private Boolean deliveryPartnerStatus;

	@ManyToOne
	@JoinColumn(name = "roleid")
	private Roles roles;

}
