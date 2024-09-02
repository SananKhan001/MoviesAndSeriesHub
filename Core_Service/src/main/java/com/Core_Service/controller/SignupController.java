package com.Core_Service.controller;

import com.Core_Service.model_request.AdminCreateRequest;
import com.Core_Service.model_request.ViewerCreateRequest;
import com.Core_Service.model_response.UserResponse;
import com.Core_Service.service.AccountCreationService;
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
    private AccountCreationService accountCreationService;

    @MutationMapping(name = "signUpAdmin")
    public Boolean signUpAdmin(@Argument @Valid AdminCreateRequest adminCreateRequest) {
        return accountCreationService.generateAdminCreateRequest(adminCreateRequest);
    }

    @MutationMapping(name = "signUpViewer")
    public Boolean signUpViewer(@Argument @Valid ViewerCreateRequest viewerCreateRequest) {
        return accountCreationService.generateViewerCreateRequest(viewerCreateRequest);
    }

    @MutationMapping(name = "verifyOtpAdmin")
    public UserResponse verifyOtpAdmin(@Argument String otp) {
        return accountCreationService.verifyOtpAdmin(otp);
    }

    @MutationMapping(name = "verifyOtpViewer")
    UserResponse verifyOtpViewer(@Argument String otp) {
        return accountCreationService.verifyOtpViewer(otp);
    }

}
