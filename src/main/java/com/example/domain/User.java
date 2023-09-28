package com.example.domain;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class User {
    
    private Long id;
    
    @NotBlank(message="User name can not be blank")
    private String fname;

    private String lname;
    
    @Email(message="email not valid")
    @NotBlank(message="email not provided")
    private String email;

    private List<Order> orders;

    public User() {
    }
    public User(Long id, String fname, String lname, String email) {
        this.id = id;
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.orders = new ArrayList<Order>();
    }
    public User(String fname, String lname, String email) {
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.orders = new ArrayList<Order>();
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getFname() {
        return fname;
    }
    public void setFname(String fname) {
        this.fname = fname;
    }
    public String getLname() {
        return lname;
    }
    public void setLname(String lname) {
        this.lname = lname;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public List<Order> getOrders() {
        return orders;
    }
    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
}
