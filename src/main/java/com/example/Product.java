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
    // public static Product of(String name, String desc, float price){
        //     Product product = new Product();
        //     // product.setId(UUID.randomUUID().toString());
    //     product.setName(name);
    //     product.setDesc(desc);
    //     product.setPrice(price);
    //     return product;
    // }
    // getters and setters
    // public void setId(long id){
    //     this.id = id;
    // }
    // public void setName(String name){
    //     this.name = name;
    // }
    // public void setDesc(String desc){
    //     this.desc = desc;
    // }
    // public void setPrice(float price){
    //     this.price = price;
    // }
    // public long getId(){ return this.id; }
    // public String getName(){ return this.name; }
    // public String getDesc(){ return this.desc; }
    // public float getPrice(){ return this.price; }
}
