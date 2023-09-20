package com.example.db;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity
@Data
public class JpaUser {

    @Id
    @GeneratedValue
    private Long id;
    
    @NotBlank(message="first Name can not be blank.")
    private String fname;
    
    private String lname;
 
    @Email(message="Not in correct email form")
    private String email;
    

    public JpaUser(String fname, String lname, String email) {
        this.fname = fname;
        this.lname = lname;
        this.email = email;
    }

    public JpaUser() {
    }
}
