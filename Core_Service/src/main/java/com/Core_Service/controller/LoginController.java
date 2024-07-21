package com.Core_Service.controller;

import com.Core_Service.config_jwt.Authenticator;
import com.Core_Service.model_request.JWTRequest;
import com.Core_Service.model_response.JWTResponse;
import com.Core_Service.service.AuthenticationService;
import com.Core_Service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

@PreAuthorize("permitAll()")
@RestController
public class LoginController {

    @Autowired
    private Authenticator authenticator;

    @Autowired
    private AuthenticationService authenticationService;

    @QueryMapping(name = "login")
    public JWTResponse login(@Argument JWTRequest credentials){
        authenticator.doAuthenticate(credentials.getUsername(), credentials.getPassword());
        return authenticationService.generateToken(credentials);
    }

}
