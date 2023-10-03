package com.example.infrastructure.db;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JpaProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    
    @NotBlank(message="Name can not be blank.")
    private String name;
    
    private String describtion;
 
    @Positive(message="Price must be a positive number.")
    private float price;

    private List<String> imageIds;

    @ManyToMany(mappedBy = "products")
    private List<JpaOrder> orders;
    
    public JpaProduct(String name, String desc, float price) {
        this.name = name;
        this.describtion = desc;
        this.price = price;
        this.imageIds = new ArrayList<String>();        
    }
    public JpaProduct(String name, String desc, float price, List<String> imageIds) {
        this.name = name;
        this.describtion = desc;
        this.price = price;
        this.imageIds = imageIds;  
        this.orders = new ArrayList<JpaOrder>();
    }
    public JpaProduct(Long id, String name, String desc, float price, List<String> imageIds) {
        this.id = id;
        this.name = name;
        this.describtion = desc;
        this.price = price;
        this.imageIds = imageIds;  
        this.orders = new ArrayList<JpaOrder>();
    }
}