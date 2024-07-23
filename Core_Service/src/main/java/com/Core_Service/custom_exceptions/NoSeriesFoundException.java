package com.Core_Service.custom_exceptions;

public class NoSeriesFoundException extends Exception{
    public NoSeriesFoundException(String message){
        super(message);
    }
}
