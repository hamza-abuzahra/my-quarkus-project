package com.example.db;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Entity
@Data
public class JpaProduct {

    @Id
    @GeneratedValue
    private Long id;
    
    @NotBlank(message="Name can not be blank.")
    private String name;
    
    private String describtion;
 
    @Positive(message="Price must be a positive number.")
    private float price;

    private List<String> imageIds;
    
    public JpaProduct(String name, String desc, float price) {
        this.name = name;
        this.describtion = desc;
        this.price = price;
    }

    public JpaProduct() {
    }
}
