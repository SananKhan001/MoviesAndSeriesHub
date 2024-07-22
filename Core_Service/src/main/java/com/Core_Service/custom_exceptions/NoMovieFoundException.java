package com.Core_Service.custom_exceptions;

public class NoMovieFoundException extends Exception{
    public NoMovieFoundException(String message){
        super(message);
    }
}
