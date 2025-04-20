package com.project.gogreen.exceptions;

//Task 13: Write code for UserNot Found Exception here:
public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String message){
        super(message);
    }
}
