package com.Core_Service.controller;

import com.Core_Service.model_request.AdminCreateRequest;
import com.Core_Service.model_request.ViewerCreateRequest;
import com.Core_Service.model_response.UserResponse;
import com.Core_Service.service.AdminService;
import com.Core_Service.service.ViewerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

@RestController
@PreAuthorize("permitAll()")
public class SignupController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private ViewerService viewerService;

    @MutationMapping(name = "signUpAdmin")
    public UserResponse signUpAdmin(@Argument @Valid AdminCreateRequest adminCreateRequest) {
        return adminService.createAdmin(adminCreateRequest);
    }

    @MutationMapping(name = "signUpViewer")
    public UserResponse signUpViewer(@Argument @Valid ViewerCreateRequest viewerCreateRequest) {
        return viewerService.createViewer(viewerCreateRequest);
    }

}
