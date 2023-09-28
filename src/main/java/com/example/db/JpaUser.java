package com.example.db;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@Data
@AllArgsConstructor
public class JpaUser {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    
    @NotBlank(message="first Name can not be blank.")
    private String fname;
    
    private String lname;
 
    @Column
    @Email(message="Not in correct email form")
    private String email;
    
    @OneToMany(mappedBy="user")
    private List<JpaOrder> orders;

    public JpaUser(Long id, String fname, String lname, String email) {
        this.id = id;
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.orders = new ArrayList<JpaOrder>();
    }
    public JpaUser(String fname, String lname, String email) {
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.orders = new ArrayList<JpaOrder>();
    }

    public JpaUser() {
    }

}
