package com.Stream_Service.controller;

import com.Stream_Service.models.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class AuthenticationController {
    @GetMapping("/auth/viewer")
    public Mono<String> authViewer(){
        return Mono.just("You are authenticated viewer !!!");
    }
    @GetMapping("/auth/admin")
    public Mono<String> authAdmin(){
        return Mono.just("You are authenticated admin !!!");
    }
}
