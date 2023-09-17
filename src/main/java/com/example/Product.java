package com.example;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="products")
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Product implements Serializable {
    
    @Id
    @GeneratedValue
    private Long id;
    
    @Column(nullable=false)
    private String name;
    
    private String describtion;
    
    private float price;
    
    public Product(String string, String string2, float f) {
        this.name = string;
        this.describtion = string2;
        this.price = f;
    }
}
