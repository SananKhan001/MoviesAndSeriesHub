package com.Core_Service.custom_exceptions;

public class SessionExpiredException extends Exception {
    public SessionExpiredException(String message){
        super(message);
    }
}
