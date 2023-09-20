package com.example.db;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Data
public class JpaOrder {

    @Id
    @GeneratedValue
    private Long id;
    
    @NotNull(message="user id cannot be null")
    private Long userId;
    
    @NotEmpty(message="products cannot be empty")
    private List<Long> productId;
    
    public JpaOrder(Long userId, List<Long> productId) {
        this.userId = userId;
        this.productId = productId;
    }
    public JpaOrder() {
    }
}
