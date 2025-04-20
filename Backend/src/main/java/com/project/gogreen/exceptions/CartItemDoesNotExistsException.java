package com.project.gogreen.exceptions;

// Task 13: Write code for CartItemDoesNotExistsException here
public class  CartItemDoesNotExistsException extends RuntimeException{
    public CartItemDoesNotExistsException(String message){
        super(message);
    }
}
