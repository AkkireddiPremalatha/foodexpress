package com.menu.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.menu.entity.auditing.AuditingFile;
import com.menu.enums.Category;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "menu_items") 
//@Data
@Getter
@Setter
@EqualsAndHashCode(of ="menuItemId",callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class MenuItem extends AuditingFile {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menu_item_id") 
    private Long menuItemId;

//    @Column(name = "category_id") 
//    private Long categoryId;

//    @Column(name = "subcategory_id") 
//    private Long subcategoryId;

    @Column(name = "item_name", nullable = false) 
    private String itemName;

    @Column(name = "item_description") 
    private String itemDescription;

    @Column(name = "price", nullable = false) 
    private Double price;

    @Column(name = "is_available", nullable = false) 
    private boolean isAvailable = true; 
    @Lob
    @Column(name = "item_image", columnDefinition = "LONGBLOB")
    private byte[] itemImage;

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

    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @JsonIgnore// Foreign key column in menu_items table
    private Restaurant restaurant;
    
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "category_id", nullable = false) // Foreign key column in menu_items table
//    private Category category;
    
    
    @Enumerated(EnumType.STRING)
    private Category category;
}
