package com.Stream_Service.config_springsecurity;

import com.Stream_Service.config_jwt.AuthConverter;
import com.Stream_Service.config_jwt.AuthManager;
import com.Stream_Service.enums.Authority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;

@EnableWebFluxSecurity
@Configuration
public class SecurityConfig {
    @Autowired
    private ReactiveUserDetailsService userDetailsService;

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http, AuthConverter jwtAuthConverter, AuthManager jwtAuthManager) {
        AuthenticationWebFilter jwtFilter = new AuthenticationWebFilter(jwtAuthManager);
        jwtFilter.setServerAuthenticationConverter(jwtAuthConverter);
        http
                .authorizeExchange(exchanges ->
                    {
                        exchanges.pathMatchers("/poster/upload/**", "/video/upload/**", "/delete/media/**").hasAuthority(Authority.ADMIN.toString());
                        exchanges.pathMatchers("/poster/get/**", "/profile/**", "/movie/stream/**", "/series/stream/**").permitAll();

                        exchanges.anyExchange().permitAll();
                    }
                )
                .addFilterAt(jwtFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .httpBasic().disable()
                .formLogin().disable()
                .csrf().disable();

        return http.build();
    }
}
