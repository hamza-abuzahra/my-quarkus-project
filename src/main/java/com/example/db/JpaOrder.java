package com.example.db;

import java.util.List;

import io.smallrye.common.constraint.NotNull;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@Data
@AllArgsConstructor
public class JpaOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    
    @NotNull
    @ManyToOne(optional=false)
    @JoinColumn(name="user_id", referencedColumnName="id")
    private JpaUser user;
    
    @NotEmpty(message="products cannot be empty")
    @ManyToMany
    @JoinTable(
        name = "product_order",
        joinColumns = @JoinColumn(name="order_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name="product_id", referencedColumnName = "id")
    )
    private List<JpaProduct> products;
    
    public JpaOrder(JpaUser user, List<JpaProduct> products) {
        this.user = user;
        this.products = products;
    }
    public JpaOrder() {
    }
}
