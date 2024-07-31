package com.Stream_Service.config_jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.reactive.context.ReactiveWebServerApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.context.ContextLoader;
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
                            return Mono.error(new IllegalArgumentException("No user found in auth manager !!!"));
                        }
                        if(jwtHelper.validateToken(auth.getCredentials(), u)){
                            SecurityContext.setPrincipal(u);
                            return Mono.justOrEmpty(UsernamePasswordAuthenticationToken.authenticated(u.getUsername(), u.getPassword(), u.getAuthorities()));
                        }
                        return Mono.error(new IllegalArgumentException("Invalid/Expired token !!!"));
                    });
                    return authenticatedUser;
                });
    }
}
