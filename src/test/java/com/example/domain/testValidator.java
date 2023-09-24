package com.example.domain;


import java.util.ArrayList;
import java.util.Set;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

@ApplicationScoped
public class testValidator {
    private Validator validator;

    public testValidator() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    public ArrayList<String> violations(Object obj){
        Set<ConstraintViolation<Object>> violations = validator.validate(obj);
        ArrayList<String> violationsList = new ArrayList<String>();
        
        for (ConstraintViolation<Object> violation : violations) {
            violationsList.add(violation.getMessage());
        }
        return violationsList;
    }    

}
