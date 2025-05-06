package com.project.gogreen.exceptions;

//Task 13: Write code for ProductNotFoundException here
public class ProductNotFoundException extends RuntimeException{
    public ProductNotFoundException(String message){
        super(message);
    }
}
