/*
 * package com.foodexpress.entity;
 * 
 * import java.time.LocalDateTime;
 * 
 * import jakarta.persistence.Column; import jakarta.persistence.Entity; import
 * jakarta.persistence.GeneratedValue; import
 * jakarta.persistence.GenerationType; import jakarta.persistence.Id; import
 * jakarta.persistence.Table; import lombok.Data;
 * 
 * @Entity
 * 
 * @Table(name = "delivery_partner")
 * 
 * @Data public class DeliveryPartner {
 * 
 * @Id
 * 
 * @GeneratedValue(strategy = GenerationType.IDENTITY) private Integer
 * partnerId;
 * 
 * private String name; private String phoneNumber; private String
 * vehicleNumber;
 * 
 * @Column(unique=true) private String email; private String servicePincode;
 * private Boolean deliveryPartnerStatus;
 * 
 * @Column(nullable = false, updatable = false) private LocalDateTime
 * createdDate; private LocalDateTime updatedDate; }
 */