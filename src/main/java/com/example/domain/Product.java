package com.example.domain;

import java.util.ArrayList;
import java.util.List;

import io.smallrye.common.constraint.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class Product {
    
    private Long id;
    
    @NotBlank(message="Product name cannot be blank")
    @NotNull
    private String name;
    
    private String describtion;
 
    @Positive(message="Product price must be positive")
    private float price;

    private List<String> imageIds;

    public Product(String name, String desc, float price) {
        this.name = name;
        this.describtion = desc;
        this.price = price;
        this.imageIds = new ArrayList<String>();
    }
    public Product(Long id, String name, String desc, float price) {
        this.id = id;
        this.name = name;
        this.describtion = desc;
        this.price = price;
        this.imageIds = new ArrayList<String>();
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
    public List<String> getImageIds() {
        return imageIds;
    }

    public void setImageIds(List<String> imageIds) {
        this.imageIds = imageIds;
    }

    
}
