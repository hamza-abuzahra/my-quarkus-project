package com.example.domain;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class Order {
    
    private Long id;
    @NotNull(message="user id must be provided")
    private Long userId;
    @NotEmpty(message="products cannot be empty")
    private List<Long> productId;
    public Order(Long userId, List<Long> productId) {
        this.userId = userId;
        this.productId = productId;
    }
    public Order() {
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public List<Long> getProductId() {
        return productId;
    }
    public void setProductId(List<Long> productId) {
        this.productId = productId;
    }
}
