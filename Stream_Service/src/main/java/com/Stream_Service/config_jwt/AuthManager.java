package com.Stream_Service.config_jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import reactor.core.publisher.Mono;

@Configuration
public class AuthManager implements ReactiveAuthenticationManager {

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private ReactiveUserDetailsService reactiveUserDetailsService;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        return Mono.justOrEmpty(
                authentication
        )
                .cast(BearerToken.class)
                .flatMap(auth ->{
                    String username = jwtHelper.getUsernameFromToken(auth.getCredentials());
                    Mono<UserDetails> foundUser = reactiveUserDetailsService.findByUsername(username);
                    Mono<Authentication> authenticatedUser = foundUser.flatMap(u ->{
                        if(u.getUsername() == null) {
                            Mono.error(new IllegalArgumentException("No user found in auth manager !!!"));
                        }
                        if(jwtHelper.validateToken(auth.getCredentials(), u)){
                            SecurityContextHolder.getContext().setAuthentication(UsernamePasswordAuthenticationToken.authenticated(u.getUsername(), u.getPassword(), u.getAuthorities()));
                            return Mono.justOrEmpty(UsernamePasswordAuthenticationToken.authenticated(u.getUsername(), u.getPassword(), u.getAuthorities()));
                        }
                        Mono.error(new IllegalArgumentException("Invalid/Expired token !!!"));
                        return Mono.justOrEmpty(UsernamePasswordAuthenticationToken.unauthenticated(u.getUsername(), u.getPassword()));
                    });
                    return authenticatedUser;
                });
    }
}
