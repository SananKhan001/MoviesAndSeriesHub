package com.Stream_Service.config_jwt;

import com.Stream_Service.models.User;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.reactive.context.ReactiveWebApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

public class SecurityContext {
    private static UserDetails principal;

    public static void setPrincipal(UserDetails principal){
        SecurityContext.principal = principal;
    }
    public static UserDetails principal(){
        return SecurityContext.principal;
    }
}
