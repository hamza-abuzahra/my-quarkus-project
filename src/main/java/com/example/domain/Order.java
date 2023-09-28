package com.example.domain;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Order {
    
    private Long id;
    @NotNull(message="user id must be provided")
    private User user;
    @NotEmpty(message="products cannot be empty")
    private List<Product> products;
    
    public Order(User user, List<Product> products) {
        this.user = user;
        this.products = products;
    }
    public Order() {
        this.products = new ArrayList<Product>();
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public List<Product> getProducts() {
        return products;
    }
    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
