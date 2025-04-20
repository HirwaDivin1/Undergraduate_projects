package com.project.gogreen.exceptions;

//Task 13: Write code for CartItemAlreadyExistsException here
public class CartItemAlreadyExistsException extends RuntimeException{
    public CartItemAlreadyExistsException(String message){
        super(message);
    }
}