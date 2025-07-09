package com.menu.entity;
import java.util.List;

import com.menu.entity.auditing.AuditingFile;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "restaurants")
//@Data
@Getter
@Setter
@EqualsAndHashCode(of ="id",callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Restaurant extends AuditingFile {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "restaurant_id")
	private long id;
	
	private Long userId;
	
	@Column(name = "restaurant_name", nullable = false, unique = true)
	private String restaurantName;
	
	

	@Column(name = "restaurant_pin", nullable = false) 
    private String restaurantPin;
	
	@Column(name = "restaurant_location") 
    private String restaurantLocation;

    @Column(name = "contact_number") 
    private String contactNumber;

    @Column(name = "email") 
    private String email;

//    @CreationTimestamp 
//    @Column(name = "created_date", nullable = false, updatable = false) 
//    private LocalDateTime createdDate;
//
//    @Column(name = "created_by") 
//    private String createdBy; 
//
//    @UpdateTimestamp 
//    @Column(name = "updated_date") 
//    private LocalDateTime updatedDate;
//
//    @Column(name = "updated_by") 
//    private String updatedBy; 
    
    private boolean isOpen;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MenuItem> menuItems;

	
}
