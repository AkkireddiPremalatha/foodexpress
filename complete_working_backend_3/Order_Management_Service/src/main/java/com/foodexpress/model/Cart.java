package com.foodexpress.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "cart")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id", callSuper = true) // Include BaseEntity fields in equals/hashCode
public class Cart extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long customerId; // ID of the customer who owns the cart

    @Column(nullable = false)
    private Long menuItemId; // ID of the menu item added to the cart
        
    @Column(nullable = false)
    private int restaurantId; // ID of the restaurant offering the menu item
    
	@Column(nullable=false)
    private String itemName;

    @Column(nullable = false)
    private Integer quantity; // Quantity of the menu item

    @Column(nullable = false)
    private Double price; // Price of the menu item
}