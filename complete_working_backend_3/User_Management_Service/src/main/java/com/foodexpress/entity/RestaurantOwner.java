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
@Table(name = "restaurantOwner")
@EqualsAndHashCode(of = "userId", callSuper = false)
public class RestaurantOwner extends AuditableEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int userId;
	private String firstName;
	private String lastName;

	@Column(unique = true)
	private String email;

	private String password;
	private String phoneNo;
	private String address;
	
	@Column(name = "restaurant_name", nullable = false, unique = true)
	private String restaurantName;

	@Column(name = "restaurant_pin", nullable = false) 
    private String restaurantPin;
	
	@Column(name = "restaurant_location") 
    private String restaurantLocation; 

	@ManyToOne
	@JoinColumn(name = "roleid")
	private Roles roles;

}
