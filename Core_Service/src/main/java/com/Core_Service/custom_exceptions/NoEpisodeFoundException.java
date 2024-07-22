package com.Core_Service.custom_exceptions;

public class NoEpisodeFoundException extends Exception{
    public NoEpisodeFoundException(String message){
        super(message);
    }
}
