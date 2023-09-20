package com.example.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public class Product {
    
    private Long id;
    
    @NotBlank(message="Name can not be blank.")
    private String name;
    
    private String describtion;
 
    @Positive(message="Price must be a positive number.")
    private float price;
    
    public Product(Long id, String name, String desc, float price) {
        this.id = id;
        this.name = name;
        this.describtion = desc;
        this.price = price;
    }

    public Product() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescribtion() {
        return describtion;
    }

    public void setDescribtion(String describtion) {
        this.describtion = describtion;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
    
}
