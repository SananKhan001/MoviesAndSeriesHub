package com.Core_Service.service;

import com.Core_Service.config_jwt.JwtHelper;
import com.Core_Service.model_request.JWTRequest;
import com.Core_Service.model_response.JWTResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtHelper helper;
    public JWTResponse generateToken(JWTRequest credentials) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(credentials.getUsername());
        String token = this.helper.generateToken(userDetails);
        return JWTResponse.builder().jwtToken(token).build();
    }
}
