package com.Core_Service.custom_exceptions;

import java.util.function.Supplier;

public class NoUserFound extends Exception {
    public NoUserFound(String message){
        super(message);
    }
}
