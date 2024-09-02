package com.Stream_Service.config_springsecurity;

import com.Stream_Service.config_jwt.AuthConverter;
import com.Stream_Service.config_jwt.AuthManager;
import com.Stream_Service.enums.Authority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;

@EnableWebFluxSecurity
@Configuration
public class SecurityConfig {
    @Autowired
    private ReactiveUserDetailsService userDetailsService;

    @Value("${allowed.origin}")
    private String allowedOrigin;

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
                .csrf().disable()
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration configuration = new CorsConfiguration();
                    configuration.setAllowedOrigins(Arrays.asList(allowedOrigin)); // Specify allowed origins
                    configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS")); // Allow all methods
                    configuration.setAllowedHeaders(Arrays.asList("Authorization", "Range", "Content-Type")); // Allow all headers
                    configuration.setAllowCredentials(true); // Allow credentials
                    return configuration;}));

        return http.build();
    }
}
