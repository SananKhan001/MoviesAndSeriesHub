package com.Core_Service.helpers;

import java.util.UUID;

public final class Helper {
    public static String generateUUID(){
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
