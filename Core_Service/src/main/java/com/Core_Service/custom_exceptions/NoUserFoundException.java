package com.Core_Service.custom_exceptions;

import java.util.function.Supplier;

public class NoUserFoundException extends Exception {
    public NoUserFoundException(String message){
        super(message);
    }
}
