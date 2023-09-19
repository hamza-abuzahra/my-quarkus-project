package com.example;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.xml.bind.annotation.XmlRootElement;
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
@XmlRootElement
public class Product implements Serializable {
    
    @Id
    @GeneratedValue
    private Long id;
    
    @Column(nullable=false)
    @NotBlank(message="Name can not be blank.")
    private String name;
    
    private String describtion;
 
    @Positive(message="Price must be a positive number.")
    private float price;
    
    public Product(String string, String string2, float f) {
        this.name = string;
        this.describtion = string2;
        this.price = f;
    }
}
